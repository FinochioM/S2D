/* --------------------------------------------------------------------------------
S2D - Core Package Examples - Input Mouse Wheel.

This example was created with S2D 0.1.8. (Updated 25/05/2026)

You can run this example with any scala build tool but I'd recommend ScalaCLI.

Controls:
  MOUSE WHEEL - Move the box up and down
--------------------------------------------------------------------------------- */

package examples

import s2d.core.*
import s2d.shapes.*
import s2d.types.*

@main
def main(): Unit =
  val screenWidth  = 800
  val screenHeight = 450

  Window.create(screenWidth, screenHeight, "S2D [core] example - input mouse wheel")
  Input.setExitKey(Key.Escape)
  Timing.setTargetFPS(60)

  var boxPositionY = screenHeight / 2 - 40
  val scrollSpeed  = 4

  while Window.isOpen() do
    Drawing.beginFrame()
    Drawing.clear(Color.White)
    
    boxPositionY -= (Input.getWheelMove() * scrollSpeed).toInt

    Basics.rectangle(screenWidth / 2 - 40, boxPositionY, 80, 80, Color.Maroon)

    Drawing.endFrame()

  Window.close()