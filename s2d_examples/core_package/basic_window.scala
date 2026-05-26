/* --------------------------------------------------------------------------------
S2D - Core Package Examples - Basic Window.

This example was created with S2D 0.1.8. (Updated 25/05/2026)

You can run this example with any scala build tool but I'd recommend ScalaCLI.

--------------------------------------------------------------------------------- */

package examples

import s2d.core.*
import s2d.shapes.*
import s2d.types.*

@main
def main(): Unit =
  val screenWidth  = 800
  val screenHeight = 450

  Window.create(screenWidth, screenHeight, "S2D Framework - Basic Window")
  Input.setExitKey(Key.Escape)
  Timing.setTargetFPS(60)

  while Window.isOpen() do
    // your variables go here
    
    Drawing.beginFrame()
    Drawing.clear(Color.DarkBlue)

    // drawing goes here

    Drawing.endFrame()

  Window.close()