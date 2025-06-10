package S2D.shapes

import S2D.types.{Color, Rectangle, Vector2}
import org.lwjgl.opengl.GL11.*

object Basics:
  // BASIC SHAPES DRAWING FUNCTIONS
  def pixel(posX: Int, posY: Int, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)
    glPointSize(1.0f)

    glBegin(GL_POINTS)
    glVertex2f(posX.toFloat, posY.toFloat)
    glEnd()
  def pixel(position: Vector2, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)
    glPointSize(1.0f)

    glBegin(GL_POINTS)
    glVertex2f(position.x, position.y)
    glEnd()
  def line(startPosX: Int, startPosY: Int, endPosX: Int, endPosY: Int, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINES)
    glVertex2f(startPosX.toFloat, startPosY.toFloat)
    glVertex2f(endPosX.toFloat, endPosY.toFloat)
    glEnd()
  def line(startPos: Vector2, endPos: Vector2, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINES)
    glVertex2f(startPos.x, startPos.y)
    glVertex2f(endPos.x, endPos.y)
    glEnd()
  def lineThick(startPos: Vector2, endPos: Vector2, thick: Float, color: Color): Unit =
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
  def lineStrip(points: Array[Vector2], color: Color): Unit =
    if points.length < 2 then return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINE_STRIP)
    for point <- points do
      glVertex2f(point.x, point.y)
    glEnd()
  def lineBezier(startPos: Vector2, endPos: Vector2, thick: Float, color: Color): Unit =
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
      lineThick(points(i), points(i + 1), thick, color)
  def circle(centerX: Int, centerY: Int, radius: Float, color: Color): Unit =
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
  def circle(center: Vector2, radius: Float, color: Color): Unit =
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
  def circleSector(center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
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
  def circleSectorOutline(center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
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
  def circleGradient(centerX: Int, centerY: Int, radius: Float, inner: Color, outer: Color): Unit =
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
  def circleOutline(centerX: Int, centerY: Int, radius: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val segments = 36

    glBegin(GL_LINE_LOOP)

    for i <- 0 until segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radius * math.cos(angle).toFloat
      val y = centerY + radius * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()
  def circleOutline(center: Vector2, radius: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val segments = 36

    glBegin(GL_LINE_LOOP)

    for i <- 0 until segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()
  def ellipse(centerX: Int, centerY: Int, radiusH: Float, radiusV: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val segments = 36

    glBegin(GL_TRIANGLE_FAN)
    glVertex2f(centerX.toFloat, centerY.toFloat)

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radiusH * math.cos(angle).toFloat
      val y = centerY + radiusV * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()
  def ellipseOutlines(centerX: Int, centerY: Int, radiusH: Float, radiusV: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val segments = 36

    glBegin(GL_LINE_LOOP)

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radiusH * math.cos(angle).toFloat
      val y = centerY + radiusV * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()
  def ring(center: Vector2, innerRadius: Float, outerRadius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
    if segments < 3 then return
    if innerRadius >= outerRadius then return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val startRad = math.toRadians(startAngle).toFloat
    val endRad = math.toRadians(endAngle).toFloat

    val angleStep = (endRad - startRad) / segments.toFloat

    glBegin(GL_TRIANGLE_STRIP)

    for i <- 0 to segments do
      val angle = startRad + (i * angleStep)
      val cosAngle = math.cos(angle).toFloat
      val sinAngle = math.sin(angle).toFloat

      val innerX = center.x + innerRadius * cosAngle
      val innerY = center.y + innerRadius * sinAngle
      glVertex2f(innerX, innerY)

      val outerX = center.x + outerRadius * cosAngle
      val outerY = center.y + outerRadius * sinAngle
      glVertex2f(outerX, outerY)

    glEnd()
  def ringOutlines(center: Vector2, innerRadius: Float, outerRadius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
    if segments < 3 then return
    if innerRadius >= outerRadius then return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val startRad = math.toRadians(startAngle).toFloat
    val endRad = math.toRadians(endAngle).toFloat

    val angleStep = (endRad - startRad) / segments.toFloat

    glBegin(GL_LINE_STRIP)

    for i <- 0 to segments do
      val angle = startRad + (i * angleStep)
      val x = center.x + innerRadius * math.cos(angle).toFloat
      val y = center.y + innerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()

    glBegin(GL_LINE_STRIP)
    for i <- 0 to segments do
      val angle = startRad + (i * angleStep)
      val x = center.x + outerRadius * math.cos(angle).toFloat
      val y = center.y + outerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()
  def rectangle(posX: Int, posY: Int, width: Int, height: Int, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_QUADS)
    glVertex2f(posX.toFloat, posY.toFloat)
    glVertex2f((posX + width).toFloat, posY.toFloat)
    glVertex2f((posX + width).toFloat, (posY + height).toFloat)
    glVertex2f(posX.toFloat, (posY + height).toFloat)
    glEnd()
  def rectangle(pos: Vector2, size: Vector2, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_QUADS)
    glVertex2f(pos.x, pos.y)
    glVertex2f((pos.x + size.x), pos.y)
    glVertex2f((pos.x + size.x), (pos.y + size.y))
    glVertex2f(pos.x, (pos.y + size.y))
    glEnd()
  def rectangle(rectangle: Rectangle, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_QUADS)
    glVertex2f(rectangle.x, rectangle.y)
    glVertex2f((rectangle.x + rectangle.width), rectangle.y)
    glVertex2f((rectangle.x + rectangle.width), (rectangle.y + rectangle.height))
    glVertex2f(rectangle.x, (rectangle.y + rectangle.height))
    glEnd()
  def rectangleRotated(rectangle: Rectangle, origin: Vector2, rotation: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glPushMatrix()

    glTranslatef(rectangle.x + origin.x, rectangle.y + origin.y, 0.0f)

    glRotatef(rotation, 0.0f, 0.0f, 1.0f)

    glBegin(GL_QUADS)
    glVertex2f(-origin.x, -origin.y)
    glVertex2f(rectangle.width - origin.x, -origin.y)
    glVertex2f(rectangle.width - origin.x, rectangle.height - origin.y)
    glVertex2f(-origin.x, rectangle.height - origin.y)
    glEnd()

    glPopMatrix()
  def rectangleGradientV(posX: Int, posY: Int, width: Int, height: Int, top: Color, bottom: Color): Unit =
    glBegin(GL_QUADS)

    glColor4f(top.r / 255.0f, top.g / 255.0f, top.b / 255.0f, top.a / 255.0f)
    glVertex2f(posX.toFloat, posY.toFloat)

    glColor4f(top.r / 255.0f, top.g / 255.0f, top.b / 255.0f, top.a / 255.0f)
    glVertex2f((posX + width).toFloat, posY.toFloat)

    glColor4f(bottom.r / 255.0f, bottom.g / 255.0f, bottom.b / 255.0f, bottom.a / 255.0f)
    glVertex2f((posX + width).toFloat, (posY + height).toFloat)

    glColor4f(bottom.r / 255.0f, bottom.g / 255.0f, bottom.b / 255.0f, bottom.a / 255.0f)
    glVertex2f(posX.toFloat, (posY + height).toFloat)

    glEnd()
  def rectangleGradientH(posX: Int, posY: Int, width: Int, height: Int, left: Color, right: Color): Unit =
    glBegin(GL_QUADS)

    glColor4f(left.r / 255.0f, left.g / 255.0f, left.b / 255.0f, left.a / 255.0f)
    glVertex2f(posX.toFloat, posY.toFloat)

    glColor4f(right.r / 255.0f, right.g / 255.0f, right.b / 255.0f, right.a / 255.0f)
    glVertex2f((posX + width).toFloat, posY.toFloat)

    glColor4f(right.r / 255.0f, right.g / 255.0f, right.b / 255.0f, right.a / 255.0f)
    glVertex2f((posX + width).toFloat, (posY + height).toFloat)

    glColor4f(left.r / 255.0f, left.g / 255.0f, left.b / 255.0f, left.a / 255.0f)
    glVertex2f(posX.toFloat, (posY + height).toFloat)

    glEnd()
  def rectangleGradient(rectangle: Rectangle, topLeft: Color, bottomLeft: Color, topRight: Color, bottomRight: Color): Unit =
    glBegin(GL_QUADS)

    glColor4f(topLeft.r / 255.0f, topLeft.g / 255.0f, topLeft.b / 255.0f, topLeft.a / 255.0f)
    glVertex2f(rectangle.x, rectangle.y)

    glColor4f(topRight.r / 255.0f, topRight.g / 255.0f, topRight.b / 255.0f, topRight.a / 255.0f)
    glVertex2f((rectangle.x + rectangle.width), rectangle.y)

    glColor4f(bottomRight.r / 255.0f, bottomRight.g / 255.0f, bottomRight.b / 255.0f, bottomRight.a / 255.0f)
    glVertex2f((rectangle.x + rectangle.width), (rectangle.y + rectangle.height))

    glColor4f(bottomLeft.r / 255.0f, bottomLeft.g / 255.0f, bottomLeft.b / 255.0f, bottomLeft.a / 255.0f)
    glVertex2f(rectangle.x, (rectangle.y + rectangle.height))

    glEnd()
  def rectangleOutline(posX: Int, posY: Int, width: Int, height: Int, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINE_LOOP)
    glVertex2f(posX.toFloat, posY.toFloat)
    glVertex2f((posX + width).toFloat, posY.toFloat)
    glVertex2f((posX + width).toFloat, (posY + height).toFloat)
    glVertex2f(posX.toFloat, (posY + height).toFloat)
    glEnd()
  def rectangleOutline(pos: Vector2, size: Vector2, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINE_LOOP)
    glVertex2f(pos.x, pos.y)
    glVertex2f(pos.x + size.x, pos.y)
    glVertex2f(pos.x + size.x, pos.y + size.y)
    glVertex2f(pos.x, pos.y + size.y)
    glEnd()
  def rectangleOutline(rectangle: Rectangle, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINE_LOOP)
    glVertex2f(rectangle.x, rectangle.y)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + rectangle.height)
    glVertex2f(rectangle.x, rectangle.y + rectangle.height)
    glEnd()
  def rectangleOutlineThick(rectangle: Rectangle, thick: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_QUADS)

    glVertex2f(rectangle.x, rectangle.y)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + thick)
    glVertex2f(rectangle.x, rectangle.y + thick)

    glVertex2f(rectangle.x + rectangle.width - thick, rectangle.y + thick)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + thick)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + rectangle.height)
    glVertex2f(rectangle.x + rectangle.width - thick, rectangle.y + rectangle.height)

    glVertex2f(rectangle.x, rectangle.y + rectangle.height - thick)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + rectangle.height - thick)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + rectangle.height)
    glVertex2f(rectangle.x, rectangle.y + rectangle.height)

    glVertex2f(rectangle.x, rectangle.y)
    glVertex2f(rectangle.x + thick, rectangle.y)
    glVertex2f(rectangle.x + thick, rectangle.y + rectangle.height - thick)
    glVertex2f(rectangle.x, rectangle.y + rectangle.height - thick)

    glEnd()
  def rectangleRounded(rect: Rectangle, roundness: Float, segments: Int, color: Color): Unit =
    if segments < 3 then return
    if roundness <= 0.0f then
      rectangle(rect, color)
      return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val maxRadius = math.min(rect.width, rect.height) / 2.0f
    val cornerRadius = (roundness * maxRadius).min(maxRadius)

    val innerWidth = rect.width - 2 * cornerRadius
    val innerHeight = rect.height - 2 * cornerRadius

    if innerWidth > 0 && innerHeight > 0 then
      glBegin(GL_QUADS)
      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius)
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + cornerRadius)
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + cornerRadius + innerHeight)
      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius + innerHeight)
      glEnd()

    if innerWidth > 0 then
      glBegin(GL_QUADS)
      glVertex2f(rect.x + cornerRadius, rect.y)
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y)
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + cornerRadius)
      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius)

      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius + innerHeight)
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + cornerRadius + innerHeight)
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + rect.height)
      glVertex2f(rect.x + cornerRadius, rect.y + rect.height)
      glEnd()

    if innerHeight > 0 then
      glBegin(GL_QUADS)
      glVertex2f(rect.x, rect.y + cornerRadius)
      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius)
      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius + innerHeight)
      glVertex2f(rect.x, rect.y + cornerRadius + innerHeight)

      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + cornerRadius)
      glVertex2f(rect.x + rect.width, rect.y + cornerRadius)
      glVertex2f(rect.x + rect.width, rect.y + cornerRadius + innerHeight)
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + cornerRadius + innerHeight)
      glEnd()

    val angleStep = (math.Pi / 2.0f) / segments.toFloat

    glBegin(GL_TRIANGLE_FAN)
    glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius)
    for i <- 0 to segments do
      val angle = math.Pi + (i * angleStep)
      val x = rect.x + cornerRadius + cornerRadius * math.cos(angle).toFloat
      val y = rect.y + cornerRadius + cornerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()

    glBegin(GL_TRIANGLE_FAN)
    glVertex2f(rect.x + rect.width - cornerRadius, rect.y + cornerRadius)
    for i <- 0 to segments do
      val angle = (3 * math.Pi / 2.0f) + (i * angleStep)
      val x = rect.x + rect.width - cornerRadius + cornerRadius * math.cos(angle).toFloat
      val y = rect.y + cornerRadius + cornerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()

    glBegin(GL_TRIANGLE_FAN)
    glVertex2f(rect.x + rect.width - cornerRadius, rect.y + rect.height - cornerRadius)
    for i <- 0 to segments do
      val angle = (i * angleStep)
      val x = rect.x + rect.width - cornerRadius + cornerRadius * math.cos(angle).toFloat
      val y = rect.y + rect.height - cornerRadius + cornerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()

    glBegin(GL_TRIANGLE_FAN)
    glVertex2f(rect.x + cornerRadius, rect.y + rect.height - cornerRadius)
    for i <- 0 to segments do
      val angle = (math.Pi / 2.0f) + (i * angleStep)
      val x = rect.x + cornerRadius + cornerRadius * math.cos(angle).toFloat
      val y = rect.y + rect.height - cornerRadius + cornerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()