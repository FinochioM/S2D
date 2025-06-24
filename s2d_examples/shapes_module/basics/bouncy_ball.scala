/* --------------------------------------------------------------------------------
S2D - Shapes Package Examples - Bouncy Ball.

This example was created with S2D 0.1.7. (Updated 24/06/2025)

You can run this example with any scala build tool but I'd recommend either SBT or ScalaCLI.

--------------------------------------------------------------------------------- */

package examples

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