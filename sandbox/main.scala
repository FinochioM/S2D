//> using scala 3.3.6
//> using platform scala-native
//> using nativeVersion 0.5.8
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.1.71-SNAPSHOT"

package sandbox

import s2d.core.{Window, Drawing, Shaders}
import s2d.shapes.Basics
import s2d.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main
def main(): Unit =
  Window.create(800, 600, "S2D Framework - Images Module Test")

  val camera2D = Camera2D.default

  val vertexShader =
    """
    #version 120
    attribute vec3 aPos;

    void main() {
        gl_Position = gl_ModelViewProjectionMatrix * vec4(aPos, 1.0);
    }
  """

  val fragmentShader =
    """
    #version 120

    void main() {
        gl_FragColor = vec4(0.0, 1.0, 1.0, 1.0);
    }
  """

  val shader = Shaders.loadFromMemory(vertexShader, fragmentShader)
  val rect = Rectangle(350, 250, 100, 100)

  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.fromHex("#3498DB").getOrElse(Color.Blue))

    Drawing.beginCamera(camera2D)

    shader match
      case Some(s) =>
        //println("Using custom shader")
        Drawing.beginShader(s)
        Basics.rectangle(rect, Color.White)
        Drawing.endShader()
      case None =>
        //println("Using default shader")
        Basics.rectangle(rect, Color.Blue)

    Drawing.endCamera()
    Drawing.endFrame()
  end while

  shader.foreach(s => Shaders.unloadShader(s))

  Window.close()
end main