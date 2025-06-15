import S2D.core.{Window, Drawing}
import S2D.shapes.Basics
import S2D.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main
def main(): Unit =
  Window.create(800, 600, "S2D Framework - Images Module Test")

  val camera2D = Camera2D(
    offset = Vector2(0.0f, 0.0f),
    target = Vector2(0.0f, 0.0f),
    rotation = 0.0f,
    zoom = 1.0f
  )

  val rect = Rectangle(400, 300, 100, 200)

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.fromHex("#3498DB").getOrElse(Color.Blue))

    Drawing.beginCamera(camera2D)
    /*
    Old Tests.
    Basics.line(400, 300, 200, 500, Color.Red)
    Basics.lineThick(Vector2(200, 500), Vector2(400, 300), 10.0f, Color.Green)
    Basics.circle(Vector2(400, 300), 100.0f, Color.Red)
    Basics.circleGradient(600, 400, 100.0f, Color.Red, Color.Green)
    Basics.circleOutline(
      200,
      200,
      100.0f,
      Color.fromHex("#FF5733").getOrElse(Color.Red)
    )
    Basics.ellipse(400, 300, 200, 100, Color.Red)
    Basics.ellipseOutlines(400, 300, 200, 100, Color.Red)
    Basics.rectangle(rect, Color.Red)
     */

    Basics.rectangleRounded(rect, 0.5, 10, Color.Red)

    Drawing.endCamera()
    Drawing.endFrame()
  end while

  Window.close()
end main
