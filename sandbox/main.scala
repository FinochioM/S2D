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
  Window.create(800, 600, "S2D Framework - Custom Texture Shader Test")

  Input.setExitKey(Key.Escape)

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.fromHex("#2C3E50").getOrElse(Color.Black))

    if Input.isMouseButtonPressed(MouseButton.Left) then
      println("Left mouse button pressed")

    Drawing.endFrame()
  Window.close()
end main