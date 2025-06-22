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

    val ribbonStrip = Array(
      Vector2(350, 80),
      Vector2(350, 120),
      Vector2(400, 75),
      Vector2(400, 125),
      Vector2(450, 70),
      Vector2(450, 130),
      Vector2(500, 75),
      Vector2(500, 125),
      Vector2(550, 80),
      Vector2(550, 120)
    )
    Basics.triangleStrip(ribbonStrip, Color.Blue)

    Drawing.endCamera()
    Drawing.endFrame()
  Window.close()
end main