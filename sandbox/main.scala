//> using scala 3.3.6
//> using platform scala-native
//> using nativeVersion 0.5.8
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.1.75-SNAPSHOT"

package sandbox

import s2d.core.*
import s2d.shapes.*
import s2d.textures.*
import s2d.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main
def main(): Unit =
  val screenWidth = 800
  val screenHeight = 450
  
  Window.create(screenWidth, screenHeight, "S2D Framework - Basic Shapes Drawing")
  Input.setExitKey(Key.Escape)

  while !Window.shouldCloseWindow() do    
    Drawing.beginFrame()

    Drawing.clear(Color.White)
    
    Drawing.endFrame()
  
  Window.close()