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