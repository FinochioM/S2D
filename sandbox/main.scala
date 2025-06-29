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

  Window.create(screenWidth, screenHeight, "S2D Framework - Bouncy Ball Example")
  Input.setExitKey(Key.Escape)

  var ballPosition = Vector2(Window.width / 2.0f, Window.height / 2.0f)
  var ballSpeed = Vector2(3.0f, 2.0f)
  val ballRadius = 20.0f
  var pause = false

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    
    if Input.isKeyPressed(Key.Space) then
      pause = !pause

    if !pause then
      ballPosition = ballPosition + ballSpeed

      if ballPosition.x >= (Window.width - ballRadius) || ballPosition.x <= ballRadius then
        ballSpeed = Vector2(-ballSpeed.x, ballSpeed.y)
      if ballPosition.y >= (Window.height - ballRadius) || ballPosition.y <= ballRadius then
        ballSpeed = Vector2(ballSpeed.x, -ballSpeed.y)
    
    Drawing.clear(Color.fromHex("#2C3E50").getOrElse(Color.Black))

    Basics.circle(ballPosition, ballRadius, Color.fromHex("#8000000").getOrElse(Color.Red))

    Drawing.endFrame()
  Window.close()
end main