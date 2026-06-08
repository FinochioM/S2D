//> using scala 3.8.3
//> using platform scala-native
//> using nativeVersion 0.5.12
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.2.1"
//> using nativeLinking -lSDL2
//> using nativeLinking -lGLEW
//> using nativeLinking -lGL

package sandbox

import s2d.core.*
import s2d.shapes.*
import s2d.types.*

@main def main(): Unit =
  Window.create(800, 450, "sandbox")
  Input.setExitKey(Key.Escape)
  Timing.setTargetFPS(60)

  val vs = """
    #version 330 core
    layout (location = 0) in vec2 aPos;
    layout (location = 1) in vec4 aColor;
    uniform mat4 uProjection;
    out vec4 vertexColor;
    void main() {
      gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
      vertexColor = aColor;
    }
  """

  val fs = """
    #version 330 core
    in vec4 vertexColor;
    out vec4 FragColor;
    uniform float uTime;
    void main() {
      float r = (sin(uTime) + 1.0) * 0.5;
      float g = (sin(uTime + 2.094) + 1.0) * 0.5;
      float b = (sin(uTime + 4.189) + 1.0) * 0.5;
      FragColor = vec4(r, g, b, 1.0);
    }
  """

  val shader = Shaders.loadFromMemory(vs, fs).getOrElse:
    println("Failed to load shader")
    Window.close()
    return

  val timeLoc = Shaders.getShaderLocation(shader, "uTime")
  var elapsed = 0.0f

  while Window.isOpen() do
    elapsed += Timing.delta.toFloat

    Shaders.setShaderValue(shader, timeLoc, elapsed)

    Drawing.beginFrame()
    Drawing.clear(Color.Black)

    Drawing.beginShader(shader)
    Basics.rectangle(200, 125, 400, 200, Color.White)
    Drawing.endShader()

    Drawing.endFrame()

  Shaders.unloadShader(shader)
  Window.close()