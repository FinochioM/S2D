package s2d.core

import s2d.backend.gl.GL.*
import s2d.backend.gl.GLExtras.*
import s2d.backend.gl.GLEWHelper
import s2d.types.*
import s2d.backend.sdl2.SDL.*
import s2d.backend.sdl2.Extras.*
import s2d.core.{Window, Input}
import s2d.math.*
import s2d.shapes.BasicRenderer
import scalanative.unsafe.*
import scalanative.unsigned.*
import scalanative.libc.stdlib

object Drawing:
  private var currentCustomShader: Option[Shader] = None
  private val projectionPtr: Ptr[GLfloat] =
    stdlib.malloc(sizeof[GLfloat] * 16.toUInt).asInstanceOf[Ptr[GLfloat]]
  private var cachedProjection: Array[Float] = Matrix4.ortho(0.0f, 800.0f, 600.0f, 0.0f, -1.0f, 1.0f)
  private var cachedProjLoc: Int = -1
  private var cachedColorLoc: Int = -1
  private var cachedTexLoc: Int = -1
  private var currentProgramId: UInt = 0.toUInt

  def clear(color: Color): Unit =
    glClearColor(color.rNorm, color.gNorm, color.bNorm, color.aNorm)
    glClear(GL_COLOR_BUFFER_BIT)

  def clear(r: Float, g: Float, b: Float, a: Float = 1.0f): Unit =
    glClearColor(r, g, b, a)
    glClear(GL_COLOR_BUFFER_BIT)

  def beginFrame(): Unit =
    if !Window.isWindowInitialized then
      throw new RuntimeException("Window not initialized!")

    Hot.poll() // hot reload

    Timing.updateDelta()

    updateProjection()
    currentProgramId = 0.toUInt

    Input.updateKeyStates()
    Input.updateMouseStates()
    Input.updateMousePosition()

    Zone {
      val event = stackalloc[SDL_Event]()

      if Window.eventWaitingEnabled then
        SDL_WaitEvent(event)
        processEvent(event)
        while SDL_PollEvent(event) != 0 do processEvent(event)
      else while SDL_PollEvent(event) != 0 do processEvent(event)
      end if
    }

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
  end beginFrame

  def endFrame(): Unit =
    if !Window.isWindowInitialized then
      throw new RuntimeException("Window not initialized!")

    BasicRenderer.flush()
    SDL_GL_SwapWindow(Window.windowHandle)
    Input.resetMouseWheel()
    Timing.waitForTargetFPS()

  def beginCamera(camera: Camera2D): Unit =
    if !Window.isWindowInitialized then return

    BasicRenderer.flush()

    val matrix = Matrix4.getCameraMatrix(camera, Window.width, Window.height)
    var i = 0; while i < 16 do {projectionPtr(i) = matrix(i); i+= 1}

  def endCamera(): Unit =
    if !Window.isWindowInitialized then return
    
    BasicRenderer.flush()
    updateProjection()

  def beginTexture(target: RenderTexture2D): Unit =
    if !Window.isWindowInitialized then return

    glBindFramebuffer(GL_FRAMEBUFFER, target.id.toUInt)
    glViewport(0, 0, target.texture.width.toUInt, target.texture.height.toUInt)
  end beginTexture

  def endTexture(): Unit =
    if !Window.isWindowInitialized then return

    glBindFramebuffer(GL_FRAMEBUFFER, 0.toUInt)
    glViewport(0, 0, Window.width.toUInt, Window.height.toUInt)
  end endTexture

  def beginShader(shader: Shader): Unit =
    if !Window.isWindowInitialized then return
    BasicRenderer.flush()
    Zone {
      cachedProjLoc  = GLEWHelper.glGetUniformLocation(shader.id.toUInt, toCString("uProjection"))
      cachedColorLoc = GLEWHelper.glGetUniformLocation(shader.id.toUInt, toCString("uColor"))
      cachedTexLoc   = GLEWHelper.glGetUniformLocation(shader.id.toUInt, toCString("uTexture"))
    }
    currentCustomShader = Some(shader)
    useProgram(shader.id.toUInt)

  def endShader(): Unit =
    if !Window.isWindowInitialized then return

    cachedProjLoc  = -1
    cachedColorLoc = -1
    cachedTexLoc   = -1
    currentCustomShader = None
    useProgram(0.toUInt)

  private[s2d] def useProgram(id: UInt): Unit =
    if id != currentProgramId then
      GLEWHelper.glUseProgram(id)
      currentProgramId = id

  def beginBlend(mode: BlendMode): Unit =
    if !Window.isWindowInitialized then return

    mode match
      case BlendMode.Alpha =>
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glBlendEquation(GL_FUNC_ADD)
      case BlendMode.Additive =>
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)
        glBlendEquation(GL_FUNC_ADD)
      case BlendMode.Multiplied =>
        glBlendFunc(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA)
        glBlendEquation(GL_FUNC_ADD)
      case BlendMode.AddColors =>
        glBlendFunc(GL_ONE, GL_ONE)
        glBlendEquation(GL_FUNC_ADD)
      case BlendMode.SubtractColors =>
        glBlendFunc(GL_ONE, GL_ONE)
        glBlendEquation(GL_FUNC_SUBTRACT)
      case BlendMode.AlphaPremultiply =>
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)
        glBlendEquation(GL_FUNC_ADD)
      case _ =>
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glBlendEquation(GL_FUNC_ADD)
    end match
  end beginBlend

  def endBlend(): Unit =
    if !Window.isWindowInitialized then return

    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    glBlendEquation(GL_FUNC_ADD)
  end endBlend

  def beginScissor(x: Int, y: Int, width: Int, height: Int): Unit =
    if !Window.isWindowInitialized then return

    glEnable(GL_SCISSOR_TEST)

    val flippedY = Window.windowHeight - (y + height)
    glScissor(x, flippedY, width.toUInt, height.toUInt)
  end beginScissor

  def endScissor(): Unit =
    if !Window.isWindowInitialized then return

    glDisable(GL_SCISSOR_TEST)

  def updateProjection(): Unit =
    cachedProjection = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
    var i = 0; while i < 16 do { projectionPtr(i) = cachedProjection(i); i += 1 }

  def getProjection(): Array[Float] = cachedProjection
  private[s2d] def getProjectionPtr: Ptr[GLfloat] = projectionPtr

  private[s2d] def getCustomProjLoc: Int  = cachedProjLoc
  private[s2d] def getCustomColorLoc: Int = cachedColorLoc
  private[s2d] def getCustomTexLoc: Int   = cachedTexLoc

  private def processEvent(event: Ptr[SDL_Event]): Unit =
    event.type_ match
      case SDL_QUIT =>
        Window.shouldClose = true
      case SDL_WINDOWEVENT =>
        val windowEvent = event.window
        windowEvent.event match
          case SDL_WINDOWEVENT_CLOSE =>
            Window.shouldClose = true
          case _ =>
            ()
        end match
      case SDL_KEYDOWN | SDL_KEYUP =>
        Input.processKeyEvent(event.key)
        if Input.checkExitKey() then
          Window.shouldClose = true
      case SDL_TEXTINPUT =>
        Input.processTextEvent(event.text)
      case SDL_MOUSEBUTTONDOWN | SDL_MOUSEBUTTONUP =>
        Input.processMouseEvent(event.button)
      case SDL_MOUSEWHEEL =>
        Input.processMouseWheelEvent(event)
      case _ =>
        ()

  private[s2d] def getCurrentShader: Option[Shader] = currentCustomShader
end Drawing
