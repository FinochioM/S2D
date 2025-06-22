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

    // small smiley face
    val smileyPixels = Array(
      (420, 180), (421, 179), (422, 178), (423, 178), (424, 178), (425, 178), (426, 178), (427, 179), (428, 180),
      (419, 181), (429, 181), (418, 182), (430, 182), (418, 183), (430, 183), (418, 184), (430, 184),
      (418, 185), (430, 185), (418, 186), (430, 186), (419, 187), (429, 187),
      (420, 188), (421, 189), (422, 190), (423, 190), (424, 190), (425, 190), (426, 190), (427, 189), (428, 188),

      (422, 182), (426, 182),

      (422, 186), (423, 187), (424, 187), (425, 187), (426, 186)
    )

    for (x, y) <- smileyPixels do
      Basics.pixel(x, y, Color.Yellow)

    Drawing.endCamera()
    Drawing.endFrame()
  Window.close()
end main