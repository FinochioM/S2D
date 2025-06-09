package S2D.shapes

import S2D.types.{Rectangle, Texture2D}

object Setup:
  var shapesTexture: Texture2D = Texture2D(0, 0, 0, 0, 0) // EMPTY TEXTURE
  var shapesTextureSource: Rectangle = Rectangle(0.0f, 0.0f, 0.0f, 0.0f)
  var shapesTextureEnabled: Boolean = false

  // SETUP FUNCTIONS
  def SetShapesTexture(texture: Texture2D, source: Rectangle): Unit =
    shapesTexture = texture
    shapesTextureSource = source
    shapesTextureEnabled = texture.id != 0
  def GetShapesTexture(): Texture2D =
    shapesTexture
  def GetShapesTextureRectangle(): Rectangle =
    shapesTextureSource