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

object GLEWHelper:
  import GLEWConstants.*

  private var glGenerateMipmapPtr: PFNGLGENERATEMIPMAPPROC = null

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
    }

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