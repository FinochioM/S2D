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

case class Circle(pos: Vector2, radius: Float, color: Color)

@main
def main(): Unit =
  Window.create(800, 450, "Testing random")
  Input.setExitKey(Key.Escape)
  Timing.setTargetFPS(60)

  var circles = List.empty[Circle]

  while Window.isOpen() do
    Drawing.beginFrame()
    Drawing.clear(Color.White)

    if Input.isMouseButtonPressed(MouseButton.Left) then
      val pos    = Input.getMousePosition()
      val radius = Random.float(10.0f, 40.0f)
      val color  = Random.color(255)
      circles = Circle(pos, radius, color) :: circles
      println(s"Spawned circle at (${pos.x.toInt}, ${pos.y.toInt}) r=${"%.1f".format(radius)}")

    if Input.isKeyPressed(Key.R) then
      circles = List.empty
      println("Cleared")

    if Input.isKeyPressed(Key.S) then
      Random.seed(42)
      println("Seed set to 42")

    circles.foreach(c => Basics.circle(c.pos, c.radius, c.color))

    Drawing.endFrame()

  Window.close()