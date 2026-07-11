//> using scala 3.8.3
//> using platform scala-native
//> using nativeVersion 0.5.12
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.4.0"
//> using nativeLinking -lSDL2
//> using nativeLinking -lGLEW
//> using nativeLinking -lGL

package sandbox

import s2d.core.*
import s2d.textures.*
import s2d.types.*

@main def main(): Unit =
  Window.create(800, 450, "sandbox")

  Hot.enable()
  println(s"hot reload status: ${Hot.isEnabled}")

  val tex = Textures.load("assets/grill.png") match
    case Some(t) => t
    case None =>
      println("Failed to load texture")
      Window.close()
      return

  println(s"Watched count: ${Hot.watchedCount}") // 1

  while Window.isOpen() do
    Drawing.beginFrame()
    Drawing.clear(Color.DarkBlue)

    Textures.draw(tex, 100, 100, Color.White)
    Drawing.endFrame()

  Window.close()