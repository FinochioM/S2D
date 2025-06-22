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

    val pentagon = Array(
      Vector2(300, 100)
    ) ++ (0 until 5).map { i =>
      val angle = i * 2 * math.Pi / 5 - math.Pi / 2
      val radius = 40.0f
      Vector2(
        300 + radius * math.cos(angle).toFloat,
        100 + radius * math.sin(angle).toFloat
      )
    } :+ Vector2(300 + 40 * math.cos(-math.Pi / 2).toFloat, 100 + 40 * math.sin(-math.Pi / 2).toFloat)
    Basics.triangleFan(pentagon, Color.Green)

    Drawing.endCamera()
    Drawing.endFrame()
  Window.close()
end main