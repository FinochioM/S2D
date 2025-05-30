package S2D

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