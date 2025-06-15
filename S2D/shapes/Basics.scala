package S2D.shapes

import S2D.types.{Color, Rectangle, Vector2}
import gl.GL._
import gl.GLExtras._

object Basics:
  def pixel(posX: Int, posY: Int, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)
    glPointSize(1.0f)

    glBegin(GL_POINTS.toUInt)
    glVertex2f(posX.toFloat, posY.toFloat)
    glEnd()

  def pixel(position: Vector2, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)
    glPointSize(1.0f)

    glBegin(GL_POINTS.toUInt)
    glVertex2f(position.x, position.y)
    glEnd()

  def line(startPosX: Int, startPosY: Int, endPosX: Int, endPosY: Int, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINES.toUInt)
    glVertex2f(startPosX.toFloat, startPosY.toFloat)
    glVertex2f(endPosX.toFloat, endPosY.toFloat)
    glEnd()

  def line(startPos: Vector2, endPos: Vector2, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINES.toUInt)
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

    glBegin(GL_QUADS.toUInt)
    glVertex2f(startPos.x + perpX, startPos.y + perpY)
    glVertex2f(startPos.x - perpX, startPos.y - perpY)
    glVertex2f(endPos.x - perpX, endPos.y - perpY)
    glVertex2f(endPos.x + perpX, endPos.y + perpY)
    glEnd()

  def lineStrip(points: Array[Vector2], color: Color): Unit =
    if points.length < 2 then return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINE_STRIP.toUInt)
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

    glBegin(GL_TRIANGLE_FAN.toUInt)
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

    glBegin(GL_TRIANGLE_FAN.toUInt)
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

    glBegin(GL_TRIANGLE_FAN.toUInt)
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

    glBegin(GL_LINE_STRIP.toUInt)
    for i <- 0 to segments do
      val angle = startRad + (i * angleStep)
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()

    glBegin(GL_LINES.toUInt)
    glVertex2f(center.x, center.y)
    glVertex2f(center.x + radius * math.cos(startRad).toFloat, center.y + radius * math.sin(startRad).toFloat)

    glVertex2f(center.x, center.y)
    glVertex2f(center.x + radius * math.cos(endRad).toFloat, center.y + radius * math.sin(endRad).toFloat)
    glEnd()

  def circleGradient(centerX: Int, centerY: Int, radius: Float, inner: Color, outer: Color): Unit =
    val segments = 36

    glBegin(GL_TRIANGLE_FAN.toUInt)

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

    glBegin(GL_LINE_LOOP.toUInt)

    for i <- 0 until segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radius * math.cos(angle).toFloat
      val y = centerY + radius * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()

  def circleOutline(center: Vector2, radius: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val segments = 36

    glBegin(GL_LINE_LOOP.toUInt)

    for i <- 0 until segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()

  def ellipse(centerX: Int, centerY: Int, radiusH: Float, radiusV: Float, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val segments = 36

    glBegin(GL_TRIANGLE_FAN.toUInt)
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

    glBegin(GL_LINE_LOOP.toUInt)

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radiusH * math.cos(angle).toFloat
      val y = centerY + radiusV * math.sin(angle).toFloat
      glVertex2f(x, y)

    glEnd()