package S2D

import org.lwjgl.opengl.GL11.*

object shapes:
  private var shapesTexture: Texture2D = Texture2D(0, 0, 0, 0, 0) // EMPTY TEXTURE
  private var shapesTextureSource: Rectangle = Rectangle(0.0f, 0.0f, 0.0f, 0.0f)
  private var shapesTextureEnabled: Boolean = false

  // SETUP FUNCTIONS
  def SetShapesTexture(texture: Texture2D, source: Rectangle): Unit =
    shapesTexture = texture
    shapesTextureSource = source
    shapesTextureEnabled = texture.id != 0
  def GetShapesTexture(): Texture2D =
    shapesTexture
  def GetShapesTextureRectangle(): Rectangle =
    shapesTextureSource

  // BASIC SHAPES DRAWING FUNCTIONS
  def DrawPixel(posX: Int, posY: Int, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)
    glPointSize(1.0f)

    glBegin(GL_POINTS)
    glVertex2f(posX.toFloat, posY.toFloat)
    glEnd()
  def DrawPixelV(position: Vector2, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)
    glPointSize(1.0f)

    glBegin(GL_POINTS)
    glVertex2f(position.x, position.y)
    glEnd()
  def DrawLine(startPosX: Int, startPosY: Int, endPosX: Int, endPosY: Int, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINES)
    glVertex2f(startPosX.toFloat, startPosY.toFloat)
    glVertex2f(endPosX.toFloat, endPosY.toFloat)
    glEnd()
  def DrawLineV(startPos: Vector2, endPos: Vector2, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINES)
    glVertex2f(startPos.x, startPos.y)
    glVertex2f(endPos.x, endPos.y)
    glEnd()
  def DrawLineEx(startPos: Vector2, endPos: Vector2, thick: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val dx = endPos.x - startPos.x
    val dy = endPos.y - startPos.y

    val length = math.sqrt(dx * dx + dy * dy).toFloat
    if length == 0.0f then return // no division by zero

    val perpX = -dy / length * (thick / 2.0f)
    val perpY = dx / length * (thick / 2.0f)

    glBegin(GL_QUADS)
    glVertex2f(startPos.x + perpX, startPos.y + perpY)
    glVertex2f(startPos.x - perpX, startPos.y - perpY)
    glVertex2f(endPos.x - perpX, endPos.y - perpY)
    glVertex2f(endPos.x + perpX, endPos.y + perpY)
    glEnd()
  def DrawLineStrip(points: Array[Vector2], color: Color): Unit =
    if points.length < 2 then return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINE_STRIP)
    for point <- points do
      glVertex2f(point.x, point.y)
    glEnd()
  def DrawLineBezier(startPos: Vector2, endPos: Vector2, thick: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val dx = endPos.x - startPos.x
    val dy = endPos.y - startPos.y

    val control1 = Vector2(startPos.x + dx * 0.25f, startPos.y)
    val control2 = Vector2(startPos.x + dx * 0.75f, endPos.y)

    val segments = 20
    val points = Array.ofDim[Vector2](segments + 1)

    for i <- 0 to segments do
      val t = i.toFloat / segments.toFloat
      val oneMinusT = 1.0f - t

      val x = oneMinusT * oneMinusT * oneMinusT * startPos.x +
        3 * oneMinusT * oneMinusT * t * control1.x +
        3 * oneMinusT * t * t * control2.x +
        t * t * t * endPos.x

      val y = oneMinusT * oneMinusT * oneMinusT * startPos.y +
        3 * oneMinusT * oneMinusT * t * control1.y +
        3 * oneMinusT * t * t * control2.y +
        t * t * t * endPos.y

      points(i) = Vector2(x, y)

    for i <- 0 until segments do
      DrawLineEx(points(i), points(i + 1), thick, color)
  def DrawCircle(centerX: Int, centerY: Int, radius: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val segments = 36

    glBegin(GL_TRIANGLE_FAN)
    glVertex2f(centerX.toFloat, centerY.toFloat)

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radius * math.cos(angle).toFloat
      val y = centerY + radius * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()
  def DrawCircleSector(center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
    if segments < 3 then return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val startRad = math.toRadians(startAngle).toFloat
    val endRad = math.toRadians(endAngle).toFloat

    val angleStep = (endRad - startRad) / segments.toFloat

    glBegin(GL_TRIANGLE_FAN)
    glVertex2f(center.x, center.y)

    for i <- 0 to segments do
      val angle = startRad + (i * angleStep)
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()
  def DrawCircleSectorLines(center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
    if segments < 3 then return
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val startRad = math.toRadians(startAngle).toFloat
    val endRad = math.toRadians(endAngle).toFloat

    val angleStep = (endRad - startRad) / segments.toFloat

    glBegin(GL_LINE_STRIP)
    for i <- 0 to segments do
      val angle = startRad + (i * angleStep)
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()

    glBegin(GL_LINES)
    glVertex2f(center.x, center.y)
    glVertex2f(center.x + radius * math.cos(startRad).toFloat, center.y + radius * math.sin(startRad).toFloat)

    glVertex2f(center.x, center.y)
    glVertex2f(center.x + radius * math.cos(endRad).toFloat, center.y + radius * math.sin(endRad).toFloat)
    glEnd()
  def DrawCircleGradient(centerX: Int, centerY: Int, radius: Float, inner: Color, outer: Color): Unit =
    val segments = 36

    glBegin(GL_TRIANGLE_FAN)

    glColor4f(inner.r / 255.0f, inner.g / 255.0f, inner.b / 255.0f, inner.a / 255.0f)
    glVertex2f(centerX.toFloat, centerY.toFloat)

    glColor4f(outer.r / 255.0f, outer.g / 255.0f, outer.b / 255.0f, outer.a / 255.0f)

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radius * math.cos(angle).toFloat
      val y = centerY + radius * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()
  def DrawCircleV(center: Vector2, radius: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val segments = 36

    glBegin(GL_TRIANGLE_FAN)
    glVertex2f(center.x, center.y)

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()
  def DrawCircleLines(centerX: Int, centerY: Int, radius: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val segments = 36

    glBegin(GL_LINE_LOOP)

    for i <- 0 until segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radius * math.cos(angle).toFloat
      val y = centerY + radius * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()