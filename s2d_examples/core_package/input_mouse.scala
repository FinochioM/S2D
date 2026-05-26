/* --------------------------------------------------------------------------------
S2D - Core Package Examples - Input Mouse.

This example was created with S2D 0.1.8. (Updated 25/05/2026)

You can run this example with any scala build tool but I'd recommend ScalaCLI.

Controls:
  MOUSE        - Move the ball
  LEFT BUTTON  - Change ball color to Maroon
  MIDDLE BUTTON- Change ball color to Lime
  RIGHT BUTTON - Change ball color to DarkBlue
  X1 BUTTON    - Change ball color to Purple
  X2 BUTTON    - Change ball color to Yellow
  H            - Toggle cursor visibility
--------------------------------------------------------------------------------- */

package examples

import s2d.core.*
import s2d.shapes.*
import s2d.types.*

@main
def main(): Unit =
  val screenWidth  = 800
  val screenHeight = 450

  Window.create(screenWidth, screenHeight, "S2D [core] example - input mouse")
  Input.setExitKey(Key.Escape)
  Timing.setTargetFPS(60)

  var ballPosition = Vector2(-100.0f, -100.0f)
  var ballColor    = Color.DarkBlue

  while Window.isOpen() do

    if Input.isKeyPressed(Key.H) then
      if Cursor.isHidden then Cursor.show()
      else Cursor.hide()

    ballPosition = Input.getMousePosition()

    if      Input.isMouseButtonPressed(MouseButton.Left)   then ballColor = Color.Maroon
    else if Input.isMouseButtonPressed(MouseButton.Middle) then ballColor = Color.Lime
    else if Input.isMouseButtonPressed(MouseButton.Right)  then ballColor = Color.DarkBlue
    else if Input.isMouseButtonPressed(MouseButton.X1)     then ballColor = Color.Purple
    else if Input.isMouseButtonPressed(MouseButton.X2)     then ballColor = Color.Yellow

    Drawing.beginFrame()
    Drawing.clear(Color.White)

    Basics.circle(ballPosition, 40.0f, ballColor)

    Drawing.endFrame()

  Window.close()