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
import s2d.shapes.*
import s2d.types.*
import s2d.audio.*

@main def main(): Unit =
  Window.create(800, 450, "sandbox")

  Hot.enable()
  println(s"hot reload status: ${Hot.isEnabled()}")

  while Window.isOpen() do
    Drawing.beginFrame()
    Drawing.clear(Color.DarkBlue)
    Drawing.endFrame()

  Window.close()