package S2D.shapes

import S2D.types.{Color, Rectangle, Vector2}
import gl.GL.*
import gl.GLExtras.*

object Basics:
  def pixel(posX: Int, posY: Int, color: Color): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )
    glPointSize(1.0f)

    glBegin(GL_POINTS.toUInt)
    glVertex2f(posX.toFloat, posY.toFloat)
    glEnd()
  end pixel

  def pixel(position: Vector2, color: Color): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )
    glPointSize(1.0f)

    glBegin(GL_POINTS.toUInt)
    glVertex2f(position.x, position.y)
    glEnd()
  end pixel

  def line(
      startPosX: Int,
      startPosY: Int,
      endPosX: Int,
      endPosY: Int,
      color: Color
  ): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glBegin(GL_LINES.toUInt)
    glVertex2f(startPosX.toFloat, startPosY.toFloat)
    glVertex2f(endPosX.toFloat, endPosY.toFloat)
    glEnd()
  end line

  def line(startPos: Vector2, endPos: Vector2, color: Color): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glBegin(GL_LINES.toUInt)
    glVertex2f(startPos.x, startPos.y)
    glVertex2f(endPos.x, endPos.y)
    glEnd()
  end line

  def lineThick(
      startPos: Vector2,
      endPos: Vector2,
      thick: Float,
      color: Color
  ): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

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
  end lineThick

  def lineStrip(points: Array[Vector2], color: Color): Unit =
    if points.length < 2 then return

    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glBegin(GL_LINE_STRIP.toUInt)
    for point <- points do glVertex2f(point.x, point.y)
    glEnd()
  end lineStrip

  def lineBezier(
      startPos: Vector2,
      endPos: Vector2,
      thick: Float,
      color: Color
  ): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

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
    end for

    for i <- 0 until segments do
      lineThick(points(i), points(i + 1), thick, color)
  end lineBezier

  def circle(centerX: Int, centerY: Int, radius: Float, color: Color): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    val segments = 36

    glBegin(GL_TRIANGLE_FAN.toUInt)
    glVertex2f(centerX.toFloat, centerY.toFloat)

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radius * math.cos(angle).toFloat
      val y = centerY + radius * math.sin(angle).toFloat
      glVertex2f(x, y)
    end for

    glEnd()
  end circle

  def circle(center: Vector2, radius: Float, color: Color): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    val segments = 36

    glBegin(GL_TRIANGLE_FAN.toUInt)
    glVertex2f(center.x, center.y)

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      glVertex2f(x, y)
    end for

    glEnd()
  end circle

  def circleSector(
      center: Vector2,
      radius: Float,
      startAngle: Float,
      endAngle: Float,
      segments: Int,
      color: Color
  ): Unit =
    if segments < 3 then return

    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

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
    end for

    glEnd()
  end circleSector

  def circleSectorOutline(
      center: Vector2,
      radius: Float,
      startAngle: Float,
      endAngle: Float,
      segments: Int,
      color: Color
  ): Unit =
    if segments < 3 then return
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    val startRad = math.toRadians(startAngle).toFloat
    val endRad = math.toRadians(endAngle).toFloat

    val angleStep = (endRad - startRad) / segments.toFloat

    glBegin(GL_LINE_STRIP.toUInt)
    for i <- 0 to segments do
      val angle = startRad + (i * angleStep)
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      glVertex2f(x, y)
    end for
    glEnd()

    glBegin(GL_LINES.toUInt)
    glVertex2f(center.x, center.y)
    glVertex2f(
      center.x + radius * math.cos(startRad).toFloat,
      center.y + radius * math.sin(startRad).toFloat
    )

    glVertex2f(center.x, center.y)
    glVertex2f(
      center.x + radius * math.cos(endRad).toFloat,
      center.y + radius * math.sin(endRad).toFloat
    )
    glEnd()
  end circleSectorOutline

  def circleGradient(
      centerX: Int,
      centerY: Int,
      radius: Float,
      inner: Color,
      outer: Color
  ): Unit =
    val segments = 36

    glBegin(GL_TRIANGLE_FAN.toUInt)

    glColor4f(
      inner.r / 255.0f,
      inner.g / 255.0f,
      inner.b / 255.0f,
      inner.a / 255.0f
    )
    glVertex2f(centerX.toFloat, centerY.toFloat)

    glColor4f(
      outer.r / 255.0f,
      outer.g / 255.0f,
      outer.b / 255.0f,
      outer.a / 255.0f
    )

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radius * math.cos(angle).toFloat
      val y = centerY + radius * math.sin(angle).toFloat
      glVertex2f(x, y)
    end for

    glEnd()
  end circleGradient

  def circleOutline(
      centerX: Int,
      centerY: Int,
      radius: Float,
      color: Color
  ): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    val segments = 36

    glBegin(GL_LINE_LOOP.toUInt)

    for i <- 0 until segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radius * math.cos(angle).toFloat
      val y = centerY + radius * math.sin(angle).toFloat
      glVertex2f(x, y)
    end for

    glEnd()
  end circleOutline

  def circleOutline(center: Vector2, radius: Float, color: Color): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    val segments = 36

    glBegin(GL_LINE_LOOP.toUInt)

    for i <- 0 until segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      glVertex2f(x, y)
    end for

    glEnd()
  end circleOutline

  def ellipse(
      centerX: Int,
      centerY: Int,
      radiusH: Float,
      radiusV: Float,
      color: Color
  ): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    val segments = 36

    glBegin(GL_TRIANGLE_FAN.toUInt)
    glVertex2f(centerX.toFloat, centerY.toFloat)

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radiusH * math.cos(angle).toFloat
      val y = centerY + radiusV * math.sin(angle).toFloat
      glVertex2f(x, y)
    end for

    glEnd()
  end ellipse

  def ellipseOutlines(
      centerX: Int,
      centerY: Int,
      radiusH: Float,
      radiusV: Float,
      color: Color
  ): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    val segments = 36

    glBegin(GL_LINE_LOOP.toUInt)

    for i <- 0 to segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radiusH * math.cos(angle).toFloat
      val y = centerY + radiusV * math.sin(angle).toFloat
      glVertex2f(x, y)
    end for

    glEnd()
  end ellipseOutlines

  def rectangle(
      posX: Int,
      posY: Int,
      width: Int,
      height: Int,
      color: Color
  ): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glBegin(GL_QUADS.toUInt)
    glVertex2f(posX.toFloat, posY.toFloat)
    glVertex2f((posX + width).toFloat, posY.toFloat)
    glVertex2f((posX + width).toFloat, (posY + height).toFloat)
    glVertex2f(posX.toFloat, (posY + height).toFloat)
    glEnd()
  end rectangle

  def rectangle(pos: Vector2, size: Vector2, color: Color): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glBegin(GL_QUADS.toUInt)
    glVertex2f(pos.x, pos.y)
    glVertex2f((pos.x + size.x), pos.y)
    glVertex2f((pos.x + size.x), (pos.y + size.y))
    glVertex2f(pos.x, (pos.y + size.y))
    glEnd()
  end rectangle

  def rectangle(rectangle: Rectangle, color: Color): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glBegin(GL_QUADS.toUInt)
    glVertex2f(rectangle.x, rectangle.y)
    glVertex2f((rectangle.x + rectangle.width), rectangle.y)
    glVertex2f(
      (rectangle.x + rectangle.width),
      (rectangle.y + rectangle.height)
    )
    glVertex2f(rectangle.x, (rectangle.y + rectangle.height))
    glEnd()
  end rectangle

  def rectangleRotated(
      rectangle: Rectangle,
      origin: Vector2,
      rotation: Float,
      color: Color
  ): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glPushMatrix()

    glTranslatef(rectangle.x + origin.x, rectangle.y + origin.y, 0.0f)

    glRotatef(rotation, 0.0f, 0.0f, 1.0f)

    glBegin(GL_QUADS.toUInt)
    glVertex2f(-origin.x, -origin.y)
    glVertex2f(rectangle.width - origin.x, -origin.y)
    glVertex2f(rectangle.width - origin.x, rectangle.height - origin.y)
    glVertex2f(-origin.x, rectangle.height - origin.y)
    glEnd()

    glPopMatrix()
  end rectangleRotated

  def rectangleGradientV(
      posX: Int,
      posY: Int,
      width: Int,
      height: Int,
      top: Color,
      bottom: Color
  ): Unit =
    glBegin(GL_QUADS.toUInt)

    glColor4f(top.r / 255.0f, top.g / 255.0f, top.b / 255.0f, top.a / 255.0f)
    glVertex2f(posX.toFloat, posY.toFloat)

    glColor4f(top.r / 255.0f, top.g / 255.0f, top.b / 255.0f, top.a / 255.0f)
    glVertex2f((posX + width).toFloat, posY.toFloat)

    glColor4f(
      bottom.r / 255.0f,
      bottom.g / 255.0f,
      bottom.b / 255.0f,
      bottom.a / 255.0f
    )
    glVertex2f((posX + width).toFloat, (posY + height).toFloat)

    glColor4f(
      bottom.r / 255.0f,
      bottom.g / 255.0f,
      bottom.b / 255.0f,
      bottom.a / 255.0f
    )
    glVertex2f(posX.toFloat, (posY + height).toFloat)

    glEnd()
  end rectangleGradientV

  def rectangleGradientH(
      posX: Int,
      posY: Int,
      width: Int,
      height: Int,
      left: Color,
      right: Color
  ): Unit =
    glBegin(GL_QUADS.toUInt)

    glColor4f(
      left.r / 255.0f,
      left.g / 255.0f,
      left.b / 255.0f,
      left.a / 255.0f
    )
    glVertex2f(posX.toFloat, posY.toFloat)

    glColor4f(
      right.r / 255.0f,
      right.g / 255.0f,
      right.b / 255.0f,
      right.a / 255.0f
    )
    glVertex2f((posX + width).toFloat, posY.toFloat)

    glColor4f(
      right.r / 255.0f,
      right.g / 255.0f,
      right.b / 255.0f,
      right.a / 255.0f
    )
    glVertex2f((posX + width).toFloat, (posY + height).toFloat)

    glColor4f(
      left.r / 255.0f,
      left.g / 255.0f,
      left.b / 255.0f,
      left.a / 255.0f
    )
    glVertex2f(posX.toFloat, (posY + height).toFloat)

    glEnd()
  end rectangleGradientH

  def rectangleGradient(
      rectangle: Rectangle,
      topLeft: Color,
      bottomLeft: Color,
      topRight: Color,
      bottomRight: Color
  ): Unit =
    glBegin(GL_QUADS.toUInt)

    glColor4f(
      topLeft.r / 255.0f,
      topLeft.g / 255.0f,
      topLeft.b / 255.0f,
      topLeft.a / 255.0f
    )
    glVertex2f(rectangle.x, rectangle.y)

    glColor4f(
      topRight.r / 255.0f,
      topRight.g / 255.0f,
      topRight.b / 255.0f,
      topRight.a / 255.0f
    )
    glVertex2f((rectangle.x + rectangle.width), rectangle.y)

    glColor4f(
      bottomRight.r / 255.0f,
      bottomRight.g / 255.0f,
      bottomRight.b / 255.0f,
      bottomRight.a / 255.0f
    )
    glVertex2f(
      (rectangle.x + rectangle.width),
      (rectangle.y + rectangle.height)
    )

    glColor4f(
      bottomLeft.r / 255.0f,
      bottomLeft.g / 255.0f,
      bottomLeft.b / 255.0f,
      bottomLeft.a / 255.0f
    )
    glVertex2f(rectangle.x, (rectangle.y + rectangle.height))

    glEnd()
  end rectangleGradient

  def rectangleOutline(
      posX: Int,
      posY: Int,
      width: Int,
      height: Int,
      color: Color
  ): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glBegin(GL_LINE_LOOP.toUInt)
    glVertex2f(posX.toFloat, posY.toFloat)
    glVertex2f((posX + width).toFloat, posY.toFloat)
    glVertex2f((posX + width).toFloat, (posY + height).toFloat)
    glVertex2f(posX.toFloat, (posY + height).toFloat)
    glEnd()
  end rectangleOutline

  def rectangleOutline(pos: Vector2, size: Vector2, color: Color): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glBegin(GL_LINE_LOOP.toUInt)
    glVertex2f(pos.x, pos.y)
    glVertex2f(pos.x + size.x, pos.y)
    glVertex2f(pos.x + size.x, pos.y + size.y)
    glVertex2f(pos.x, pos.y + size.y)
    glEnd()
  end rectangleOutline

  def rectangleOutline(rectangle: Rectangle, color: Color): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glBegin(GL_LINE_LOOP.toUInt)
    glVertex2f(rectangle.x, rectangle.y)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + rectangle.height)
    glVertex2f(rectangle.x, rectangle.y + rectangle.height)
    glEnd()
  end rectangleOutline

  def rectangleOutlineThick(
      rectangle: Rectangle,
      thick: Float,
      color: Color
  ): Unit =
    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    glBegin(GL_QUADS.toUInt)

    glVertex2f(rectangle.x, rectangle.y)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + thick)
    glVertex2f(rectangle.x, rectangle.y + thick)

    glVertex2f(rectangle.x + rectangle.width - thick, rectangle.y + thick)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + thick)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + rectangle.height)
    glVertex2f(
      rectangle.x + rectangle.width - thick,
      rectangle.y + rectangle.height
    )

    glVertex2f(rectangle.x, rectangle.y + rectangle.height - thick)
    glVertex2f(
      rectangle.x + rectangle.width,
      rectangle.y + rectangle.height - thick
    )
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + rectangle.height)
    glVertex2f(rectangle.x, rectangle.y + rectangle.height)

    glVertex2f(rectangle.x, rectangle.y)
    glVertex2f(rectangle.x + thick, rectangle.y)
    glVertex2f(rectangle.x + thick, rectangle.y + rectangle.height - thick)
    glVertex2f(rectangle.x, rectangle.y + rectangle.height - thick)

    glEnd()
  end rectangleOutlineThick

  def rectangleRounded(
      rect: Rectangle,
      roundness: Float,
      segments: Int,
      color: Color
  ): Unit =
    if segments < 3 then return
    if roundness <= 0.0f then
      rectangle(rect, color)
      return

    glColor4f(
      color.r / 255.0f,
      color.g / 255.0f,
      color.b / 255.0f,
      color.a / 255.0f
    )

    val maxRadius = math.min(rect.width, rect.height) / 2.0f
    val cornerRadius = (roundness * maxRadius).min(maxRadius)

    val innerWidth = rect.width - 2 * cornerRadius
    val innerHeight = rect.height - 2 * cornerRadius

    if innerWidth > 0 && innerHeight > 0 then
      glBegin(GL_QUADS.toUInt)
      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius)
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + cornerRadius)
      glVertex2f(
        rect.x + cornerRadius + innerWidth,
        rect.y + cornerRadius + innerHeight
      )
      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius + innerHeight)
      glEnd()
    end if

    if innerWidth > 0 then
      glBegin(GL_QUADS.toUInt)
      glVertex2f(rect.x + cornerRadius, rect.y)
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y)
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + cornerRadius)
      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius)

      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius + innerHeight)
      glVertex2f(
        rect.x + cornerRadius + innerWidth,
        rect.y + cornerRadius + innerHeight
      )
      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + rect.height)
      glVertex2f(rect.x + cornerRadius, rect.y + rect.height)
      glEnd()
    end if

    if innerHeight > 0 then
      glBegin(GL_QUADS.toUInt)
      glVertex2f(rect.x, rect.y + cornerRadius)
      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius)
      glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius + innerHeight)
      glVertex2f(rect.x, rect.y + cornerRadius + innerHeight)

      glVertex2f(rect.x + cornerRadius + innerWidth, rect.y + cornerRadius)
      glVertex2f(rect.x + rect.width, rect.y + cornerRadius)
      glVertex2f(rect.x + rect.width, rect.y + cornerRadius + innerHeight)
      glVertex2f(
        rect.x + cornerRadius + innerWidth,
        rect.y + cornerRadius + innerHeight
      )
      glEnd()
    end if

    val angleStep = (math.Pi / 2.0f) / segments.toFloat

    glBegin(GL_TRIANGLE_FAN.toUInt)
    glVertex2f(rect.x + cornerRadius, rect.y + cornerRadius)
    for i <- 0 to segments do
      val angle = math.Pi + (i * angleStep)
      val x = rect.x + cornerRadius + cornerRadius * math.cos(angle).toFloat
      val y = rect.y + cornerRadius + cornerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    end for
    glEnd()

    glBegin(GL_TRIANGLE_FAN.toUInt)
    glVertex2f(rect.x + rect.width - cornerRadius, rect.y + cornerRadius)
    for i <- 0 to segments do
      val angle = (3 * math.Pi / 2.0f) + (i * angleStep)
      val x = rect.x + rect.width - cornerRadius + cornerRadius * math
        .cos(angle)
        .toFloat
      val y = rect.y + cornerRadius + cornerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    end for
    glEnd()

    glBegin(GL_TRIANGLE_FAN.toUInt)
    glVertex2f(
      rect.x + rect.width - cornerRadius,
      rect.y + rect.height - cornerRadius
    )
    for i <- 0 to segments do
      val angle = (i * angleStep)
      val x = rect.x + rect.width - cornerRadius + cornerRadius * math
        .cos(angle)
        .toFloat
      val y = rect.y + rect.height - cornerRadius + cornerRadius * math
        .sin(angle)
        .toFloat
      glVertex2f(x, y)
    end for
    glEnd()

    glBegin(GL_TRIANGLE_FAN.toUInt)
    glVertex2f(rect.x + cornerRadius, rect.y + rect.height - cornerRadius)
    for i <- 0 to segments do
      val angle = (math.Pi / 2.0f) + (i * angleStep)
      val x = rect.x + cornerRadius + cornerRadius * math.cos(angle).toFloat
      val y = rect.y + rect.height - cornerRadius + cornerRadius * math
        .sin(angle)
        .toFloat
      glVertex2f(x, y)
    end for
    glEnd()
  end rectangleRounded

  def rectangleRoundedOutline(rectangle: Rectangle, roundness: Float, segments: Int, color: Color): Unit =
    if segments < 3 then return
    if roundness <= 0.0f then
      rectangleOutline(rectangle, color)
      return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val maxRadius = math.min(rectangle.width, rectangle.height) / 2.0f
    val cornerRadius = (roundness * maxRadius).min(maxRadius)

    val angleStep = (math.Pi / 2.0f) / segments.toFloat

    glBegin(GL_LINES.toUInt)
    glVertex2f(rectangle.x + cornerRadius, rectangle.y)
    glVertex2f(rectangle.x + rectangle.width - cornerRadius, rectangle.y)
    glEnd()

    glBegin(GL_LINE_STRIP.toUInt)
    for i <- 0 to segments do
      val angle = (3 * math.Pi / 2.0f) + (i * angleStep)
      val x = rectangle.x + rectangle.width - cornerRadius + cornerRadius * math.cos(angle).toFloat
      val y = rectangle.y + cornerRadius + cornerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()

    glBegin(GL_LINES.toUInt)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + cornerRadius)
    glVertex2f(rectangle.x + rectangle.width, rectangle.y + rectangle.height - cornerRadius)
    glEnd()

    glBegin(GL_LINE_STRIP.toUInt)
    for i <- 0 to segments do
      val angle = (i * angleStep)
      val x = rectangle.x + rectangle.width - cornerRadius + cornerRadius * math.cos(angle).toFloat
      val y = rectangle.y + rectangle.height - cornerRadius + cornerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()

    glBegin(GL_LINES.toUInt)
    glVertex2f(rectangle.x + rectangle.width - cornerRadius, rectangle.y + rectangle.height)
    glVertex2f(rectangle.x + cornerRadius, rectangle.y + rectangle.height)
    glEnd()

    glBegin(GL_LINE_STRIP.toUInt)
    for i <- 0 to segments do
      val angle = (math.Pi / 2.0f) + (i * angleStep)
      val x = rectangle.x + cornerRadius + cornerRadius * math.cos(angle).toFloat
      val y = rectangle.y + rectangle.height - cornerRadius + cornerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()

    glBegin(GL_LINES.toUInt)
    glVertex2f(rectangle.x, rectangle.y + rectangle.height - cornerRadius)
    glVertex2f(rectangle.x, rectangle.y + cornerRadius)
    glEnd()

    glBegin(GL_LINE_STRIP.toUInt)
    for i <- 0 to segments do
      val angle = math.Pi + (i * angleStep)
      val x = rectangle.x + cornerRadius + cornerRadius * math.cos(angle).toFloat
      val y = rectangle.y + cornerRadius + cornerRadius * math.sin(angle).toFloat
      glVertex2f(x, y)
    glEnd()

  def rectangleRoundedOutlineThick(rectangle: Rectangle, roundness: Float, segments: Int, thick: Float, color: Color): Unit =
    if segments < 3 then return
    if roundness <= 0.0f then
      rectangleOutlineThick(rectangle, thick, color)
      return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    val maxRadius = math.min(rectangle.width, rectangle.height) / 2.0f
    val cornerRadius = (roundness * maxRadius).min(maxRadius)
    val halfThick = thick / 2.0f

    val angleStep = (math.Pi / 2.0f) / segments.toFloat

    glBegin(GL_QUADS.toUInt)
    glVertex2f(rectangle.x + cornerRadius, rectangle.y - halfThick)
    glVertex2f(rectangle.x + rectangle.width - cornerRadius, rectangle.y - halfThick)
    glVertex2f(rectangle.x + rectangle.width - cornerRadius, rectangle.y + halfThick)
    glVertex2f(rectangle.x + cornerRadius, rectangle.y + halfThick)
    glEnd()

    glBegin(GL_QUADS.toUInt)
    glVertex2f(rectangle.x + rectangle.width - halfThick, rectangle.y + cornerRadius)
    glVertex2f(rectangle.x + rectangle.width + halfThick, rectangle.y + cornerRadius)
    glVertex2f(rectangle.x + rectangle.width + halfThick, rectangle.y + rectangle.height - cornerRadius)
    glVertex2f(rectangle.x + rectangle.width - halfThick, rectangle.y + rectangle.height - cornerRadius)
    glEnd()

    glBegin(GL_QUADS.toUInt)
    glVertex2f(rectangle.x + rectangle.width - cornerRadius, rectangle.y + rectangle.height - halfThick)
    glVertex2f(rectangle.x + cornerRadius, rectangle.y + rectangle.height - halfThick)
    glVertex2f(rectangle.x + cornerRadius, rectangle.y + rectangle.height + halfThick)
    glVertex2f(rectangle.x + rectangle.width - cornerRadius, rectangle.y + rectangle.height + halfThick)
    glEnd()

    glBegin(GL_QUADS.toUInt)
    glVertex2f(rectangle.x - halfThick, rectangle.y + rectangle.height - cornerRadius)
    glVertex2f(rectangle.x + halfThick, rectangle.y + rectangle.height - cornerRadius)
    glVertex2f(rectangle.x + halfThick, rectangle.y + cornerRadius)
    glVertex2f(rectangle.x - halfThick, rectangle.y + cornerRadius)
    glEnd()

    glBegin(GL_TRIANGLE_STRIP.toUInt)
    for i <- 0 to segments do
      val angle = (3 * math.Pi / 2.0f) + (i * angleStep)
      val cosAngle = math.cos(angle).toFloat
      val sinAngle = math.sin(angle).toFloat

      val innerX = rectangle.x + rectangle.width - cornerRadius + (cornerRadius - halfThick) * cosAngle
      val innerY = rectangle.y + cornerRadius + (cornerRadius - halfThick) * sinAngle
      val outerX = rectangle.x + rectangle.width - cornerRadius + (cornerRadius + halfThick) * cosAngle
      val outerY = rectangle.y + cornerRadius + (cornerRadius + halfThick) * sinAngle

      glVertex2f(innerX, innerY)
      glVertex2f(outerX, outerY)
    glEnd()

    glBegin(GL_TRIANGLE_STRIP.toUInt)
    for i <- 0 to segments do
      val angle = (i * angleStep)
      val cosAngle = math.cos(angle).toFloat
      val sinAngle = math.sin(angle).toFloat

      val innerX = rectangle.x + rectangle.width - cornerRadius + (cornerRadius - halfThick) * cosAngle
      val innerY = rectangle.y + rectangle.height - cornerRadius + (cornerRadius - halfThick) * sinAngle
      val outerX = rectangle.x + rectangle.width - cornerRadius + (cornerRadius + halfThick) * cosAngle
      val outerY = rectangle.y + rectangle.height - cornerRadius + (cornerRadius + halfThick) * sinAngle

      glVertex2f(innerX, innerY)
      glVertex2f(outerX, outerY)
    glEnd()

    glBegin(GL_TRIANGLE_STRIP.toUInt)
    for i <- 0 to segments do
      val angle = (math.Pi / 2.0f) + (i * angleStep)
      val cosAngle = math.cos(angle).toFloat
      val sinAngle = math.sin(angle).toFloat

      val innerX = rectangle.x + cornerRadius + (cornerRadius - halfThick) * cosAngle
      val innerY = rectangle.y + rectangle.height - cornerRadius + (cornerRadius - halfThick) * sinAngle
      val outerX = rectangle.x + cornerRadius + (cornerRadius + halfThick) * cosAngle
      val outerY = rectangle.y + rectangle.height - cornerRadius + (cornerRadius + halfThick) * sinAngle

      glVertex2f(innerX, innerY)
      glVertex2f(outerX, outerY)
    glEnd()

    glBegin(GL_TRIANGLE_STRIP.toUInt)
    for i <- 0 to segments do
      val angle = math.Pi + (i * angleStep)
      val cosAngle = math.cos(angle).toFloat
      val sinAngle = math.sin(angle).toFloat

      val innerX = rectangle.x + cornerRadius + (cornerRadius - halfThick) * cosAngle
      val innerY = rectangle.y + cornerRadius + (cornerRadius - halfThick) * sinAngle
      val outerX = rectangle.x + cornerRadius + (cornerRadius + halfThick) * cosAngle
      val outerY = rectangle.y + cornerRadius + (cornerRadius + halfThick) * sinAngle

      glVertex2f(innerX, innerY)
      glVertex2f(outerX, outerY)
    glEnd()

  def triangle(v1: Vector2, v2: Vector2, v3: Vector2, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_TRIANGLES.toUInt)
    glVertex2f(v1.x, v1.y)
    glVertex2f(v2.x, v2.y)
    glVertex2f(v3.x, v3.y)
    glEnd()

  def triangleOutline(v1: Vector2, v2: Vector2, v3: Vector2, color: Color): Unit =
    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_LINE_LOOP.toUInt)
    glVertex2f(v1.x, v1.y)
    glVertex2f(v2.x, v2.y)
    glVertex2f(v3.x, v3.y)
    glEnd()

  def triangleFan(points: Array[Vector2], color: Color): Unit =
    if points.length < 3 then return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_TRIANGLE_FAN.toUInt)
    for point <- points do
      glVertex2f(point.x, point.y)
    glEnd()

  def triangleStrip(points: Array[Vector2], color: Color): Unit =
    if points.length < 3 then return

    glColor4f(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

    glBegin(GL_TRIANGLE_STRIP.toUInt)
    for point <- points do
      glVertex2f(point.x, point.y)
    glEnd()

end Basics
