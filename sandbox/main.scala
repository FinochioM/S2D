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

  val small = Font.load("assets/font.ttf", 16.0f).get
  val big = Font.load("assets/font.ttf", 64.0f).get

  while Window.isOpen() do
    Drawing.beginFrame()
    Drawing.clear(Color.DarkBlue)

    Text.draw("test font", 100, 100, small, Color.White)
    Text.draw("test font", 100, 300, big, Color.White)

    val text = "centered text"
    val size = Text.measure(text, big)
    Text.draw(text, (800 - size.x) / 2, (600 - size.y) / 2, big, Color.White)

    for i <- 0 until 15 do
      Text.draw(s"line $i", 550, 10 + i * 30, small, Color.White)


    Text.draw("!@#$%^&*()_+-=[]{}|;':\",./<>?", 620, 400, small, Color.White)

    Drawing.endFrame()

  Font.unload(small)
  Font.unload(big)
  Window.close()