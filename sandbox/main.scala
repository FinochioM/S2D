//> using scala 3.3.6
//> using platform scala-native
//> using nativeVersion 0.5.8
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.1.8-SNAPSHOT"
//> using nativeLinking -lSDL2
//> using nativeLinking -lGLEW
//> using nativeLinking -lGL

package sandbox

import s2d.core.*
import s2d.shapes.*
import s2d.types.*

@main
def main(): Unit =
  val screenWidth = 800
  val screenHeight = 450

  Window.create(screenWidth, screenHeight, "Testing Timing.getDelta()")
  Input.setExitKey(Key.Escape)
  Timing.setTargetFPS(60)

  while Window.isOpen() do
    Drawing.beginFrame()
    Drawing.clear(Color.White)

    val fps = (1.0 / Timing.delta).toInt
    println(s"FPS: $fps")

    Basics.circle(Vector2(400.0f, 225.0f), 30.0f, Color.Red)

    Drawing.endFrame()
  
  Window.close()