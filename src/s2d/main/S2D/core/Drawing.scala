package S2D.core

import S2D.core.Window.*
import S2D.types.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.{GLFWErrorCallback, GLFWImage}
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.*

object Drawing:
  // DRAWING RELATED FUNCTIONS
  def ClearBackground(color: Color): Unit =
    glClearColor(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, color.a / 255.0f)
  def ClearBackground(r: Float, g: Float, b: Float, a: Float = 1.0f): Unit =
    glClearColor(r, g, b, a)
  def ClearBackgroundRGB(r: Int, g: Int, b: Int): Unit =
    ClearBackground(Color(r, g, b, 255))
  def BeginDrawing(): Unit =
    if !isWindowInitialized then
      throw new RuntimeException("Window not initialized!")

    if eventWaitingEnabled then
      glfwWaitEvents()
    else
      glfwPollEvents()

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
  def EndDrawing(): Unit =
    if !isWindowInitialized then
      throw new RuntimeException("Window not initialized!")

    glfwSwapBuffers(windowHandle)
  def BeginMode2D(camera: Camera2D): Unit =
    if !isWindowInitialized then return

    glMatrixMode(GL_PROJECTION)
    glPushMatrix()
    glLoadIdentity()

    glOrtho(0.0, windowWidth.toDouble, windowHeight.toDouble, 0.0, -1.0, 1.0)

    glMatrixMode(GL_MODELVIEW)
    glPushMatrix()
    glLoadIdentity()

    glTranslatef(camera.offset.x, camera.offset.y, 0.0f)
    glRotatef(camera.rotation, 0.0f, 0.0f, 1.0f)
    glScalef(camera.zoom, camera.zoom, 1.0f)
    glTranslatef(-camera.target.x, -camera.target.y, 0.0f)
  def EndMode2D(): Unit =
    if !isWindowInitialized then return

    glMatrixMode(GL_PROJECTION)
    glPopMatrix()

    glMatrixMode(GL_MODELVIEW)
    glPopMatrix()
  def BeginTextureMode(target: RenderTexture2D): Unit =
    if !isWindowInitialized then return

    glBindFramebuffer(GL_FRAMEBUFFER, target.id)
    glViewport(0, 0, target.texture.width, target.texture.height)
  def EndTextureMode(): Unit =
    if !isWindowInitialized then return

    glBindFramebuffer(GL_FRAMEBUFFER, 0)
    glViewport(0, 0, windowWidth, windowHeight)
  def BeginShaderMode(shader: Shader): Unit =
    if !isWindowInitialized then return

    glUseProgram(shader.id)
  def EndShaderMode(): Unit =
    if !isWindowInitialized then return

    glUseProgram(0)
  def BeginBlendMode(mode: Int): Unit =
    if !isWindowInitialized then return

    mode match
      case BlendMode.ALPHA =>
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glBlendEquation(GL_FUNC_ADD)
      case BlendMode.ADDITIVE =>
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)
        glBlendEquation(GL_FUNC_ADD)
      case BlendMode.MULTIPLIED =>
        glBlendFunc(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA)
        glBlendEquation(GL_FUNC_ADD)
      case BlendMode.ADD_COLORS =>
        glBlendFunc(GL_ONE, GL_ONE)
        glBlendEquation(GL_FUNC_ADD)
      case BlendMode.SUBTRACT_COLORS =>
        glBlendFunc(GL_ONE, GL_ONE)
        glBlendEquation(GL_FUNC_SUBTRACT)
      case BlendMode.ALPHA_PREMULTIPLY =>
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)
        glBlendEquation(GL_FUNC_ADD)
      case _ =>
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glBlendEquation(GL_FUNC_ADD)
  def EndBlendMode(): Unit =
    if !isWindowInitialized then return

    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    glBlendEquation(GL_FUNC_ADD)
  def BeginScissorMode(x: Int, y: Int, width: Int, height: Int): Unit =
    if !isWindowInitialized then return

    glEnable(GL_SCISSOR_TEST)

    val flippedY = windowHeight - (y + height)
    glScissor(x, flippedY, width, height)
  def EndScissorMode(): Unit =
    if !isWindowInitialized then return

    glDisable(GL_SCISSOR_TEST)
  def IsKeyDown(key: Int): Boolean =
    if !isWindowInitialized then return false
    glfwGetKey(windowHandle, key) == GLFW_PRESS
  def IsKeyEscape(): Boolean = IsKeyDown(GLFW_KEY_ESCAPE)

  object BlendMode:
    val ALPHA = 0
    val ADDITIVE = 1
    val MULTIPLIED = 2
    val ADD_COLORS = 3
    val SUBTRACT_COLORS = 4
    val ALPHA_PREMULTIPLY = 5
    val CUSTOM = 6
    val CUSTOM_SEPARATE = 7