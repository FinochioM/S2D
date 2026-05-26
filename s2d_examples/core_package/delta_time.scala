/* --------------------------------------------------------------------------------
S2D - Core Package Examples - Delta Time.

This example was created with S2D 0.1.8. (Updated 25/05/2026)

You can run this example with any scala build tool but I'd recommend ScalaCLI.LI.

Controls:
  MOUSE WHEEL - Adjust the FPS target
  R           - Reset both circles to start
--------------------------------------------------------------------------------- */

package examples

import s2d.core.*
import s2d.shapes.*
import s2d.types.*

@main
def main(): Unit =
  val screenWidth  = 800
  val screenHeight = 450

  Window.create(screenWidth, screenHeight, "S2D [core] example - delta time")
  Input.setExitKey(Key.Escape)

  var currentFps = 60

  var deltaCircle = Vector2(0.0f, screenHeight / 3.0f)
  var frameCircle = Vector2(0.0f, screenHeight * (2.0f / 3.0f))

  val speed        = 10.0f
  val circleRadius = 32.0f

  Timing.setTargetFPS(currentFps)

  while Window.isOpen() do
    deltaCircle = Vector2(deltaCircle.x + Timing.delta.toFloat * 6.0f * speed, deltaCircle.y)
    frameCircle = Vector2(frameCircle.x + 0.1f * speed, frameCircle.y)

    if deltaCircle.x > screenWidth then deltaCircle = Vector2(0.0f, deltaCircle.y)
    if frameCircle.x > screenWidth then frameCircle = Vector2(0.0f, frameCircle.y)

    if Input.isKeyPressed(Key.R) then
      deltaCircle = Vector2(0.0f, screenHeight / 3.0f)
      frameCircle = Vector2(0.0f, screenHeight * (2.0f / 3.0f))

    Drawing.beginFrame()
    Drawing.clear(Color.White)

    val mouseWheel = Input.getWheelMove()
    if mouseWheel != 0.0f then
      currentFps += mouseWheel.toInt
      if currentFps < 0 then currentFps = 0
      Timing.setTargetFPS(currentFps)

    Basics.circle(deltaCircle, circleRadius, Color.Red)
    Basics.circle(frameCircle, circleRadius, Color.Blue)

    Drawing.endFrame()

  Window.close()