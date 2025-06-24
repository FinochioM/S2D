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
  
  Window.create(screenWidth, screenHeight, "S2D Library")
  Input.setExitKey(Key.Escape)

  while !Window.shouldCloseWindow() do    
    Drawing.beginFrame()

    Drawing.clear(Color.White)

    val wheelMove = Input.getWheelMove()
    val wheelVector = Input.getWheelMoveVector()

    if wheelMove != 0.0f then
      println(s"Wheel move: ${wheelMove}")

    if wheelVector.x != 0.0f || wheelVector.y != 0.0f then
      println(s"Wheel vector: x=${wheelVector.x}, y={$wheelVector.y}")
    
    Drawing.endFrame()
  Window.close()