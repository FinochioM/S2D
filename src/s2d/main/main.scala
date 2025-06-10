import S2D.core.Window
import S2D.core.Drawing
import S2D.shapes.Basics
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

  val rectanglePos = Vector2(100.0, 100.0f)
  val rectanglePos2 = Vector2(300.0, 300.0f)
  val rectangleSize = Vector2(200.0f, 150.0f)

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
      Drawing.endBlend()

    Drawing.endCamera()

    Drawing.endFrame()
  Window.close()