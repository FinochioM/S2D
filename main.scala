import S2D.core.{Window, Drawing}
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

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.fromHex("#3498DB").getOrElse(Color.Blue))

    Drawing.beginCamera(camera2D)

    Drawing.endCamera()
    Drawing.endFrame()
  end while

  Window.close()
end main
