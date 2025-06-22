//> using scala 3.3.6
//> using platform scala-native
//> using nativeVersion 0.5.8
//> using scalacOptions -Wconf:msg=indented:silent
//> using repository "m2Local"
//> using dep "io.github.finochiom:s2d_native0.5_3:0.1.75-SNAPSHOT"

package sandbox

import s2d.core.{Drawing, Shaders, Window}
import s2d.gl.GLExtras.GL_FLOAT
import s2d.shapes.Basics
import s2d.textures.Textures
import s2d.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main
def main(): Unit =
  Window.create(800, 600, "S2D Framework - Custom Shader Test")

  val camera2D = Camera2D.default

  val vertexShaderSource = """
    #version 330 core

    layout (location = 0) in vec2 aPos;

    uniform mat4 uProjection;
    uniform vec4 uColor;
    uniform float uTime;

    out vec4 vertexColor;

    void main() {
        gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
        vertexColor = uColor;
    }
  """

  val fragmentShaderSource = """
    #version 330 core

    in vec4 vertexColor;
    out vec4 FragColor;

    uniform float uTime;

    void main() {
        // Create a pulsing rainbow effect
        float r = (sin(uTime * 2.0) + 1.0) * 0.5;
        float g = (sin(uTime * 2.0 + 2.094) + 1.0) * 0.5;  // 2.094 = 2*PI/3
        float b = (sin(uTime * 2.0 + 4.188) + 1.0) * 0.5;  // 4.188 = 4*PI/3

        FragColor = vec4(r, g, b, 1.0);
    }
  """

  val customShader = Shaders.loadFromMemory(vertexShaderSource, fragmentShaderSource)

  customShader match
    case Some(shader) =>
      println("Custom shader loaded successfully!")

      val timeLocation = Shaders.getShaderLocation(shader, "uTime")
      println(s"Time uniform location: $timeLocation")

      val startTime = System.currentTimeMillis()

      while !Window.shouldCloseWindow() do
        Drawing.beginFrame()
        Drawing.clear(Color.fromHex("#2C3E50").getOrElse(Color.Black))

        Drawing.beginCamera(camera2D)

        val currentTime = System.currentTimeMillis()
        val timeSeconds = (currentTime - startTime) / 1000.0f

        Drawing.beginShader(shader)

        if timeLocation >= 0 then
          Zone {
            val timeValue = stackalloc[Float]()
            !timeValue = timeSeconds
            Shaders.setShaderValue(shader, timeLocation, timeValue.asInstanceOf[Ptr[Byte]], GL_FLOAT.toInt)
          }

        //Basics.rectangle(300, 250, 200, 100, Color.White)

        //Basics.lineThick(Vector2(300, 250), Vector2(500, 350), 10.0f, Color.White)

        val points = Array(
          Vector2(300, 250),
          Vector2(400, 200),
          Vector2(500, 300),
          Vector2(450, 350),
          Vector2(350, 380)
        )
        //Basics.lineStrip(points, Color.White)

        //Basics.rectangleRotated(Rectangle(300, 250, 150, 100), Vector2(75, 50), 45.0f, Color.White)

        //Basics.rectangleOutline(300, 250, 200, 100, Color.White)

        //Basics.rectangleRounded(Rectangle(300, 250, 200, 100), 0.3f, 16, Color.White)

        //Basics.rectangleRoundedOutline(Rectangle(300, 250, 200, 100), 0.3f, 16, Color.White)

        //Basics.triangle(Vector2(400, 200), Vector2(350, 350), Vector2(450, 350), Color.White)

        //Basics.triangleOutline(Vector2(400, 200), Vector2(350, 350), Vector2(450, 350), Color.White)

        val fanPoints = Array(
          Vector2(400, 300), // center
          Vector2(450, 250),
          Vector2(480, 300),
          Vector2(450, 350),
          Vector2(350, 350),
          Vector2(320, 300),
          Vector2(350, 250)
        )
        //Basics.triangleFan(fanPoints, Color.White)

        val stripPoints = Array(
          Vector2(300, 250),
          Vector2(350, 200),
          Vector2(400, 250),
          Vector2(450, 200),
          Vector2(500, 250),
          Vector2(550, 200)
        )
        //Basics.triangleStrip(stripPoints, Color.White)

        //Basics.polygon(Vector2(400, 300), 6, 80.0f, 0.0f, Color.White)

        //Basics.polygonOutline(Vector2(400, 300), 8, 80.0f, 0.0f, Color.White)

        //Basics.circleSectorOutline(Vector2(400, 300), 80.0f, 45.0f, 135.0f, 16, Color.White)

        //Basics.circleOutline(400, 300, 80, Color.White)

        Drawing.endShader()
        Drawing.endCamera()
        Drawing.endFrame()

      Shaders.unloadShader(shader)

    case None =>
      println("Failed to load custom shader!")
      println("Falling back to normal drawing...")
  Window.close()
end main