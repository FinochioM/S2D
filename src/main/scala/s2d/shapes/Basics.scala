package s2d.shapes

import s2d.shapes.BasicRenderer
import s2d.types.{Color, Rectangle, Vector2}
import s2d.gl.GL.*
import s2d.gl.GLExtras.*

object Basics:
  def pixel(posX: Int, posY: Int, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderPixel(posX.toFloat, posY.toFloat, color)
  end pixel

  def pixel(position: Vector2, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderPixel(position.x, position.y, color)
  end pixel

  def line(startPosX: Int, startPosY: Int, endPosX: Int, endPosY: Int, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderLine(
      Vector2(startPosX.toFloat, startPosY.toFloat),
      Vector2(endPosX.toFloat, endPosY.toFloat),
      color
    )
  end line

  def line(startPos: Vector2, endPos: Vector2, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderLine(startPos, endPos, color)
  end line

  def lineThick(startPos: Vector2, endPos: Vector2, thick: Float, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderThickLine(startPos, endPos, thick, color)
  end lineThick

  def lineStrip(points: Array[Vector2], color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderLineStrip(points, color)
  end lineStrip

  def lineBezier(startPos: Vector2, endPos: Vector2, thick: Float, color: Color): Unit =
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
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderCircle(centerX.toFloat, centerY.toFloat, radius, color)
  end circle

  def circle(center: Vector2, radius: Float, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderCircle(center.x, center.y, radius, color)
  end circle

  def circleSector(center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderCircleSector(center, radius, startAngle, endAngle, segments, color)
  end circleSector

  def circleSectorOutline(center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderCircleSectorOutline(center, radius, startAngle, endAngle, segments, color)
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

  def rectangle(posX: Int, posY: Int, width: Int, height: Int, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderRectangle(posX.toFloat, posY.toFloat, width.toFloat, height.toFloat, color)
  end rectangle

  def rectangle(pos: Vector2, size: Vector2, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderRectangle(pos.x, pos.y, size.x, size.y, color)
  end rectangle

  def rectangle(rectangle: Rectangle, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderRectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height, color)
  end rectangle

  def rectangleRotated(rectangle: Rectangle, origin: Vector2, rotation: Float, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderRotatedRectangle(rectangle, origin, rotation, color)
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

  def rectangleOutline(posX: Int, posY: Int, width: Int, height: Int, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderRectangleOutline(posX.toFloat, posY.toFloat, width.toFloat, height.toFloat, color)
  end rectangleOutline

  def rectangleOutline(pos: Vector2, size: Vector2, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderRectangleOutline(pos.x, pos.y, size.x, size.y, color)
  end rectangleOutline

  def rectangleOutline(rectangle: Rectangle, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderRectangleOutline(rectangle.x, rectangle.y, rectangle.width, rectangle.height, color)
  end rectangleOutline

  def rectangleOutlineThick(rectangle: Rectangle, thick: Float, color: Color): Unit =
    lineThick(Vector2(rectangle.x, rectangle.y + thick / 2), Vector2(rectangle.x + rectangle.width, rectangle.y + thick / 2), thick, color)
    lineThick(Vector2(rectangle.x + rectangle.width - thick / 2, rectangle.y), Vector2(rectangle.x + rectangle.width - thick / 2, rectangle.y + rectangle.height), thick, color)
    lineThick(Vector2(rectangle.x, rectangle.y + rectangle.height - thick / 2), Vector2(rectangle.x + rectangle.width, rectangle.y + rectangle.height - thick / 2), thick, color)
    lineThick(Vector2(rectangle.x + thick / 2, rectangle.y), Vector2(rectangle.x + thick / 2, rectangle.y + rectangle.height), thick, color)
  end rectangleOutlineThick

  def rectangleRounded(rect: Rectangle, roundness: Float, segments: Int, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderRoundedRectangle(rect, roundness, segments, color)
  end rectangleRounded

  def rectangleRoundedOutline(rectangle: Rectangle, roundness: Float, segments: Int, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderRoundedRectangleOutline(rectangle, roundness, segments, color)
  end rectangleRoundedOutline

  def rectangleRoundedOutlineThick(rectangle: Rectangle, roundness: Float, segments: Int, thick: Float, color: Color): Unit =
    if segments < 3 then return
    if roundness <= 0.0f then
      rectangleOutlineThick(rectangle, thick, color)
      return

    val maxRadius = math.min(rectangle.width, rectangle.height) / 2.0f
    val cornerRadius = (roundness * maxRadius).min(maxRadius)
    val angleStep = (math.Pi / 2.0f) / segments.toFloat

    lineThick(
      Vector2(rectangle.x + cornerRadius, rectangle.y),
      Vector2(rectangle.x + rectangle.width - cornerRadius, rectangle.y),
      thick,
      color
    )

    for i <- 0 until segments do
      val angle1 = (3 * math.Pi / 2.0f) + (i * angleStep)
      val angle2 = (3 * math.Pi / 2.0f) + ((i + 1) * angleStep)

      val x1 = rectangle.x + rectangle.width - cornerRadius + cornerRadius * math.cos(angle1).toFloat
      val y1 = rectangle.y + cornerRadius + cornerRadius * math.sin(angle1).toFloat
      val x2 = rectangle.x + rectangle.width - cornerRadius + cornerRadius * math.cos(angle2).toFloat
      val y2 = rectangle.y + cornerRadius + cornerRadius * math.sin(angle2).toFloat

      lineThick(Vector2(x1, y1), Vector2(x2, y2), thick, color)

    lineThick(
      Vector2(rectangle.x + rectangle.width, rectangle.y + cornerRadius),
      Vector2(rectangle.x + rectangle.width, rectangle.y + rectangle.height - cornerRadius),
      thick,
      color
    )

    for i <- 0 until segments do
      val angle1 = (i * angleStep)
      val angle2 = ((i + 1) * angleStep)

      val x1 = rectangle.x + rectangle.width - cornerRadius + cornerRadius * math.cos(angle1).toFloat
      val y1 = rectangle.y + rectangle.height - cornerRadius + cornerRadius * math.sin(angle1).toFloat
      val x2 = rectangle.x + rectangle.width - cornerRadius + cornerRadius * math.cos(angle2).toFloat
      val y2 = rectangle.y + rectangle.height - cornerRadius + cornerRadius * math.sin(angle2).toFloat

      lineThick(Vector2(x1, y1), Vector2(x2, y2), thick, color)

    lineThick(
      Vector2(rectangle.x + rectangle.width - cornerRadius, rectangle.y + rectangle.height),
      Vector2(rectangle.x + cornerRadius, rectangle.y + rectangle.height),
      thick,
      color
    )

    for i <- 0 until segments do
      val angle1 = (math.Pi / 2.0f) + (i * angleStep)
      val angle2 = (math.Pi / 2.0f) + ((i + 1) * angleStep)

      val x1 = rectangle.x + cornerRadius + cornerRadius * math.cos(angle1).toFloat
      val y1 = rectangle.y + rectangle.height - cornerRadius + cornerRadius * math.sin(angle1).toFloat
      val x2 = rectangle.x + cornerRadius + cornerRadius * math.cos(angle2).toFloat
      val y2 = rectangle.y + rectangle.height - cornerRadius + cornerRadius * math.sin(angle2).toFloat

      lineThick(Vector2(x1, y1), Vector2(x2, y2), thick, color)

    lineThick(
      Vector2(rectangle.x, rectangle.y + rectangle.height - cornerRadius),
      Vector2(rectangle.x, rectangle.y + cornerRadius),
      thick,
      color
    )

    for i <- 0 until segments do
      val angle1 = math.Pi.toFloat + (i * angleStep)
      val angle2 = math.Pi.toFloat + ((i + 1) * angleStep)

      val x1 = rectangle.x + cornerRadius + cornerRadius * math.cos(angle1).toFloat
      val y1 = rectangle.y + cornerRadius + cornerRadius * math.sin(angle1).toFloat
      val x2 = rectangle.x + cornerRadius + cornerRadius * math.cos(angle2).toFloat
      val y2 = rectangle.y + cornerRadius + cornerRadius * math.sin(angle2).toFloat

      lineThick(Vector2(x1, y1), Vector2(x2, y2), thick, color)

  end rectangleRoundedOutlineThick

  def triangle(v1: Vector2, v2: Vector2, v3: Vector2, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderTriangle(v1, v2, v3, color)
  end triangle

  def triangleOutline(v1: Vector2, v2: Vector2, v3: Vector2, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderTriangleOutline(v1, v2, v3, color)
  end triangleOutline

  def triangleFan(points: Array[Vector2], color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderTriangleFan(points, color)
  end triangleFan

  def triangleStrip(points: Array[Vector2], color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderTriangleStrip(points, color)
  end triangleStrip

  def polygon(center: Vector2, sides: Int, radius: Float, rotation: Float, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderPolygon(center, sides, radius, rotation, color)
  end polygon

  def polygonOutline(center: Vector2, sides: Int, radius: Float, rotation: Float, color: Color): Unit =
    BasicRenderer.updateProjectionFromDrawing()
    BasicRenderer.renderPolygonOutline(center, sides, radius, rotation, color)
  end polygonOutline

  def polygonOutlineThick(center: Vector2, sides: Int, radius: Float, rotation: Float, thick: Float, color: Color): Unit =
    if sides < 3 then return

    val rotationRad = math.toRadians(rotation).toFloat
    val angleStep = (2.0f * math.Pi / sides.toFloat).toFloat

    for i <- 0 until sides do
      val currentAngle = (i * angleStep) + rotationRad
      val nextAngle = ((i + 1) * angleStep) + rotationRad

      val x1 = center.x + radius * math.cos(currentAngle).toFloat
      val y1 = center.y + radius * math.sin(currentAngle).toFloat
      val x2 = center.x + radius * math.cos(nextAngle).toFloat
      val y2 = center.y + radius * math.sin(nextAngle).toFloat

      lineThick(Vector2(x1, y1), Vector2(x2, y2), thick, color)
  end polygonOutlineThick

end Basics
