package s2d.core

import s2d.gl.GL.*
import s2d.gl.GLExtras.*
import s2d.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import scalanative.libc.stdio.*
import scalanative.libc.stdlib.*
import scalanative.libc.string.*

object Shaders:
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
        if vertexShader != 0.toUInt then glDeleteShader(vertexShader)
        if fragmentShader != 0.toUInt then glDeleteShader(fragmentShader)
        return None

      val program = glCreateProgram()
      if program == 0.toUInt then
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
        return None

      glAttachShader(program, vertexShader)
      glAttachShader(program, fragmentShader)
      glLinkProgram(program)

      val linkStatus = stackalloc[GLint]()
      glGetProgramiv(program, GL_LINK_STATUS.toUInt, linkStatus)

      if !linkStatus == 0 then
        glDeleteProgram(program)
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
        return None

      glDeleteShader(vertexShader)
      glDeleteShader(fragmentShader)

      Some(Shader(program.toInt, Array.empty[Int]))
    }

  def loadFromMemory(vsCode: String, fsCode: String): Option[Shader] =
    Zone {
      val vsCodeC = toCString(vsCode)
      val fsCodeC = toCString(fsCode)

      val vertexShader = compileShader(GL_VERTEX_SHADER.toUInt, vsCodeC)
      val fragmentShader = compileShader(GL_FRAGMENT_SHADER.toUInt, fsCodeC)

      if vertexShader == 0.toUInt || fragmentShader == 0.toUInt then
        if vertexShader != 0.toUInt then glDeleteShader(vertexShader)
        if fragmentShader != 0.toUInt then glDeleteShader(fragmentShader)
        return None

      val program = glCreateProgram()
      if program == 0.toUInt then
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
        return None

      glAttachShader(program, vertexShader)
      glAttachShader(program, fragmentShader)
      glLinkProgram(program)

      val linkStatus = stackalloc[GLint]()
      glGetProgramiv(program, GL_LINK_STATUS.toUInt, linkStatus)

      if !linkStatus == 0 then
        glDeleteProgram(program)
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
        return None

      glDeleteShader(vertexShader)
      glDeleteShader(fragmentShader)

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
      val shader = glCreateShader(shaderType)
      if shader == 0.toUInt then return 0.toUInt

      val sourceArray = stackalloc[CString](1)
      !sourceArray = source
      glShaderSource(shader, 1.toUInt, sourceArray, null)
      glCompileShader(shader)

      val compileStatus = stackalloc[GLint]()
      glGetShaderiv(shader, GL_COMPILE_STATUS.toUInt, compileStatus)

      if !compileStatus == 0 then
        glDeleteShader(shader)
        return 0.toUInt

      shader
    }

  def isShaderValid(shader: Shader): Boolean =
    if shader.id <= 0 then false
    else glIsProgram(shader.id.toUInt) == GL_TRUE.toUByte

  def getShaderLocation(shader: Shader, uniformName: String): Int =
    if shader.id <= 0 then -1
    else
      Zone {
        val uniformNameC = toCString(uniformName)
        glGetUniformLocation(shader.id.toUInt, uniformNameC)
      }

  def getShaderLocationAttrib(shader: Shader, attribName: String): Int =
    if shader.id <= 0 then -1
    else
      Zone {
        val attribNameC = toCString(attribName)
        glGetAttribLocation(shader.id.toUInt, attribNameC)
      }

  def setShaderValue(shader: Shader, locIndex: Int, value: Ptr[Byte], uniformType: Int): Unit =
    if shader.id <= 0 || locIndex < 0 then return

    glUseProgram(shader.id.toUInt)

    uniformType match
      case t if t == GL_FLOAT.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        glUniform1f(locIndex, !floatPtr)

      case t if t == GL_FLOAT_VEC2.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        glUniform2f(locIndex, floatPtr(0), floatPtr(1))

      case t if t == GL_FLOAT_VEC3.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        glUniform3f(locIndex, floatPtr(0), floatPtr(1), floatPtr(2))

      case t if t == GL_FLOAT_VEC4.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        glUniform4f(locIndex, floatPtr(0), floatPtr(1), floatPtr(2), floatPtr(3))

      case t if t == GL_INT.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        glUniform1i(locIndex, !intPtr)

      case t if t == GL_INT_VEC2.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        glUniform2i(locIndex, intPtr(0), intPtr(1))

      case t if t == GL_INT_VEC3.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        glUniform3i(locIndex, intPtr(0), intPtr(1), intPtr(2))

      case t if t == GL_INT_VEC4.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        glUniform4i(locIndex, intPtr(0), intPtr(1), intPtr(2), intPtr(3))

      case t if t == GL_FLOAT_MAT2.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        glUniformMatrix2fv(locIndex, 1.toUInt, GL_FALSE.toUByte, floatPtr)

      case t if t == GL_FLOAT_MAT3.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        glUniformMatrix3fv(locIndex, 1.toUInt, GL_FALSE.toUByte, floatPtr)

      case t if t == GL_FLOAT_MAT4.toInt =>
        val floatPtr = value.asInstanceOf[Ptr[GLfloat]]
        glUniformMatrix4fv(locIndex, 1.toUInt, GL_FALSE.toUByte, floatPtr)

      case t if t == GL_SAMPLER_2D.toInt =>
        val intPtr = value.asInstanceOf[Ptr[GLint]]
        glUniform1i(locIndex, !intPtr)

      case _ => // unsupported uniform type