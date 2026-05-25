//> using scala 3.3.6
//> using platform scala-native
//> using nativeVersion 0.5.8
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.1.8-SNAPSHOT"
//> using nativeLinking -lSDL2
//> using nativeLinking -lGLEW
//> using nativeLinking -lGL

package sandbox

import s2d.core.*
import s2d.shapes.*
import s2d.types.*

@main
def main(): Unit =
  Window.create(800, 450, "Testing cllision")
  Input.setExitKey(Key.Escape)
  Timing.setTargetFPS(60)

  val box     = Rectangle(300.0f, 150.0f, 200.0f, 150.0f)
  val circleCenter = Vector2(150.0f, 225.0f)
  val circleRadius = 40.0f

  while Window.isOpen() do
    Drawing.beginFrame()
    Drawing.clear(Color.White)

    val mouse = Input.getMousePosition()

    val mouseRect = Rectangle(mouse.x - 20, mouse.y - 20, 40.0f, 40.0f)
    val rectHit   = Collision.rects(mouseRect, box)
    val circleHit = Collision.circleRect(circleCenter, circleRadius, box)
    val pointInC  = Collision.pointInCircle(mouse, circleCenter, circleRadius)

    val overlap   = Collision.rectOverlap(mouseRect, box)
    val boxColor = if rectHit then Color.Red else Color.DarkBlue

    Basics.rectangleOutline(box, boxColor)

    val cColor = if pointInC then Color.Orange else Color.Green

    Basics.circleOutline(circleCenter, circleRadius, cColor)

    overlap.foreach(r => Basics.rectangle(r, Color(255, 0, 0, 80)))

    Basics.rectangleOutline(mouseRect, Color.Black)

    if rectHit then  println("rect rect hit")
    if pointInC then println("point circle hit")
    if circleHit then println("circle rect hit")

    Drawing.endFrame()

  Window.close()