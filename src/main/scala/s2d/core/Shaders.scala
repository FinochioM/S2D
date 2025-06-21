package s2d.core

import s2d.gl.GL.*
import s2d.gl.GLExtras.*
import s2d.gl.GLEWHelper
import s2d.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import scalanative.libc.stdio.*
import scalanative.libc.stdlib.*
import scalanative.libc.string.*

object Shaders:

  import s2d.gl.GLEWHelper

  def load(vsFilename: String, fsFilename: String): Option[Shader] =
    Zone {
      val vsFilenameC = toCString(vsFilename)
      val fsFilenameC = toCString(fsFilename)

      val vsSource = readShaderFile(vsFilenameC)
      if vsSource == null then return None

      val fsSource = readShaderFile(fsFilenameC)
      if fsSource == null then
        free(vsSource)
        return None

      val vertexShader = compileShader(GL_VERTEX_SHADER.toUInt, vsSource)
      val fragmentShader = compileShader(GL_FRAGMENT_SHADER.toUInt, fsSource)

      free(vsSource)
      free(fsSource)

      if vertexShader == 0.toUInt || fragmentShader == 0.toUInt then
        if vertexShader != 0.toUInt then GLEWHelper.glDeleteShader(vertexShader)
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

      val linkStatus = stackalloc[GLint]()
      GLEWHelper.glGetProgramiv(program, GL_LINK_STATUS.toUInt, linkStatus)

      if !linkStatus == 0 then
        GLEWHelper.glDeleteProgram(program)
        GLEWHelper.glDeleteShader(vertexShader)
        GLEWHelper.glDeleteShader(fragmentShader)
        return None

      GLEWHelper.glDeleteShader(vertexShader)
      GLEWHelper.glDeleteShader(fragmentShader)

      Some(Shader(program.toInt, Array.empty[Int]))
    }

  def loadFromMemory(vsCode: String, fsCode: String): Option[Shader] =
    Zone {
      val vsCodeC = toCString(vsCode)
      val fsCodeC = toCString(fsCode)

      val vertexShader = compileShader(GL_VERTEX_SHADER.toUInt, vsCodeC)
      val fragmentShader = compileShader(GL_FRAGMENT_SHADER.toUInt, fsCodeC)

      if vertexShader == 0.toUInt || fragmentShader == 0.toUInt then
        if vertexShader != 0.toUInt then GLEWHelper.glDeleteShader(vertexShader)
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

      val linkStatus = stackalloc[GLint]()
      GLEWHelper.glGetProgramiv(program, GL_LINK_STATUS.toUInt, linkStatus)

      if !linkStatus == 0 then
        GLEWHelper.glDeleteProgram(program)
        GLEWHelper.glDeleteShader(vertexShader)
        GLEWHelper.glDeleteShader(fragmentShader)
        return None

      GLEWHelper.glDeleteShader(vertexShader)
      GLEWHelper.glDeleteShader(fragmentShader)

      Some(Shader(program.toInt, Array.empty[Int]))
    }

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

  def isShaderValid(shader: Shader): Boolean =
    if shader.id <= 0 then false
    else GLEWHelper.glIsProgram(shader.id.toUInt) == GL_TRUE.toUByte

  def getShaderLocation(shader: Shader, uniformName: String): Int =
    if shader.id <= 0 then -1
    else
      Zone {
        val uniformNameC = toCString(uniformName)
        GLEWHelper.glGetUniformLocation(shader.id.toUInt, uniformNameC)
      }

  def getShaderLocationAttrib(shader: Shader, attribName: String): Int =
    if shader.id <= 0 then -1
    else
      Zone {
        val attribNameC = toCString(attribName)
        GLEWHelper.glGetAttribLocation(shader.id.toUInt, attribNameC)
      }

  def setShaderValue(shader: Shader, locIndex: Int, value: Ptr[Byte], uniformType: Int): Unit =
    if shader.id <= 0 || locIndex < 0 then return

    GLEWHelper.glUseProgram(shader.id.toUInt)

    uniformType match
      case t if t == GL_FLOAT.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniform1f(locIndex, !floatPtr)

      case t if t == GL_FLOAT_VEC2.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniform2f(locIndex, floatPtr(0), floatPtr(1))

      case t if t == GL_FLOAT_VEC3.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniform3f(locIndex, floatPtr(0), floatPtr(1), floatPtr(2))

      case t if t == GL_FLOAT_VEC4.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniform4f(locIndex, floatPtr(0), floatPtr(1), floatPtr(2), floatPtr(3))

      case t if t == GL_INT.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        GLEWHelper.glUniform1i(locIndex, !intPtr)

      case t if t == GL_INT_VEC2.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        GLEWHelper.glUniform2i(locIndex, intPtr(0), intPtr(1))

      case t if t == GL_INT_VEC3.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        GLEWHelper.glUniform3i(locIndex, intPtr(0), intPtr(1), intPtr(2))

      case t if t == GL_INT_VEC4.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        GLEWHelper.glUniform4i(locIndex, intPtr(0), intPtr(1), intPtr(2), intPtr(3))

      case t if t == GL_FLOAT_MAT2.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniformMatrix2fv(locIndex, 1.toUInt, GL_FALSE.toUByte, floatPtr)

      case t if t == GL_FLOAT_MAT3.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniformMatrix3fv(locIndex, 1.toUInt, GL_FALSE.toUByte, floatPtr)

      case t if t == GL_FLOAT_MAT4.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniformMatrix4fv(locIndex, 1.toUInt, GL_FALSE.toUByte, floatPtr)

      case t if t == GL_SAMPLER_2D.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        GLEWHelper.glUniform1i(locIndex, !intPtr)

      case _ => // unsupported uniform type

  def setShaderValueV(shader: Shader, locIndex: Int, value: Ptr[Byte], uniformType: Int, count: Int): Unit =
    if shader.id <= 0 || locIndex < 0 || count <= 0 then return

    GLEWHelper.glUseProgram(shader.id.toUInt)

    uniformType match
      case t if t == GL_FLOAT.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniform1fv(locIndex, count.toUInt, floatPtr)

      case t if t == GL_FLOAT_VEC2.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniform2fv(locIndex, count.toUInt, floatPtr)

      case t if t == GL_FLOAT_VEC3.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniform3fv(locIndex, count.toUInt, floatPtr)

      case t if t == GL_FLOAT_VEC4.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniform4fv(locIndex, count.toUInt, floatPtr)

      case t if t == GL_INT.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        GLEWHelper.glUniform1iv(locIndex, count.toUInt, intPtr)

      case t if t == GL_INT_VEC2.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        GLEWHelper.glUniform2iv(locIndex, count.toUInt, intPtr)

      case t if t == GL_INT_VEC3.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        GLEWHelper.glUniform3iv(locIndex, count.toUInt, intPtr)

      case t if t == GL_INT_VEC4.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        GLEWHelper.glUniform4iv(locIndex, count.toUInt, intPtr)

      case t if t == GL_FLOAT_MAT2.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniformMatrix2fv(locIndex, count.toUInt, GL_FALSE.toUByte, floatPtr)

      case t if t == GL_FLOAT_MAT3.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniformMatrix3fv(locIndex, count.toUInt, GL_FALSE.toUByte, floatPtr)

      case t if t == GL_FLOAT_MAT4.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        GLEWHelper.glUniformMatrix4fv(locIndex, count.toUInt, GL_FALSE.toUByte, floatPtr)

      case t if t == GL_SAMPLER_2D.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        GLEWHelper.glUniform1iv(locIndex, count.toUInt, intPtr)

      case _ => // unsupported uniform type

  def setShaderValueMatrix(shader: Shader, locIndex: Int, mat: Matrix): Unit =
    if shader.id <= 0 || locIndex < 0 then return

    GLEWHelper.glUseProgram(shader.id.toUInt)

    Zone {
      val matrixPtr = alloc[GLfloat](16)
      var i = 0
      while i < 16 do
        matrixPtr(i) = mat.values(i)
        i += 1

      GLEWHelper.glUniformMatrix4fv(locIndex, 1.toUInt, GL_FALSE.toUByte, matrixPtr)
    }

  def setShaderValueTexture(shader: Shader, locIndex: Int, texture: Texture2D): Unit =
    if shader.id <= 0 || locIndex < 0 || texture.id <= 0 then return

    GLEWHelper.glUseProgram(shader.id.toUInt)

    GLEWHelper.glActiveTexture(GL_TEXTURE0.toUInt)
    GLEWHelper.glBindTexture(GL_TEXTURE_2D.toUInt, texture.id.toUInt)

    GLEWHelper.glUniform1i(locIndex, 0)

  def unloadShader(shader: Shader): Unit =
    if shader.id > 0 then
      GLEWHelper.glDeleteProgram(shader.id.toUInt)