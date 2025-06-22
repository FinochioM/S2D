package s2d.textures

import s2d.types.{Color, Texture2D, Vector2, Rectangle}
import s2d.math.Matrix4
import s2d.core.{Window, Shaders, Drawing}
import s2d.gl.GL.*
import s2d.gl.GLExtras.*
import s2d.gl.GLEWHelper
import scalanative.unsafe.*
import scalanative.unsigned.*

object TextureRenderer:
  private val vertexShaderSource = """
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

  private val fragmentShaderSource = """
    #version 330 core

    in vec2 texCoord;
    in vec4 vertexColor;
    out vec4 FragColor;

    uniform sampler2D uTexture;

    void main() {
        FragColor = texture(uTexture, texCoord) * vertexColor;
    }
  """

  private var textureShader: Option[s2d.types.Shader] = None
  private var VAO: UInt = 0.toUInt
  private var VBO: UInt = 0.toUInt
  private var projectionLocation: Int = -1
  private var colorLocation: Int = -1
  private var textureLocation: Int = -1
  private var isInitialized: Boolean = false

  def initialize(): Boolean =
    if isInitialized then return true

    textureShader = Shaders.loadFromMemory(vertexShaderSource, fragmentShaderSource)

    textureShader match
      case Some(shader) =>
        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")
          val texName = toCString("uTexture")

          projectionLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, projName)
          colorLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, colorName)
          textureLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, texName)
        }

        val vaoArray = stackalloc[GLuint]()
        val vboArray = stackalloc[GLuint]()

        GLEWHelper.glGenVertexArrays(1.toUInt, vaoArray)
        GLEWHelper.glGenBuffers(1.toUInt, vboArray)

        VAO = !vaoArray
        VBO = !vboArray

        isInitialized = true
        true

      case None =>
        println("Failed to create texture shader for TextureRenderer")
        false
  end initialize

  def cleanup(): Unit =
    if isInitialized then
      val vaoArray = stackalloc[GLuint]()
      val vboArray = stackalloc[GLuint]()
      !vaoArray = VAO
      !vboArray = VBO

      GLEWHelper.glDeleteVertexArrays(1.toUInt, vaoArray)
      GLEWHelper.glDeleteBuffers(1.toUInt, vboArray)

      textureShader.foreach(Shaders.unloadShader)
      textureShader = None
      isInitialized = false

  def updateProjectionFromDrawing(): Unit =
    val matrix = Matrix4.ortho(0.0f, Window.windowWidth.toFloat, Window.windowHeight.toFloat, 0.0f, -1.0f, 1.0f)
    setProjectionMatrix(matrix)

  private def setProjectionMatrix(matrix: Array[Float]): Unit =
    textureShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      Zone {
        val matrixPtr = stackalloc[GLfloat](16)
        for i <- matrix.indices do
          matrixPtr(i) = matrix(i)
        GLEWHelper.glUniformMatrix4fv(projectionLocation, 1.toUInt, GL_FALSE, matrixPtr)
      }
    }

  private def setColor(color: Color): Unit =
    val r = color.r / 255.0f
    val g = color.g / 255.0f
    val b = color.b / 255.0f
    val a = color.a / 255.0f
    GLEWHelper.glUniform4f(colorLocation, r, g, b, a)

  def renderTexture(texture: Texture2D, x: Float, y: Float, width: Float, height: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if texture.id == 0 then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")
          val texName = toCString("uTexture")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)
          val textureLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, texName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.windowWidth.toFloat, Window.windowHeight.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)

          if textureLocation >= 0 then
            GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
            glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
            GLEWHelper.glUniform1i(textureLocation, 0)
        }

      case None =>
        textureShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)

          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(textureLocation, 0)
        }

    val vertices = Array(
      x, y,               0.0f, 0.0f, // top-left
      x + width, y,       1.0f, 0.0f, // top-right
      x, y + height,      0.0f, 1.0f, // bottom-left

      x + width, y,       1.0f, 0.0f, // top-right
      x + width, y + height, 1.0f, 1.0f, // bottom-right
      x, y + height,      0.0f, 1.0f  // bottom-left
    )

    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)

    Zone {
      val verticesPtr = stackalloc[GLfloat](vertices.length)
      for i <- vertices.indices do
        verticesPtr(i) = vertices(i)

      GLEWHelper.glBufferData(
        GL_ARRAY_BUFFER.toUInt,
        (vertices.length * sizeof[GLfloat].toInt),
        verticesPtr.asInstanceOf[Ptr[Byte]],
        GL_DYNAMIC_DRAW.toUInt
      )
    }

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (4 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    GLEWHelper.glVertexAttribPointer(1.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (4 * sizeof[GLfloat].toInt).toUInt, (2 * sizeof[GLfloat].toInt).toPtr.asInstanceOf[Ptr[Byte]])
    GLEWHelper.glEnableVertexAttribArray(1.toUInt)

    glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)
    glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)

  def renderTextureEx(texture: Texture2D, position: Vector2, rotation: Float, scale: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if texture.id == 0 then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")
          val texName = toCString("uTexture")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)
          val textureLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, texName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.windowWidth.toFloat, Window.windowHeight.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)

          if textureLocation >= 0 then
            GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
            glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
            GLEWHelper.glUniform1i(textureLocation, 0)
        }

      case None =>
        textureShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)

          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(textureLocation, 0)
        }

    val width = texture.width * scale
    val height = texture.height * scale

    val angleRad = math.toRadians(rotation).toFloat
    val cos = math.cos(angleRad).toFloat
    val sin = math.sin(angleRad).toFloat

    val corners = Array(
      (0.0f, 0.0f), // top-left
      (width, 0.0f), // top-right
      (width, height), // bottom-right
      (0.0f, height) // bottom-left
    )

    val transformedCorners = corners.map { case (x, y) =>
      val rotX = x * cos - y * sin + position.x
      val rotY = x * sin + y * cos + position.y
      (rotX, rotY)
    }

    val vertices = Array(
      transformedCorners(0)._1, transformedCorners(0)._2, 0.0f, 0.0f, // top-left
      transformedCorners(1)._1, transformedCorners(1)._2, 1.0f, 0.0f, // top-right
      transformedCorners(2)._1, transformedCorners(2)._2, 1.0f, 1.0f, // bottom-right

      transformedCorners(0)._1, transformedCorners(0)._2, 0.0f, 0.0f, // top-left
      transformedCorners(2)._1, transformedCorners(2)._2, 1.0f, 1.0f, // bottom-right
      transformedCorners(3)._1, transformedCorners(3)._2, 0.0f, 1.0f // bottom-left
    )

    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)

    Zone {
      val verticesPtr = stackalloc[GLfloat](vertices.length)
      for i <- vertices.indices do
        verticesPtr(i) = vertices(i)

      GLEWHelper.glBufferData(
        GL_ARRAY_BUFFER.toUInt,
        (vertices.length * sizeof[GLfloat].toInt),
        verticesPtr.asInstanceOf[Ptr[Byte]],
        GL_DYNAMIC_DRAW.toUInt
      )
    }

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (4 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    GLEWHelper.glVertexAttribPointer(1.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (4 * sizeof[GLfloat].toInt).toUInt, (2 * sizeof[GLfloat].toInt).toPtr.asInstanceOf[Ptr[Byte]])
    GLEWHelper.glEnableVertexAttribArray(1.toUInt)

    glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)
    glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)

  def renderTextureRec(texture: Texture2D, source: Rectangle, position: Vector2, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if texture.id == 0 then return
    if source.width <= 0 || source.height <= 0 then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")
          val texName = toCString("uTexture")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)
          val textureLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, texName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.windowWidth.toFloat, Window.windowHeight.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)

          if textureLocation >= 0 then
            GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
            glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
            GLEWHelper.glUniform1i(textureLocation, 0)
        }

      case None =>
        textureShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)

          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(textureLocation, 0)
        }

    val texLeft = source.x / texture.width.toFloat
    val texTop = source.y / texture.height.toFloat
    val texRight = (source.x + source.width) / texture.width.toFloat
    val texBottom = (source.y + source.height) / texture.height.toFloat

    val vertices = Array(
      position.x, position.y, texLeft, texTop, // top-left
      position.x + source.width, position.y, texRight, texTop, // top-right
      position.x, position.y + source.height, texLeft, texBottom, // bottom-left

      position.x + source.width, position.y, texRight, texTop, // top-right
      position.x + source.width, position.y + source.height, texRight, texBottom, // bottom-right
      position.x, position.y + source.height, texLeft, texBottom // bottom-left
    )

    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)

    Zone {
      val verticesPtr = stackalloc[GLfloat](vertices.length)
      for i <- vertices.indices do
        verticesPtr(i) = vertices(i)

      GLEWHelper.glBufferData(
        GL_ARRAY_BUFFER.toUInt,
        (vertices.length * sizeof[GLfloat].toInt),
        verticesPtr.asInstanceOf[Ptr[Byte]],
        GL_DYNAMIC_DRAW.toUInt
      )
    }

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (4 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    GLEWHelper.glVertexAttribPointer(1.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (4 * sizeof[GLfloat].toInt).toUInt, (2 * sizeof[GLfloat].toInt).toPtr.asInstanceOf[Ptr[Byte]])
    GLEWHelper.glEnableVertexAttribArray(1.toUInt)

    glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)
    glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)

  def renderTexturePro(texture: Texture2D, source: Rectangle, dest: Rectangle, origin: Vector2, rotation: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if texture.id == 0 then return
    if source.width <= 0 || source.height <= 0 then return
    if dest.width <= 0 || dest.height <= 0 then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")
          val texName = toCString("uTexture")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)
          val textureLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, texName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.windowWidth.toFloat, Window.windowHeight.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)

          if textureLocation >= 0 then
            GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
            glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
            GLEWHelper.glUniform1i(textureLocation, 0)
        }

      case None =>
        textureShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)

          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(textureLocation, 0)
        }

    val texLeft = source.x / texture.width.toFloat
    val texTop = source.y / texture.height.toFloat
    val texRight = (source.x + source.width) / texture.width.toFloat
    val texBottom = (source.y + source.height) / texture.height.toFloat

    val angleRad = math.toRadians(rotation).toFloat
    val cos = math.cos(angleRad).toFloat
    val sin = math.sin(angleRad).toFloat

    val corners = Array(
      (-origin.x, -origin.y), // top-left
      (dest.width - origin.x, -origin.y), // top-right
      (dest.width - origin.x, dest.height - origin.y), // bottom-right
      (-origin.x, dest.height - origin.y) // bottom-left
    )

    val transformedCorners = corners.map { case (x, y) =>
      val rotX = x * cos - y * sin + dest.x + origin.x
      val rotY = x * sin + y * cos + dest.y + origin.y
      (rotX, rotY)
    }

    val vertices = Array(
      transformedCorners(0)._1, transformedCorners(0)._2, texLeft, texTop, // top-left
      transformedCorners(1)._1, transformedCorners(1)._2, texRight, texTop, // top-right
      transformedCorners(2)._1, transformedCorners(2)._2, texRight, texBottom, // bottom-right

      transformedCorners(0)._1, transformedCorners(0)._2, texLeft, texTop, // top-left
      transformedCorners(2)._1, transformedCorners(2)._2, texRight, texBottom, // bottom-right
      transformedCorners(3)._1, transformedCorners(3)._2, texLeft, texBottom // bottom-left
    )

    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)

    Zone {
      val verticesPtr = stackalloc[GLfloat](vertices.length)
      for i <- vertices.indices do
        verticesPtr(i) = vertices(i)

      GLEWHelper.glBufferData(
        GL_ARRAY_BUFFER.toUInt,
        (vertices.length * sizeof[GLfloat].toInt),
        verticesPtr.asInstanceOf[Ptr[Byte]],
        GL_DYNAMIC_DRAW.toUInt
      )
    }

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (4 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    GLEWHelper.glVertexAttribPointer(1.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (4 * sizeof[GLfloat].toInt).toUInt, (2 * sizeof[GLfloat].toInt).toPtr.asInstanceOf[Ptr[Byte]])
    GLEWHelper.glEnableVertexAttribArray(1.toUInt)

    glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)
    glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)
end TextureRenderer
