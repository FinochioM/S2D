package s2d.core

import s2d.backend.gl.GL.*
import s2d.backend.gl.GLExtras.*
import s2d.backend.gl.GLEWHelper
import s2d.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import scalanative.libc.stdio.*
import scalanative.libc.stdlib.*
import scalanative.libc.string.*
import scala.annotation.targetName

object Shaders:

  def load(vsFilename: String, fsFilename: String): Option[Shader] =
    Zone {
      val vsSource = readShaderFile(toCString(vsFilename))
      if vsSource == null then return None

      val fsSource = readShaderFile(toCString(fsFilename))
      if fsSource == null then
        free(vsSource)
        return None

      val result = linkProgram(vsSource, fsSource)
      free(vsSource)
      free(fsSource)
      result
    }

  def loadFromMemory(vsCode: String, fsCode: String): Option[Shader] =
    Zone {
      linkProgram(toCString(vsCode), toCString(fsCode))
    }

  def isShaderValid(shader: Shader): Boolean =
    if shader.id <= 0 then false
    else GLEWHelper.glIsProgram(shader.id.toUInt) == GL_TRUE.toUByte

  def getShaderLocation(shader: Shader, uniformName: String): Int =
    if shader.id <= 0 then -1
    else Zone { GLEWHelper.glGetUniformLocation(shader.id.toUInt, toCString(uniformName)) }

  def getShaderLocationAttrib(shader: Shader, attribName: String): Int =
    if shader.id <= 0 then -1
    else Zone { GLEWHelper.glGetAttribLocation(shader.id.toUInt, toCString(attribName)) }

  def setShaderValue(shader: Shader, locIndex: Int, value: Float): Unit =
    if shader.id <= 0 || locIndex < 0 then return
    GLEWHelper.glUseProgram(shader.id.toUInt)
    GLEWHelper.glUniform1f(locIndex, value)

  def setShaderValue(shader: Shader, locIndex: Int, value: Int): Unit =
    if shader.id <= 0 || locIndex < 0 then return
    GLEWHelper.glUseProgram(shader.id.toUInt)
    GLEWHelper.glUniform1i(locIndex, value)

  @targetName("setShaderValueVec2")
  def setShaderValue(shader: Shader, locIndex: Int, value: Vector2): Unit =
    if shader.id <= 0 || locIndex < 0 then return
    GLEWHelper.glUseProgram(shader.id.toUInt)
    GLEWHelper.glUniform2f(locIndex, value.x, value.y)

  def setShaderValue(shader: Shader, locIndex: Int, x: Float, y: Float, z: Float): Unit =
    if shader.id <= 0 || locIndex < 0 then return
    GLEWHelper.glUseProgram(shader.id.toUInt)
    GLEWHelper.glUniform3f(locIndex, x, y, z)

  def setShaderValue(shader: Shader, locIndex: Int, x: Float, y: Float, z: Float, w: Float): Unit =
    if shader.id <= 0 || locIndex < 0 then return
    GLEWHelper.glUseProgram(shader.id.toUInt)
    GLEWHelper.glUniform4f(locIndex, x, y, z, w)

  @targetName("setShaderValueColor")
  def setShaderValue(shader: Shader, locIndex: Int, color: Color): Unit =
    if shader.id <= 0 || locIndex < 0 then return
    GLEWHelper.glUseProgram(shader.id.toUInt)
    GLEWHelper.glUniform4f(locIndex, color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)

  @targetName("setShaderValueTexture")
  def setShaderValue(shader: Shader, locIndex: Int, texture: Texture2D): Unit =
    if shader.id <= 0 || locIndex < 0 || texture.id <= 0 then return
    GLEWHelper.glUseProgram(shader.id.toUInt)
    GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
    GLEWHelper.glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)
    GLEWHelper.glUniform1i(locIndex, 0)

  def unloadShader(shader: Shader): Unit =
    if shader.id > 0 then
      GLEWHelper.glDeleteProgram(shader.id.toUInt)

  private def linkProgram(vsSource: CString, fsSource: CString): Option[Shader] =
    val vertexShader   = compileShader(GL_VERTEX_SHADER.toUInt, vsSource)
    val fragmentShader = compileShader(GL_FRAGMENT_SHADER.toUInt, fsSource)

    if vertexShader == 0.toUInt || fragmentShader == 0.toUInt then
      if vertexShader   != 0.toUInt then GLEWHelper.glDeleteShader(vertexShader)
      if fragmentShader != 0.toUInt then GLEWHelper.glDeleteShader(fragmentShader)
      return None

    val program = GLEWHelper.glCreateProgram()
    if program == 0.toUInt then
      GLEWHelper.glDeleteShader(vertexShader)
      GLEWHelper.glDeleteShader(fragmentShader)
      return None

    GLEWHelper.glAttachShader(program, vertexShader)
    GLEWHelper.glAttachShader(program, fragmentShader)
    GLEWHelper.glLinkProgram(program)

    Zone {
      val linkStatus = stackalloc[GLint]()
      GLEWHelper.glGetProgramiv(program, GL_LINK_STATUS.toUInt, linkStatus)
      if !linkStatus == 0 then
        GLEWHelper.glDeleteProgram(program)
        GLEWHelper.glDeleteShader(vertexShader)
        GLEWHelper.glDeleteShader(fragmentShader)
        return None
    }

    GLEWHelper.glDeleteShader(vertexShader)
    GLEWHelper.glDeleteShader(fragmentShader)

    Some(Shader(program.toInt, Array.empty[Int]))

  private def readShaderFile(filename: CString): Ptr[CChar] =
    val file = fopen(filename, c"rb")
    if file == null then return null
    try
      fseek(file, 0, SEEK_END)
      val fileSize = ftell(file)
      fseek(file, 0, SEEK_SET)
      if fileSize <= 0 then return null

      val source = malloc((fileSize + 1).toLong).asInstanceOf[Ptr[CChar]]
      if source == null then return null

      val bytesRead = fread(source.asInstanceOf[Ptr[Byte]], 1.toCSize, fileSize.toCSize, file)
      if bytesRead != fileSize then
        free(source)
        return null

      source(fileSize.toInt) = 0.toByte
      source
    finally
      fclose(file)

  private def compileShader(shaderType: GLuint, source: CString): GLuint =
    Zone {
      val shader = GLEWHelper.glCreateShader(shaderType)
      if shader == 0.toUInt then return 0.toUInt

      val sourceArray = stackalloc[CString](1)
      !sourceArray = source
      GLEWHelper.glShaderSource(shader, 1.toUInt, sourceArray, null)
      GLEWHelper.glCompileShader(shader)

      val compileStatus = stackalloc[GLint]()
      GLEWHelper.glGetShaderiv(shader, GL_COMPILE_STATUS.toUInt, compileStatus)

      if !compileStatus == 0 then
        GLEWHelper.glDeleteShader(shader)
        return 0.toUInt

      shader
    }