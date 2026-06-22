//> using scala 3.8.3
//> using platform scala-native
//> using nativeVersion 0.5.12
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.3.4"
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

  val music = Audio.loadMusic("assets/test.mp3") match
    case Some(m) => m
    case None =>
      println("failed to load music")
      Window.close()
      return

  Audio.playMusic(music)

  while Window.isOpen() do
    Drawing.beginFrame()
    Drawing.endFrame()

  Audio.unloadMusic(music)
  Window.close()