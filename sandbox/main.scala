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

  var inputText = ""

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.fromHex("#2C3E50").getOrElse(Color.Black))

    var char = Input.getCharPressed()
    while char != 0 do
      if char >= 32 && char <= 126 then
        inputText += char.toChar
        println(s"Character pressed: ${char.toChar} (code: $char)")
      char = Input.getCharPressed()

    if Input.isKeyPressed(Key.Backspace) then
      if inputText.nonEmpty then
        inputText = inputText.dropRight(1)
        println("Backspace pressed.")

    if Input.isKeyPressed(Key.Enter) then
      println(s"Input text: $inputText")
      inputText = ""

    println(s"Current text: $inputText")
    Drawing.endFrame()
  Window.close()
end main