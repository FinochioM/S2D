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