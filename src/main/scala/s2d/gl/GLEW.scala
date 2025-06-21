package s2d.gl

import scalanative.unsafe.*
import scalanative.unsigned.*
import s2d.gl.GL.*
import s2d.sdl2.SDL.*
@extern
object GLEW:
  def glewInit(): GLenum = extern

  def glewGetString(name: GLenum): CString = extern
  def glewGetErrorString(error: GLenum): CString = extern

  def glewIsSupported(name: CString): GLboolean = extern

object GLEWConstants:
  val GLEW_OK: GLenum = 0.toUInt
  val GLEW_NO_ERROR: GLenum = 0.toUInt
  val GLEW_ERROR_NO_GL_VERSION: GLenum = 1.toUInt
  val GLEW_ERROR_GL_VERSION_10_ONLY: GLenum = 2.toUInt
  val GLEW_ERROR_GLX_VERSION_11_ONLY: GLenum = 3.toUInt

type PFNGLGENERATEMIPMAPPROC = CFuncPtr1[GLenum, Unit]

// shaders
type PFNGLCREATESHADERPROC = CFuncPtr1[GLenum, GLuint]
type PFNGLDELETESHADERPROC = CFuncPtr1[GLuint, Unit]
type PFNGLSHADERSOURCEPROC = CFuncPtr4[GLuint, GLsizei, Ptr[Ptr[GLchar]], Ptr[GLint], Unit]
type PFNGLCOMPILESHADERPROC = CFuncPtr1[GLuint, Unit]
type PFNGLGETSHADERIVPROC = CFuncPtr3[GLuint, GLenum, Ptr[GLint], Unit]
type PFNGLCREATEPROGRAMPROC = CFuncPtr0[GLuint]
type PFNGLDELETEPROGRAMPROC = CFuncPtr1[GLuint, Unit]
type PFNGLATTACHSHADERPROC = CFuncPtr2[GLuint, GLuint, Unit]
type PFNGLLINKPROGRAMPROC = CFuncPtr1[GLuint, Unit]
type PFNGLGETPROGRAMIVPROC = CFuncPtr3[GLuint, GLenum, Ptr[GLint], Unit]
type PFNGLUSEPROGRAMPROC = CFuncPtr1[GLuint, Unit]
type PFNGLGETUNIFORMLOCATIONPROC = CFuncPtr2[GLuint, Ptr[GLchar], GLint]
type PFNGLGETATTRIBLOCATIONPROC = CFuncPtr2[GLuint, Ptr[GLchar], GLint]
type PFNGLUNIFORM1FPROC = CFuncPtr2[GLint, GLfloat, Unit]
type PFNGLUNIFORM2FPROC = CFuncPtr3[GLint, GLfloat, GLfloat, Unit]
type PFNGLUNIFORM3FPROC = CFuncPtr4[GLint, GLfloat, GLfloat, GLfloat, Unit]
type PFNGLUNIFORM4FPROC = CFuncPtr5[GLint, GLfloat, GLfloat, GLfloat, GLfloat, Unit]
type PFNGLUNIFORM1IPROC = CFuncPtr2[GLint, GLint, Unit]
type PFNGLUNIFORM2IPROC = CFuncPtr3[GLint, GLint, GLint, Unit]
type PFNGLUNIFORM3IPROC = CFuncPtr4[GLint, GLint, GLint, GLint, Unit]
type PFNGLUNIFORM4IPROC = CFuncPtr5[GLint, GLint, GLint, GLint, GLint, Unit]
type PFNGLUNIFORM1FVPROC = CFuncPtr3[GLint, GLsizei, Ptr[GLfloat], Unit]
type PFNGLUNIFORM2FVPROC = CFuncPtr3[GLint, GLsizei, Ptr[GLfloat], Unit]
type PFNGLUNIFORM3FVPROC = CFuncPtr3[GLint, GLsizei, Ptr[GLfloat], Unit]
type PFNGLUNIFORM4FVPROC = CFuncPtr3[GLint, GLsizei, Ptr[GLfloat], Unit]
type PFNGLUNIFORM1IVPROC = CFuncPtr3[GLint, GLsizei, Ptr[GLint], Unit]
type PFNGLUNIFORM2IVPROC = CFuncPtr3[GLint, GLsizei, Ptr[GLint], Unit]
type PFNGLUNIFORM3IVPROC = CFuncPtr3[GLint, GLsizei, Ptr[GLint], Unit]
type PFNGLUNIFORM4IVPROC = CFuncPtr3[GLint, GLsizei, Ptr[GLint], Unit]
type PFNGLUNIFORMMATRIX2FVPROC = CFuncPtr4[GLint, GLsizei, GLboolean, Ptr[GLfloat], Unit]
type PFNGLUNIFORMMATRIX3FVPROC = CFuncPtr4[GLint, GLsizei, GLboolean, Ptr[GLfloat], Unit]
type PFNGLUNIFORMMATRIX4FVPROC = CFuncPtr4[GLint, GLsizei, GLboolean, Ptr[GLfloat], Unit]
type PFNGLISPROGRAMPROC = CFuncPtr1[GLuint, GLboolean]
type PFNGLACTIVETEXTUREPROC = CFuncPtr1[GLenum, Unit]

object GLEWHelper:
  import GLEWConstants.*

  private var glGenerateMipmapPtr: PFNGLGENERATEMIPMAPPROC = null

  // shaders
  private var glCreateShaderPtr: PFNGLCREATESHADERPROC = null
  private var glDeleteShaderPtr: PFNGLDELETESHADERPROC = null
  private var glShaderSourcePtr: PFNGLSHADERSOURCEPROC = null
  private var glCompileShaderPtr: PFNGLCOMPILESHADERPROC = null
  private var glGetShaderivPtr: PFNGLGETSHADERIVPROC = null
  private var glCreateProgramPtr: PFNGLCREATEPROGRAMPROC = null
  private var glDeleteProgramPtr: PFNGLDELETEPROGRAMPROC = null
  private var glAttachShaderPtr: PFNGLATTACHSHADERPROC = null
  private var glLinkProgramPtr: PFNGLLINKPROGRAMPROC = null
  private var glGetProgramivPtr: PFNGLGETPROGRAMIVPROC = null
  private var glUseProgramPtr: PFNGLUSEPROGRAMPROC = null
  private var glGetUniformLocationPtr: PFNGLGETUNIFORMLOCATIONPROC = null
  private var glGetAttribLocationPtr: PFNGLGETATTRIBLOCATIONPROC = null
  private var glUniform1fPtr: PFNGLUNIFORM1FPROC = null
  private var glUniform2fPtr: PFNGLUNIFORM2FPROC = null
  private var glUniform3fPtr: PFNGLUNIFORM3FPROC = null
  private var glUniform4fPtr: PFNGLUNIFORM4FPROC = null
  private var glUniform1iPtr: PFNGLUNIFORM1IPROC = null
  private var glUniform2iPtr: PFNGLUNIFORM2IPROC = null
  private var glUniform3iPtr: PFNGLUNIFORM3IPROC = null
  private var glUniform4iPtr: PFNGLUNIFORM4IPROC = null
  private var glUniform1fvPtr: PFNGLUNIFORM1FVPROC = null
  private var glUniform2fvPtr: PFNGLUNIFORM2FVPROC = null
  private var glUniform3fvPtr: PFNGLUNIFORM3FVPROC = null
  private var glUniform4fvPtr: PFNGLUNIFORM4FVPROC = null
  private var glUniform1ivPtr: PFNGLUNIFORM1IVPROC = null
  private var glUniform2ivPtr: PFNGLUNIFORM2IVPROC = null
  private var glUniform3ivPtr: PFNGLUNIFORM3IVPROC = null
  private var glUniform4ivPtr: PFNGLUNIFORM4IVPROC = null
  private var glUniformMatrix2fvPtr: PFNGLUNIFORMMATRIX2FVPROC = null
  private var glUniformMatrix3fvPtr: PFNGLUNIFORMMATRIX3FVPROC = null
  private var glUniformMatrix4fvPtr: PFNGLUNIFORMMATRIX4FVPROC = null
  private var glIsProgramPtr: PFNGLISPROGRAMPROC = null
  private var glActiveTexturePtr: PFNGLACTIVETEXTUREPROC = null

  def initializeGLEW(): Boolean =
    val result = GLEW.glewInit()
    if result == GLEW_OK then
      loadModernGLFunctions()
      true
    else
      val errorStr = fromCString(GLEW.glewGetErrorString(result))
      println(s"GLEW initialization failed: $errorStr")
      false

  private def loadModernGLFunctions(): Unit =
    Zone {
      val funcPtr = SDL_GL_GetProcAddress(c"glGenerateMipmap")
      if funcPtr != null then
        glGenerateMipmapPtr = CFuncPtr.fromPtr[PFNGLGENERATEMIPMAPPROC](funcPtr)

      // shader functions
      loadShaderFunction(c"glCreateShader", glCreateShaderPtr, (ptr: Ptr[Byte]) => glCreateShaderPtr = CFuncPtr.fromPtr[PFNGLCREATESHADERPROC](ptr))
      loadShaderFunction(c"glDeleteShader", glDeleteShaderPtr, (ptr: Ptr[Byte]) => glDeleteShaderPtr = CFuncPtr.fromPtr[PFNGLDELETESHADERPROC](ptr))
      loadShaderFunction(c"glShaderSource", glShaderSourcePtr, (ptr: Ptr[Byte]) => glShaderSourcePtr = CFuncPtr.fromPtr[PFNGLSHADERSOURCEPROC](ptr))
      loadShaderFunction(c"glCompileShader", glCompileShaderPtr, (ptr: Ptr[Byte]) => glCompileShaderPtr = CFuncPtr.fromPtr[PFNGLCOMPILESHADERPROC](ptr))
      loadShaderFunction(c"glGetShaderiv", glGetShaderivPtr, (ptr: Ptr[Byte]) => glGetShaderivPtr = CFuncPtr.fromPtr[PFNGLGETSHADERIVPROC](ptr))
      loadShaderFunction(c"glCreateProgram", glCreateProgramPtr, (ptr: Ptr[Byte]) => glCreateProgramPtr = CFuncPtr.fromPtr[PFNGLCREATEPROGRAMPROC](ptr))
      loadShaderFunction(c"glDeleteProgram", glDeleteProgramPtr, (ptr: Ptr[Byte]) => glDeleteProgramPtr = CFuncPtr.fromPtr[PFNGLDELETEPROGRAMPROC](ptr))
      loadShaderFunction(c"glAttachShader", glAttachShaderPtr, (ptr: Ptr[Byte]) => glAttachShaderPtr = CFuncPtr.fromPtr[PFNGLATTACHSHADERPROC](ptr))
      loadShaderFunction(c"glLinkProgram", glLinkProgramPtr, (ptr: Ptr[Byte]) => glLinkProgramPtr = CFuncPtr.fromPtr[PFNGLLINKPROGRAMPROC](ptr))
      loadShaderFunction(c"glGetProgramiv", glGetProgramivPtr, (ptr: Ptr[Byte]) => glGetProgramivPtr = CFuncPtr.fromPtr[PFNGLGETPROGRAMIVPROC](ptr))
      loadShaderFunction(c"glUseProgram", glUseProgramPtr, (ptr: Ptr[Byte]) => glUseProgramPtr = CFuncPtr.fromPtr[PFNGLUSEPROGRAMPROC](ptr))
      loadShaderFunction(c"glGetUniformLocation", glGetUniformLocationPtr, (ptr: Ptr[Byte]) => glGetUniformLocationPtr = CFuncPtr.fromPtr[PFNGLGETUNIFORMLOCATIONPROC](ptr))
      loadShaderFunction(c"glGetAttribLocation", glGetAttribLocationPtr, (ptr: Ptr[Byte]) => glGetAttribLocationPtr = CFuncPtr.fromPtr[PFNGLGETATTRIBLOCATIONPROC](ptr))
      loadShaderFunction(c"glUniform1f", glUniform1fPtr, (ptr: Ptr[Byte]) => glUniform1fPtr = CFuncPtr.fromPtr[PFNGLUNIFORM1FPROC](ptr))
      loadShaderFunction(c"glUniform2f", glUniform2fPtr, (ptr: Ptr[Byte]) => glUniform2fPtr = CFuncPtr.fromPtr[PFNGLUNIFORM2FPROC](ptr))
      loadShaderFunction(c"glUniform3f", glUniform3fPtr, (ptr: Ptr[Byte]) => glUniform3fPtr = CFuncPtr.fromPtr[PFNGLUNIFORM3FPROC](ptr))
      loadShaderFunction(c"glUniform4f", glUniform4fPtr, (ptr: Ptr[Byte]) => glUniform4fPtr = CFuncPtr.fromPtr[PFNGLUNIFORM4FPROC](ptr))
      loadShaderFunction(c"glUniform1i", glUniform1iPtr, (ptr: Ptr[Byte]) => glUniform1iPtr = CFuncPtr.fromPtr[PFNGLUNIFORM1IPROC](ptr))
      loadShaderFunction(c"glUniform2i", glUniform2iPtr, (ptr: Ptr[Byte]) => glUniform2iPtr = CFuncPtr.fromPtr[PFNGLUNIFORM2IPROC](ptr))
      loadShaderFunction(c"glUniform3i", glUniform3iPtr, (ptr: Ptr[Byte]) => glUniform3iPtr = CFuncPtr.fromPtr[PFNGLUNIFORM3IPROC](ptr))
      loadShaderFunction(c"glUniform4i", glUniform4iPtr, (ptr: Ptr[Byte]) => glUniform4iPtr = CFuncPtr.fromPtr[PFNGLUNIFORM4IPROC](ptr))
      loadShaderFunction(c"glUniform1fv", glUniform1fvPtr, (ptr: Ptr[Byte]) => glUniform1fvPtr = CFuncPtr.fromPtr[PFNGLUNIFORM1FVPROC](ptr))
      loadShaderFunction(c"glUniform2fv", glUniform2fvPtr, (ptr: Ptr[Byte]) => glUniform2fvPtr = CFuncPtr.fromPtr[PFNGLUNIFORM2FVPROC](ptr))
      loadShaderFunction(c"glUniform3fv", glUniform3fvPtr, (ptr: Ptr[Byte]) => glUniform3fvPtr = CFuncPtr.fromPtr[PFNGLUNIFORM3FVPROC](ptr))
      loadShaderFunction(c"glUniform4fv", glUniform4fvPtr, (ptr: Ptr[Byte]) => glUniform4fvPtr = CFuncPtr.fromPtr[PFNGLUNIFORM4FVPROC](ptr))
      loadShaderFunction(c"glUniform1iv", glUniform1ivPtr, (ptr: Ptr[Byte]) => glUniform1ivPtr = CFuncPtr.fromPtr[PFNGLUNIFORM1IVPROC](ptr))
      loadShaderFunction(c"glUniform2iv", glUniform2ivPtr, (ptr: Ptr[Byte]) => glUniform2ivPtr = CFuncPtr.fromPtr[PFNGLUNIFORM2IVPROC](ptr))
      loadShaderFunction(c"glUniform3iv", glUniform3ivPtr, (ptr: Ptr[Byte]) => glUniform3ivPtr = CFuncPtr.fromPtr[PFNGLUNIFORM3IVPROC](ptr))
      loadShaderFunction(c"glUniform4iv", glUniform4ivPtr, (ptr: Ptr[Byte]) => glUniform4ivPtr = CFuncPtr.fromPtr[PFNGLUNIFORM4IVPROC](ptr))
      loadShaderFunction(c"glUniformMatrix2fv", glUniformMatrix2fvPtr, (ptr: Ptr[Byte]) => glUniformMatrix2fvPtr = CFuncPtr.fromPtr[PFNGLUNIFORMMATRIX2FVPROC](ptr))
      loadShaderFunction(c"glUniformMatrix3fv", glUniformMatrix3fvPtr, (ptr: Ptr[Byte]) => glUniformMatrix3fvPtr = CFuncPtr.fromPtr[PFNGLUNIFORMMATRIX3FVPROC](ptr))
      loadShaderFunction(c"glUniformMatrix4fv", glUniformMatrix4fvPtr, (ptr: Ptr[Byte]) => glUniformMatrix4fvPtr = CFuncPtr.fromPtr[PFNGLUNIFORMMATRIX4FVPROC](ptr))
      loadShaderFunction(c"glIsProgram", glIsProgramPtr, (ptr: Ptr[Byte]) => glIsProgramPtr = CFuncPtr.fromPtr[PFNGLISPROGRAMPROC](ptr))
      loadShaderFunction(c"glActiveTexture", glActiveTexturePtr, (ptr: Ptr[Byte]) => glActiveTexturePtr = CFuncPtr.fromPtr[PFNGLACTIVETEXTUREPROC](ptr))
    }

  private def loadShaderFunction[T](name: CString, currentPtr: T, setPtr: Ptr[Byte] => Unit): Unit =
    val funcPtr = SDL_GL_GetProcAddress(name)
    if funcPtr != null then
      setPtr(funcPtr)

  def glGenerateMipmap(target: GLenum): Unit =
    if glGenerateMipmapPtr != null then
      glGenerateMipmapPtr(target)
    else
      println("Warning: glGenerateMipmap not available - skipping mipmap generation")

  def isGenerateMipmapAvailable: Boolean = glGenerateMipmapPtr != null

  def checkExtension(extensionName: String): Boolean =
    Zone {
      GLEW.glewIsSupported(toCString(extensionName)) != 0.toUByte
    }

  def glCreateShader(shaderType: GLuint): GLuint =
    if glCreateShaderPtr != null then glCreateShaderPtr(shaderType) else 0.toUInt

  def glDeleteShader(shader: GLuint): Unit =
    if glDeleteShaderPtr != null then glDeleteShaderPtr(shader)

  def glShaderSource(shader: GLuint, count: GLsizei, string: Ptr[Ptr[GLchar]], length: Ptr[GLint]): Unit =
    if glShaderSourcePtr != null then glShaderSourcePtr(shader, count, string, length)

  def glCompileShader(shader: GLuint): Unit =
    if glCompileShaderPtr != null then glCompileShaderPtr(shader)

  def glGetShaderiv(shader: GLuint, pname: GLenum, params: Ptr[GLint]): Unit =
    if glGetShaderivPtr != null then glGetShaderivPtr(shader, pname, params)

  def glCreateProgram(): GLuint =
    if glCreateProgramPtr != null then glCreateProgramPtr() else 0.toUInt

  def glDeleteProgram(program: GLuint): Unit =
    if glDeleteProgramPtr != null then glDeleteProgramPtr(program)

  def glAttachShader(program: GLuint, shader: GLuint): Unit =
    if glAttachShaderPtr != null then glAttachShaderPtr(program, shader)

  def glLinkProgram(program: GLuint): Unit =
    if glLinkProgramPtr != null then glLinkProgramPtr(program)

  def glGetProgramiv(program: GLuint, pname: GLenum, params: Ptr[GLint]): Unit =
    if glGetProgramivPtr != null then glGetProgramivPtr(program, pname, params)

  def glUseProgram(program: GLuint): Unit =
    if glUseProgramPtr != null then glUseProgramPtr(program)

  def glGetUniformLocation(program: GLuint, name: Ptr[GLchar]): GLint =
    if glGetUniformLocationPtr != null then glGetUniformLocationPtr(program, name) else -1

  def glGetAttribLocation(program: GLuint, name: Ptr[GLchar]): GLint =
    if glGetAttribLocationPtr != null then glGetAttribLocationPtr(program, name) else -1

  def glUniform1f(location: GLint, v0: GLfloat): Unit =
    if glUniform1fPtr != null then glUniform1fPtr(location, v0)

  def glUniform2f(location: GLint, v0: GLfloat, v1: GLfloat): Unit =
    if glUniform2fPtr != null then glUniform2fPtr(location, v0, v1)

  def glUniform3f(location: GLint, v0: GLfloat, v1: GLfloat, v2: GLfloat): Unit =
    if glUniform3fPtr != null then glUniform3fPtr(location, v0, v1, v2)

  def glUniform4f(location: GLint, v0: GLfloat, v1: GLfloat, v2: GLfloat, v3: GLfloat): Unit =
    if glUniform4fPtr != null then glUniform4fPtr(location, v0, v1, v2, v3)

  def glUniform1i(location: GLint, v0: GLint): Unit =
    if glUniform1iPtr != null then glUniform1iPtr(location, v0)

  def glUniform2i(location: GLint, v0: GLint, v1: GLint): Unit =
    if glUniform2iPtr != null then glUniform2iPtr(location, v0, v1)

  def glUniform3i(location: GLint, v0: GLint, v1: GLint, v2: GLint): Unit =
    if glUniform3iPtr != null then glUniform3iPtr(location, v0, v1, v2)

  def glUniform4i(location: GLint, v0: GLint, v1: GLint, v2: GLint, v3: GLint): Unit =
    if glUniform4iPtr != null then glUniform4iPtr(location, v0, v1, v2, v3)

  def glUniform1fv(location: GLint, count: GLsizei, value: Ptr[GLfloat]): Unit =
    if glUniform1fvPtr != null then glUniform1fvPtr(location, count, value)

  def glUniform2fv(location: GLint, count: GLsizei, value: Ptr[GLfloat]): Unit =
    if glUniform2fvPtr != null then glUniform2fvPtr(location, count, value)

  def glUniform3fv(location: GLint, count: GLsizei, value: Ptr[GLfloat]): Unit =
    if glUniform3fvPtr != null then glUniform3fvPtr(location, count, value)

  def glUniform4fv(location: GLint, count: GLsizei, value: Ptr[GLfloat]): Unit =
    if glUniform4fvPtr != null then glUniform4fvPtr(location, count, value)

  def glUniform1iv(location: GLint, count: GLsizei, value: Ptr[GLint]): Unit =
    if glUniform1ivPtr != null then glUniform1ivPtr(location, count, value)

  def glUniform2iv(location: GLint, count: GLsizei, value: Ptr[GLint]): Unit =
    if glUniform2ivPtr != null then glUniform2ivPtr(location, count, value)

  def glUniform3iv(location: GLint, count: GLsizei, value: Ptr[GLint]): Unit =
    if glUniform3ivPtr != null then glUniform3ivPtr(location, count, value)

  def glUniform4iv(location: GLint, count: GLsizei, value: Ptr[GLint]): Unit =
    if glUniform4ivPtr != null then glUniform4ivPtr(location, count, value)

  def glUniformMatrix2fv(location: GLint, count: GLsizei, transpose: GLboolean, value: Ptr[GLfloat]): Unit =
    if glUniformMatrix2fvPtr != null then glUniformMatrix2fvPtr(location, count, transpose, value)

  def glUniformMatrix3fv(location: GLint, count: GLsizei, transpose: GLboolean, value: Ptr[GLfloat]): Unit =
    if glUniformMatrix3fvPtr != null then glUniformMatrix3fvPtr(location, count, transpose, value)

  def glUniformMatrix4fv(location: GLint, count: GLsizei, transpose: GLboolean, value: Ptr[GLfloat]): Unit =
    if glUniformMatrix4fvPtr != null then glUniformMatrix4fvPtr(location, count, transpose, value)

  def glIsProgram(program: GLuint): GLboolean =
    if glIsProgramPtr != null then glIsProgramPtr(program) else 0.toUByte

  def glActiveTexture(texture: GLenum): Unit =
    if glActiveTexturePtr != null then glActiveTexturePtr(texture)