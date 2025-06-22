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
  Window.create(800, 600, "S2D Framework - Custom Texture Shader Test")

  val camera2D = Camera2D.default

  val grillTexture = Textures.load("assets/grill.png") match
    case Some(texture) =>
      println(s"Texture loaded successfully! Size: ${texture.width}x${texture.height}")
      texture
    case None =>
      println("Failed to load texture!")
      Window.close()
      return

  val vertexShaderSource = """
    #version 330 core

    layout (location = 0) in vec2 aPos;
    layout (location = 1) in vec2 aTexCoord;

    uniform mat4 uProjection;
    uniform vec4 uColor;

    out vec2 texCoord;
    out vec4 vertexColor;

    void main() {
        gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
        texCoord = aTexCoord;
        vertexColor = uColor;
    }
  """

  val fragmentShaderSource = """
    #version 330 core

    in vec2 texCoord;
    in vec4 vertexColor;
    out vec4 FragColor;

    uniform sampler2D uTexture;

    void main() {
        vec4 textureColor = texture(uTexture, texCoord);

        vec4 redTint = vec4(0.0, 1.0, 1.0, 1.0);
        FragColor = textureColor * vertexColor * redTint;
    }
  """

  val customShader = Shaders.loadFromMemory(vertexShaderSource, fragmentShaderSource)

  customShader match
    case Some(shader) =>
      println("Custom texture shader loaded successfully!")

      while !Window.shouldCloseWindow() do
        Drawing.beginFrame()
        Drawing.clear(Color.fromHex("#2C3E50").getOrElse(Color.Black))

        Drawing.beginCamera(camera2D)

        val sourceRect = Rectangle(
          0.0f, 0.0f, 
          grillTexture.width / 2.0f, 
          grillTexture.height / 2.0f
        )

        val destRect = Rectangle(50.0f, 50.0f, 150.0f, 100.0f)
        
        val origin = Vector2(destRect.width / 2.0f, destRect.height / 2.0f)

        Textures.drawPro(
          grillTexture, 
          sourceRect, 
          destRect, 
          origin, 
          30.0f, 
          Color.White
        )

        val destRect2 = Rectangle(350.0f, 50.0f, 150.0f, 100.0f)
        val origin2 = Vector2(destRect2.width / 2.0f, destRect2.height / 2.0f)

        Drawing.beginShader(shader)
        Textures.drawPro(
          grillTexture, 
          sourceRect, 
          destRect2, 
          origin2, 
          45.0f,
          Color.White
        )
        Drawing.endShader()

        Textures.draw(grillTexture, 50, 300, Color(255, 255, 255, 128))

        Drawing.endCamera()
        Drawing.endFrame()

      Shaders.unloadShader(shader)

    case None =>
      println("Failed to load custom texture shader!")

  Textures.unload(grillTexture)
  Window.close()
end main