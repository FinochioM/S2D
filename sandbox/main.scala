//> using scala 3.3.6
//> using platform scala-native
//> using nativeVersion 0.5.8
//> using scalacOptions -Wconf:msg=indented:silent
//> using dep "io.github.FinochioM::s2d::0.1.5-SNAPSHOT"

package sandbox

import s2d.core.{Window, Drawing}
import s2d.shapes.Basics
import s2d.types.*
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
    Basics.rectangleRounded(rect, 0.5, 10, Color.Red)
    Basics.rectangleRoundedOutline(rect, 0.5, 10, Color.Red)
    Basics.rectangleRoundedOutlineThick(rect, 0.5, 10, 5, Color.Red)
    Basics.triangle(Vector2(300, 200), Vector2(300, 400), Vector2(500, 400), Color.Red)
     */
    Basics.polygon(Vector2(400, 300), 6, 100, 0, Color.Red)


    Drawing.endCamera()
    Drawing.endFrame()
  end while

  Window.close()
end main
