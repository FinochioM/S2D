package s2d.font

import s2d.types.{FontType, FontData, Color, Vector2}
import s2d.core.{Shaders, Drawing}
import s2d.backend.gl.GL.*
import s2d.backend.gl.GLExtras.*
import s2d.backend.gl.GLEWHelper
import s2d.backend.stb.{stbtt, stbtt_bakedchar, stbtt_aligned_quad}
import scalanative.unsafe.*
import scalanative.unsigned.*
import scalanative.libc.stdlib

private[font] object FontRenderer:
  private val vertexShaderSource = """
    #version 330 core
    layout (location = 0) in vec2 aPos;
    layout (location = 1) in vec2 aTexCoord;
    uniform mat4 uProjection;
    out vec2 texCoord;
    void main() {
        gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
        texCoord = aTexCoord;
    }
  """

  private val fragmentShaderSource = """
    #version 330 core
    in vec2 texCoord;
    out vec4 FragColor;
    uniform sampler2D uTexture;
    uniform vec4 uColor;
    void main() {
        float alpha = texture(uTexture, texCoord).r;
        FragColor = vec4(uColor.rgb, uColor.a * alpha);
    }
  """

  private var fontShader: Option[s2d.types.Shader] = None
  private var VAO: UInt = 0.toUInt
  private var VBO: UInt = 0.toUInt
  private var projectionLocation: Int = -1
  private var colorLocation: Int = -1
  private var textureLocation: Int = -1
  private var isInitialized: Boolean = false

  // 6 verts per glyph, 4 floats per vert (x, y, u, v), max 1024 glyphs per draw
  private val MAX_GLYPHS = 1024
  private val FLOATS_PER_VERT = 4
  private val VERTS_PER_GLYPH = 6
  private val scratchBuffer: Ptr[GLfloat] =
    stdlib.malloc(sizeof[GLfloat] * (MAX_GLYPHS * VERTS_PER_GLYPH * FLOATS_PER_VERT).toUInt).asInstanceOf[Ptr[GLfloat]]

  def initialize(): Boolean =
    if isInitialized then return true
    fontShader = Shaders.loadFromMemory(vertexShaderSource, fragmentShaderSource)

    fontShader match
      case Some(shader) =>
        val projName = stackalloc[CChar](16)
        val colorName = stackalloc[CChar](8)
        val texName  = stackalloc[CChar](10)
        copyStringToBuffer("uProjection", projName)
        copyStringToBuffer("uColor", colorName)
        copyStringToBuffer("uTexture", texName)

        projectionLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, projName)
        colorLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, colorName)
        textureLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, texName)

        val vaoArr = stackalloc[GLuint]()
        val vboArr = stackalloc[GLuint]()
        GLEWHelper.glGenVertexArrays(1.toUInt, vaoArr)
        GLEWHelper.glGenBuffers(1.toUInt, vboArr)
        VAO = !vaoArr
        VBO = !vboArr

        GLEWHelper.glBindVertexArray(VAO)
        GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
        val stride = (FLOATS_PER_VERT * sizeof[GLfloat].toInt).toUInt
        GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, stride, null)
        GLEWHelper.glEnableVertexAttribArray(0.toUInt)
        GLEWHelper.glVertexAttribPointer(1.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, stride, (2 * sizeof[GLfloat].toInt).toPtr.asInstanceOf[Ptr[Byte]])
        GLEWHelper.glEnableVertexAttribArray(1.toUInt)
        GLEWHelper.glBindVertexArray(0.toUInt)

        isInitialized = true
        true

      case None =>
        println("Failed to create font shader for FontRenderer")
        false

  def cleanup(): Unit =
    if isInitialized then
      val vaoArr = stackalloc[GLuint]()
      val vboArr = stackalloc[GLuint]()
      !vaoArr = VAO; !vboArr = VBO
      GLEWHelper.glDeleteVertexArrays(1.toUInt, vaoArr)
      GLEWHelper.glDeleteBuffers(1.toUInt, vboArr)
      fontShader.foreach(Shaders.unloadShader)
      fontShader = None
      isInitialized = false

  def drawText(text: String, x: Float, y: Float, font: FontType, color: Color): Unit =
    if !isInitialized then if !initialize() then return

    val fd = FontType.data(font)
    var cursorX = x
    val cursorY = y
    val xpos = stackalloc[CFloat](1)
    val ypos = stackalloc[CFloat](1)
    val quad = stackalloc[stbtt_aligned_quad](1)
    var vertCount = 0

    fontShader.foreach { shader =>
      Drawing.useProgram(shader.id.toUInt)
      GLEWHelper.glUniformMatrix4fv(projectionLocation, 1.toUInt, GL_FALSE, Drawing.getProjectionPtr)
      GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
      GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
      glBindTexture(GL_TEXTURE_2D.toUInt, fd.textureId.toUInt)
      GLEWHelper.glUniform1i(textureLocation, 0)
    }

    val charData = stdlib.malloc((fd.numChars.toLong * sizeof[stbtt_bakedchar].toLong).toUSize).asInstanceOf[Ptr[stbtt_bakedchar]]
    var ci = 0
    while ci < fd.numChars do
        val c = charData + ci
        val base = ci * 7
        (!c).x0 = fd.charData(base + 0).toShort
        (!c).y0 = fd.charData(base + 1).toShort
        (!c).x1 = fd.charData(base + 2).toShort
        (!c).y1 = fd.charData(base + 3).toShort
        (!c).xoff = fd.charData(base + 4)
        (!c).yoff = fd.charData(base + 5)
        (!c).xadvance = fd.charData(base + 6)
        ci += 1

    val bytes = text.getBytes("UTF-8")
    var bi = 0
    while bi < bytes.length && vertCount + VERTS_PER_GLYPH <= MAX_GLYPHS * VERTS_PER_GLYPH do
      val ch = bytes(bi).toInt & 0xFF
      if ch >= fd.firstChar && ch < fd.firstChar + fd.numChars then
        !xpos = cursorX; !ypos = cursorY
        stbtt.stbtt_GetBakedQuad(charData, fd.atlasWidth, fd.atlasHeight, ch - fd.firstChar, xpos, ypos, quad, 1)
        val q = !quad
        val base = vertCount * FLOATS_PER_VERT
        // 1
        scratchBuffer(base + 0) = q.x0; scratchBuffer(base + 1) = q.y0; scratchBuffer(base + 2) = q.s0; scratchBuffer(base + 3) = q.t0
        scratchBuffer(base + 4) = q.x1; scratchBuffer(base + 5) = q.y0; scratchBuffer(base + 6) = q.s1; scratchBuffer(base + 7) = q.t0
        scratchBuffer(base + 8) = q.x1; scratchBuffer(base + 9) = q.y1; scratchBuffer(base + 10) = q.s1; scratchBuffer(base + 11) = q.t1
        // 2
        scratchBuffer(base + 12) = q.x0; scratchBuffer(base + 13) = q.y0; scratchBuffer(base + 14) = q.s0; scratchBuffer(base + 15) = q.t0
        scratchBuffer(base + 16) = q.x1; scratchBuffer(base + 17) = q.y1; scratchBuffer(base + 18) = q.s1; scratchBuffer(base + 19) = q.t1
        scratchBuffer(base + 20) = q.x0; scratchBuffer(base + 21) = q.y1; scratchBuffer(base + 22) = q.s0; scratchBuffer(base + 23) = q.t1
        cursorX = !xpos
        vertCount += VERTS_PER_GLYPH
      bi += 1

    stdlib.free(charData.asInstanceOf[Ptr[Byte]])

    if vertCount > 0 then
      glEnable(GL_BLEND.toUInt)
      glBlendFunc(GL_SRC_ALPHA.toUInt, GL_ONE_MINUS_SRC_ALPHA.toUInt)

      GLEWHelper.glBindVertexArray(VAO)
      GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
      GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (vertCount * FLOATS_PER_VERT * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)
      glDrawArrays(GL_TRIANGLES.toUInt, 0, vertCount.toUInt)
      GLEWHelper.glBindVertexArray(0.toUInt)
      glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)

  def measureText(text: String, font: FontType): Vector2 =
    val fd = FontType.data(font)
    val charData = stdlib.malloc((fd.numChars.toLong * sizeof[stbtt_bakedchar].toLong).toUSize).asInstanceOf[Ptr[stbtt_bakedchar]]
    var ci = 0
    while ci < fd.numChars do
        val c = charData + ci
        val base = ci * 7
        (!c).x0 = fd.charData(base + 0).toShort
        (!c).y0 = fd.charData(base + 1).toShort
        (!c).x1 = fd.charData(base + 2).toShort
        (!c).y1 = fd.charData(base + 3).toShort
        (!c).xoff = fd.charData(base + 4)
        (!c).yoff = fd.charData(base + 5)
        (!c).xadvance = fd.charData(base + 6)
        ci += 1

    val xpos = stackalloc[CFloat](1); val ypos = stackalloc[CFloat](1)
    val quad = stackalloc[stbtt_aligned_quad](1)
    !xpos = 0.0f; !ypos = 0.0f

    val bytes = text.getBytes("UTF-8")
    var bi = 0
    while bi < bytes.length do
      val ch = bytes(bi).toInt & 0xFF
      if ch >= fd.firstChar && ch < fd.firstChar + fd.numChars then
        stbtt.stbtt_GetBakedQuad(charData, fd.atlasWidth, fd.atlasHeight, ch - fd.firstChar, xpos, ypos, quad, 1)
      bi += 1

    stdlib.free(charData.asInstanceOf[Ptr[Byte]])
    Vector2(!xpos, fd.size)

  private def copyStringToBuffer(s: String, buf: Ptr[CChar]): Unit =
    val bytes = s.getBytes("UTF-8")
    var i = 0
    while i < bytes.length do
      buf(i) = bytes(i); i += 1
    buf(bytes.length) = 0.toByte