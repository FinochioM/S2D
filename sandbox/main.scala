//> using scala 3.8.3
//> using platform scala-native
//> using nativeVersion 0.5.12
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.3.3"
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

  val sound = Audio.load("assets/test.wav") match
    case Some(s) => s
    case None =>
      println("failed to load sound")
      Window.close()
      return

  while Window.isOpen() do
    Drawing.beginFrame()
    Drawing.endFrame()

    if !Audio.isPlaying(sound) then
      Audio.play(sound)

  Audio.unload(sound)
  Window.close()