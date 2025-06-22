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
  var rotation = 0.0f

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.fromHex("#3498DB").getOrElse(Color.Blue))

    Drawing.beginCamera(camera2D)

    Basics.rectangleOutline(50, 50, 150, 100, Color.Red)

    Basics.rectangleOutline(Vector2(250, 50), Vector2(150, 100), Color.Green)

    val rect = Rectangle(450, 50, 150, 100)
    Basics.rectangleOutline(rect, Color.Blue)

    Drawing.endCamera()
    Drawing.endFrame()

    rotation += 1.0f
    if rotation > 360.0f then rotation = 0.0f

  Window.close()
end main