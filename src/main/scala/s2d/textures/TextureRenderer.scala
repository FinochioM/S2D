package s2d.textures

import s2d.types.{Color, Texture2D, Vector2, Rectangle}
import s2d.math.Matrix4
import s2d.core.{Window, Shaders, Drawing}
import s2d.backend.gl.GL.*
import s2d.backend.gl.GLExtras.*
import s2d.backend.gl.GLEWHelper
import scalanative.unsafe.*
import scalanative.unsigned.*
import scalanative.libc.stdlib

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
  private val scratchBuffer: Ptr[GLfloat] =
    stdlib.malloc(sizeof[GLfloat] * 64.toUInt).asInstanceOf[Ptr[GLfloat]]

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

        GLEWHelper.glBindVertexArray(VAO)
        GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
        GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (4 * sizeof[GLfloat].toInt).toUInt, null)
        GLEWHelper.glEnableVertexAttribArray(0.toUInt)
        GLEWHelper.glVertexAttribPointer(1.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (4 * sizeof[GLfloat].toInt).toUInt, (2 * sizeof[GLfloat].toInt).toPtr.asInstanceOf[Ptr[Byte]])
        GLEWHelper.glEnableVertexAttribArray(1.toUInt)
        GLEWHelper.glBindVertexArray(0.toUInt)

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
    setProjectionMatrix(Drawing.getProjection())

  private def setProjectionMatrix(matrix: Array[Float]): Unit =
    textureShader.foreach { shader =>
      Drawing.useProgram(shader.id.toUInt)
      var i = 0
      while i < 16 do { scratchBuffer(i) = matrix(i); i += 1 }
      GLEWHelper.glUniformMatrix4fv(projectionLocation, 1.toUInt, GL_FALSE, scratchBuffer)
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
        Drawing.useProgram(customShader.id.toUInt)
        val projLoc  = Drawing.getCustomProjLoc
        val colorLoc = Drawing.getCustomColorLoc
        val texLoc   = Drawing.getCustomTexLoc
        if projLoc >= 0 then
          GLEWHelper.glUniformMatrix4fv(projLoc, 1.toUInt, GL_FALSE, Drawing.getProjectionPtr)
        if colorLoc >= 0 then
          GLEWHelper.glUniform4f(colorLoc, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        if texLoc >= 0 then
          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(texLoc, 0)

      case None =>
        textureShader.foreach { shader =>
          Drawing.useProgram(shader.id.toUInt)
          setColor(color)

          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(textureLocation, 0)
        }

    scratchBuffer(0)  = x;         scratchBuffer(1)  = y;            scratchBuffer(2)  = 0.0f; scratchBuffer(3)  = 0.0f
    scratchBuffer(4)  = x + width; scratchBuffer(5)  = y;            scratchBuffer(6)  = 1.0f; scratchBuffer(7)  = 0.0f
    scratchBuffer(8)  = x;         scratchBuffer(9)  = y + height;   scratchBuffer(10) = 0.0f; scratchBuffer(11) = 1.0f
    scratchBuffer(12) = x + width; scratchBuffer(13) = y;            scratchBuffer(14) = 1.0f; scratchBuffer(15) = 0.0f
    scratchBuffer(16) = x + width; scratchBuffer(17) = y + height;   scratchBuffer(18) = 1.0f; scratchBuffer(19) = 1.0f
    scratchBuffer(20) = x;         scratchBuffer(21) = y + height;   scratchBuffer(22) = 0.0f; scratchBuffer(23) = 1.0f

    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (24 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)



    glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)
    glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)

  def renderTextureEx(texture: Texture2D, position: Vector2, rotation: Float, scale: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if texture.id == 0 then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        Drawing.useProgram(customShader.id.toUInt)
        val projLoc  = Drawing.getCustomProjLoc
        val colorLoc = Drawing.getCustomColorLoc
        val texLoc   = Drawing.getCustomTexLoc
        if projLoc >= 0 then
          GLEWHelper.glUniformMatrix4fv(projLoc, 1.toUInt, GL_FALSE, Drawing.getProjectionPtr)
        if colorLoc >= 0 then
          GLEWHelper.glUniform4f(colorLoc, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        if texLoc >= 0 then
          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(texLoc, 0)

      case None =>
        textureShader.foreach { shader =>
          Drawing.useProgram(shader.id.toUInt)
          setColor(color)

          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(textureLocation, 0)
        }

    val width  = texture.width.toFloat * scale
    val height = texture.height.toFloat * scale

    val angleRad = math.toRadians(rotation).toFloat
    val cos = math.cos(angleRad).toFloat
    val sin = math.sin(angleRad).toFloat

    val r0x = position.x
    val r0y = position.y
    val r1x = width * cos + position.x
    val r1y = width * sin + position.y
    val r2x = width * cos - height * sin + position.x
    val r2y = width * sin + height * cos + position.y
    val r3x = -height * sin + position.x
    val r3y =  height * cos + position.y

    scratchBuffer(0)  = r0x; scratchBuffer(1)  = r0y; scratchBuffer(2)  = 0.0f; scratchBuffer(3)  = 0.0f
    scratchBuffer(4)  = r1x; scratchBuffer(5)  = r1y; scratchBuffer(6)  = 1.0f; scratchBuffer(7)  = 0.0f
    scratchBuffer(8)  = r2x; scratchBuffer(9)  = r2y; scratchBuffer(10) = 1.0f; scratchBuffer(11) = 1.0f
    scratchBuffer(12) = r0x; scratchBuffer(13) = r0y; scratchBuffer(14) = 0.0f; scratchBuffer(15) = 0.0f
    scratchBuffer(16) = r2x; scratchBuffer(17) = r2y; scratchBuffer(18) = 1.0f; scratchBuffer(19) = 1.0f
    scratchBuffer(20) = r3x; scratchBuffer(21) = r3y; scratchBuffer(22) = 0.0f; scratchBuffer(23) = 1.0f

    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (24 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)



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
        Drawing.useProgram(customShader.id.toUInt)
        val projLoc  = Drawing.getCustomProjLoc
        val colorLoc = Drawing.getCustomColorLoc
        val texLoc   = Drawing.getCustomTexLoc
        if projLoc >= 0 then
          GLEWHelper.glUniformMatrix4fv(projLoc, 1.toUInt, GL_FALSE, Drawing.getProjectionPtr)
        if colorLoc >= 0 then
          GLEWHelper.glUniform4f(colorLoc, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        if texLoc >= 0 then
          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(texLoc, 0)

      case None =>
        textureShader.foreach { shader =>
          Drawing.useProgram(shader.id.toUInt)
          setColor(color)

          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(textureLocation, 0)
        }

    val texLeft   = source.x / texture.width.toFloat
    val texTop    = source.y / texture.height.toFloat
    val texRight  = (source.x + source.width)  / texture.width.toFloat
    val texBottom = (source.y + source.height) / texture.height.toFloat

    scratchBuffer(0)  = position.x;                scratchBuffer(1)  = position.y;                 scratchBuffer(2)  = texLeft;  scratchBuffer(3)  = texTop
    scratchBuffer(4)  = position.x + source.width; scratchBuffer(5)  = position.y;                 scratchBuffer(6)  = texRight; scratchBuffer(7)  = texTop
    scratchBuffer(8)  = position.x;                scratchBuffer(9)  = position.y + source.height; scratchBuffer(10) = texLeft;  scratchBuffer(11) = texBottom
    scratchBuffer(12) = position.x + source.width; scratchBuffer(13) = position.y;                 scratchBuffer(14) = texRight; scratchBuffer(15) = texTop
    scratchBuffer(16) = position.x + source.width; scratchBuffer(17) = position.y + source.height; scratchBuffer(18) = texRight; scratchBuffer(19) = texBottom
    scratchBuffer(20) = position.x;                scratchBuffer(21) = position.y + source.height; scratchBuffer(22) = texLeft;  scratchBuffer(23) = texBottom

    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (24 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)



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
        Drawing.useProgram(customShader.id.toUInt)
        val projLoc  = Drawing.getCustomProjLoc
        val colorLoc = Drawing.getCustomColorLoc
        val texLoc   = Drawing.getCustomTexLoc
        if projLoc >= 0 then
          GLEWHelper.glUniformMatrix4fv(projLoc, 1.toUInt, GL_FALSE, Drawing.getProjectionPtr)
        if colorLoc >= 0 then
          GLEWHelper.glUniform4f(colorLoc, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        if texLoc >= 0 then
          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(texLoc, 0)

      case None =>
        textureShader.foreach { shader =>
          Drawing.useProgram(shader.id.toUInt)
          setColor(color)

          GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
          glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
          GLEWHelper.glUniform1i(textureLocation, 0)
        }

    val texLeft   = source.x / texture.width.toFloat
    val texTop    = source.y / texture.height.toFloat
    val texRight  = (source.x + source.width)  / texture.width.toFloat
    val texBottom = (source.y + source.height) / texture.height.toFloat

    val angleRad = math.toRadians(rotation).toFloat
    val cos = math.cos(angleRad).toFloat
    val sin = math.sin(angleRad).toFloat

    val ox = dest.x + origin.x
    val oy = dest.y + origin.y

    val lx0 = -origin.x;                 val ly0 = -origin.y
    val lx1 = dest.width - origin.x;     val ly1 = -origin.y
    val lx2 = dest.width - origin.x;     val ly2 = dest.height - origin.y
    val lx3 = -origin.x;                 val ly3 = dest.height - origin.y

    val r0x = lx0 * cos - ly0 * sin + ox; val r0y = lx0 * sin + ly0 * cos + oy
    val r1x = lx1 * cos - ly1 * sin + ox; val r1y = lx1 * sin + ly1 * cos + oy
    val r2x = lx2 * cos - ly2 * sin + ox; val r2y = lx2 * sin + ly2 * cos + oy
    val r3x = lx3 * cos - ly3 * sin + ox; val r3y = lx3 * sin + ly3 * cos + oy

    scratchBuffer(0)  = r0x; scratchBuffer(1)  = r0y; scratchBuffer(2)  = texLeft;  scratchBuffer(3)  = texTop
    scratchBuffer(4)  = r1x; scratchBuffer(5)  = r1y; scratchBuffer(6)  = texRight; scratchBuffer(7)  = texTop
    scratchBuffer(8)  = r2x; scratchBuffer(9)  = r2y; scratchBuffer(10) = texRight; scratchBuffer(11) = texBottom
    scratchBuffer(12) = r0x; scratchBuffer(13) = r0y; scratchBuffer(14) = texLeft;  scratchBuffer(15) = texTop
    scratchBuffer(16) = r2x; scratchBuffer(17) = r2y; scratchBuffer(18) = texRight; scratchBuffer(19) = texBottom
    scratchBuffer(20) = r3x; scratchBuffer(21) = r3y; scratchBuffer(22) = texLeft;  scratchBuffer(23) = texBottom

    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (24 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)



    glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)
    glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)
end TextureRenderer