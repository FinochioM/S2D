import S2D.core.Window
import S2D.core.Drawing
import S2D.shapes.Basics
import S2D.textures.Textures
import S2D.types.*

@main
def main(): Unit =
  Window.create(800, 600, "S2D Framework Says Hi!")

  val camera2D = Camera2D(
    offset = Vector2(0.0f, 0.0f),
    target = Vector2(0.0f, 0.0f),
    rotation = 0.0f,
    zoom = 1.0f
  )

  val textureOpt = Textures.load("assets/player_still.png")

  textureOpt match
    case Some(texture) => println(s"Texture loaded")
    case None => println("Failed to load texture")

  val rectanglePos = Vector2(100.0, 100.0f)
  val rectanglePos2 = Vector2(300.0, 300.0f)
  val rectangleSize = Vector2(200.0f, 150.0f)

  var rotation = 0.0f
  var scale = 1.0f
  var scaleDirection = 1

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.fromHex("#6495ED").getOrElse(Color.Blue))

    Drawing.beginCamera(camera2D)
      val rect = Rectangle.fromCenter(rectanglePos + rectangleSize / 2, rectangleSize)
      val roundedRect = Rectangle.fromCenter(rectanglePos2 + rectangleSize / 2, rectangleSize)

      val baseColor = Color.Red
      val animatedColor = baseColor.lerp(Color.Yellow, (math.sin(System.currentTimeMillis() / 1000.0) + 1.0).toFloat / 2.0f)

      Drawing.beginBlend(BlendMode.Alpha)
      Basics.rectangle(rect, animatedColor)
      Basics.rectangleRoundedOutlineThick(roundedRect, 0.5f, 5, 10, animatedColor)

    textureOpt match
      case Some(texture) =>
        rotation += 1.0f
        if rotation >= 360.0f then rotation = 0.0f

        scale += 0.01f * scaleDirection
        if scale >= 1.5f then scaleDirection = -1
        if scale <= 0.5f then scaleDirection = 1

        Textures.draw(texture, 50, 50, Color.White)
        Textures.draw(texture, Vector2(200.0f, 50.0f), Color.White)
        Textures.drawEx(texture, Vector2(400.0f, 50.0f), rotation, scale, Color.White)
        Textures.draw(texture, Vector2(50.0f, 200.0f), animatedColor)

        val sourceRect = Rectangle(0.0f, 0.0f, texture.width.toFloat, texture.height.toFloat)
        val destRect = Rectangle(400.0f, 200.0f, 100.0f, 100.0f)
        val origin = Vector2(50.0f, 50.0f)
        Textures.drawPro(texture, sourceRect, destRect, origin, rotation * 0.5f, Color.fromNormalized(1.0f, 1.0f, 1.0f, 0.8f))
      case None =>
        val placeHolderRect = Rectangle(50.0f, 50.0f, 100.0f, 100.0f)
        Basics.rectangle(placeHolderRect, Color.Red)

    Drawing.endBlend()

    Drawing.endCamera()

    Drawing.endFrame()

  textureOpt.foreach(Textures.unload)
  Window.close()