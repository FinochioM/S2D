package s2d.core

import s2d.backend.gl.GL.*
import s2d.backend.gl.GLExtras.*
import s2d.backend.gl.GLEWHelper
import s2d.types.*
import s2d.backend.sdl2.SDL.*
import s2d.backend.sdl2.Extras.*
import s2d.core.{Window, Input}
import scalanative.unsafe.*
import scalanative.unsigned.*

object Drawing:
  private var currentCustomShader: Option[Shader] = None

  def clear(color: Color): Unit =
    glClearColor(color.rNorm, color.gNorm, color.bNorm, color.aNorm)
    glClear(GL_COLOR_BUFFER_BIT)

  def clear(r: Float, g: Float, b: Float, a: Float = 1.0f): Unit =
    glClearColor(r, g, b, a)
    glClear(GL_COLOR_BUFFER_BIT)

  def clearRGB(r: Int, g: Int, b: Int): Unit =
    clear(Color(r, g, b, 255))

  def beginFrame(): Unit =
    if !Window.isWindowInitialized then
      throw new RuntimeException("Window not initialized!")

    Input.updateKeyStates()

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

    SDL_GL_SwapWindow(Window.windowHandle)
  end endFrame

  def beginCamera(camera: Camera2D): Unit =
    if !Window.isWindowInitialized then return

    glMatrixMode(GL_PROJECTION)
    glPushMatrix()
    glLoadIdentity()

    glOrtho(
      0.0,
      Window.windowWidth.toDouble,
      Window.windowHeight.toDouble,
      0.0,
      -1.0,
      1.0
    )

    glMatrixMode(GL_MODELVIEW)
    glPushMatrix()
    glLoadIdentity()

    glTranslatef(camera.offset.x, camera.offset.y, 0.0f)
    glRotatef(camera.rotation, 0.0f, 0.0f, 1.0f)
    glScalef(camera.zoom, camera.zoom, 1.0f)
    glTranslatef(-camera.target.x, -camera.target.y, 0.0f)
  end beginCamera

  def endCamera(): Unit =
    if !Window.isWindowInitialized then return

    glMatrixMode(GL_PROJECTION)
    glPopMatrix()

    glMatrixMode(GL_MODELVIEW)
    glPopMatrix()
  end endCamera

  def beginTexture(target: RenderTexture2D): Unit =
    if !Window.isWindowInitialized then return

    glBindFramebuffer(GL_FRAMEBUFFER, target.id.toUInt)
    glViewport(0, 0, target.texture.width.toUInt, target.texture.height.toUInt)
  end beginTexture

  def endTexture(): Unit =
    if !Window.isWindowInitialized then return

    glBindFramebuffer(GL_FRAMEBUFFER, 0.toUInt)
    glViewport(0, 0, Window.windowWidth.toUInt, Window.windowHeight.toUInt)
  end endTexture

  def beginShader(shader: Shader): Unit =
    if !Window.isWindowInitialized then return

    currentCustomShader = Some(shader)
    GLEWHelper.glUseProgram(shader.id.toUInt)

  def endShader(): Unit =
    if !Window.isWindowInitialized then return

    currentCustomShader = None
    GLEWHelper.glUseProgram(0.toUInt)

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
      case SDL_TEXTINPUT =>
        Input.processTextEvent(event.text)
      case _ =>
        ()

  def getCurrentShader: Option[Shader] = currentCustomShader
end Drawing
