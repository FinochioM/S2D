import S2D.core.Window
import S2D.core.Drawing
import S2D.shapes.Basics
import S2D.types.*

@main
def main(): Unit =
  Window.create(800, 600, "S2D Framework - Images Module Test")

  val camera2D = Camera2D(
    offset = Vector2(0.0f, 0.0f),
    target = Vector2(0.0f, 0.0f),
    rotation = 0.0f,
    zoom = 1.0f
  )

    while !Window.shouldCloseWindow() do
      Drawing.beginFrame()
      Drawing.clear(Color.fromHex("#3498DB").getOrElse(Color.Blue))

      Drawing.beginCamera(camera2D)
        val rect = Rectangle(200.0f, 200.0f, 200.0f, 100.0f)
        Basics.rectangle(rect, Color.Red)
        Basics.rectangleOutlineThick(Rectangle(180.0f, 180.0f, 240.0f, 140.0f), 20, Color.White)
      Drawing.endCamera()

      Drawing.endFrame()

  Window.close()