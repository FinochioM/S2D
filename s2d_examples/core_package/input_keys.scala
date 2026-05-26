/* --------------------------------------------------------------------------------
S2D - Core Package Examples - Input Keys.

This example was created with S2D 0.1.8. (Updated 25/05/2026)

You can run this example with any scala build tool but I'd recommend ScalaCLI.

Controls:
  ARROW KEYS - Move the ball
--------------------------------------------------------------------------------- */

package examples

import s2d.core.*
import s2d.shapes.*
import s2d.types.*

@main
def main(): Unit =
  val screenWidth  = 800
  val screenHeight = 450

  Window.create(screenWidth, screenHeight, "S2D [core] example - input keys")
  Input.setExitKey(Key.Escape)
  Timing.setTargetFPS(60)

  var ballPosition = Vector2(screenWidth / 2.0f, screenHeight / 2.0f)

  while Window.isOpen() do
    if Input.isKeyDown(Key.Right) then ballPosition = Vector2(ballPosition.x + 2.0f, ballPosition.y)
    if Input.isKeyDown(Key.Left)  then ballPosition = Vector2(ballPosition.x - 2.0f, ballPosition.y)
    if Input.isKeyDown(Key.Up)    then ballPosition = Vector2(ballPosition.x, ballPosition.y - 2.0f)
    if Input.isKeyDown(Key.Down)  then ballPosition = Vector2(ballPosition.x, ballPosition.y + 2.0f)

    Drawing.beginFrame()
    Drawing.clear(Color.White)

    Basics.circle(ballPosition, 50.0f, Color.Maroon)

    Drawing.endFrame()

  Window.close()