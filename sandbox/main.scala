//> using scala 3.3.6
//> using platform scala-native
//> using nativeVersion 0.5.8
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.1.75-SNAPSHOT"

package sandbox

import s2d.core.{Window, Drawing, Shaders}
import s2d.shapes.Basics
import s2d.textures.Textures
import s2d.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main
def main(): Unit =
  Window.create(800, 600, "S2D Framework - Images Module Test")

  val camera2D = Camera2D.default

  val texture = Textures.load("assets/grill.png")

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.fromHex("#3498DB").getOrElse(Color.Blue))

    Drawing.beginCamera(camera2D)

    texture match
    case Some(tex) =>
      val sourceRect = Rectangle(0, 0, tex.width / 2, tex.height / 2)
      val destRect = Rectangle(200, 200, 150, 100)
      val origin = Vector2(75, 50)
      Textures.drawPro(tex, sourceRect, destRect, origin, 30.0f, Color.White)
    case None =>
        println("Failed to load texture.")

    Drawing.endCamera()
    Drawing.endFrame()
  Window.close()
end main