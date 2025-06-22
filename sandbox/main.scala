//> using scala 3.3.6
//> using platform scala-native
//> using nativeVersion 0.5.8
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.1.75-SNAPSHOT"

package sandbox

import s2d.core.{Window, Drawing, Shaders}
import s2d.shapes.Basics
import s2d.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main
def main(): Unit =
  Window.create(800, 600, "S2D Framework - Images Module Test")

  val camera2D = Camera2D.default

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.fromHex("#3498DB").getOrElse(Color.Blue))

    Drawing.beginCamera(camera2D)

    Basics.circleOutline(Vector2(100, 320), 10.0f, Color.Red)
    Basics.circleOutline(Vector2(150, 320), 20.0f, Color.Green)
    Basics.circleOutline(Vector2(220, 320), 35.0f, Color.Blue)
    Basics.circleOutline(Vector2(310, 320), 50.0f, Color.Yellow)

    Drawing.endCamera()
    Drawing.endFrame()
  Window.close()
end main