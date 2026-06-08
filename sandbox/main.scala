//> using scala 3.8.3
//> using platform scala-native
//> using nativeVersion 0.5.12
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.2.0"
//> using nativeLinking -lSDL2
//> using nativeLinking -lGLEW
//> using nativeLinking -lGL

package sandbox

import s2d.core.*
import s2d.font.*
import s2d.types.*

@main
def main(): Unit =
  Window.create(800, 450, "sandbox")
  Input.setExitKey(Key.Escape)
  Timing.setTargetFPS(60)

  val font = Font.load("assets/font.ttf", 32.0f).getOrElse {
    println("failed to load font.")
    Window.close()
    return
  }

  while Window.isOpen() do
    Drawing.beginFrame()
    Drawing.clear(Color.DarkBlue)

    Text.draw("test font", 100, 100, font, Color.White)

    Drawing.endFrame()

  Font.unload(font)
  Window.close()