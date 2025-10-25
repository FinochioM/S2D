//> using scala 3.3.6
//> using platform scala-native
//> using nativeVersion 0.5.8
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.1.8-SNAPSHOT"

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

  var position = Vector2(100.0f, 225.0f)
  val speed = 200.0f

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.White)
    
    val delta = Timing.getDelta().toFloat
    
    position = Vector2(position.x + speed * delta, position.y)
    
    if position.x > screenWidth then
      position = Vector2(0.0f, position.y)
    
    Basics.circle(position, 20.0f, Color.Red)
    
    if (Timing.getTime() % 1.0 < delta) then
      println(s"Delta: ${(delta * 1000).formatted("%.2f")} ms (${(1.0 / delta).formatted("%.1f")} FPS)")
    
    Drawing.endFrame()
  
  Window.close()