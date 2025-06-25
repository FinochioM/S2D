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
  Window.create(800, 600, "Mouse Cursor Test")

  var currentCursor = 0
  val cursors = Array(
    CursorStyle.Arrow,
    CursorStyle.IBeam, 
    CursorStyle.Wait,
    CursorStyle.Crosshair,
    CursorStyle.Hand,
    CursorStyle.SizeAll,
    CursorStyle.No
  )

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.Black)

    if Input.isKeyPressed(Key.Space) then
      currentCursor = (currentCursor + 1) % cursors.length
      Input.setMouseCursor(cursors(currentCursor))
      println(s"Cursor changed to: ${cursors(currentCursor)}")

    Drawing.endFrame()
  
  Window.close()
end main