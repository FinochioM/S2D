package S2D

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.*

object core:
  private var windowHandle: Long = NULL
  private var windowWidth: Int = 0
  private var windowHeight: Int = 0
  private var windowTitle: String = ""
  private var isWindowInitialized: Boolean = false
  private var shouldClose: Boolean = false

  private var glfwInitialized: Boolean = false

  /**
   * Initialize window and OpenGL context
   * @param width Window width in pixels
   * @param height Window height in pixels
   * @param title Window title
   */
  def InitWindow(width: Int, height: Int, title: String): Unit =
    if isWindowInitialized then
      throw new RuntimeException("Window already initialized!")

    if !glfwInitialized then
      GLFWErrorCallback.createPrint(System.err).set()

      if !glfwInit() then
        throw new RuntimeException("Unable to initialize GLFW")

      glfwInitialized = true

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)

    windowHandle = glfwCreateWindow(width, height, title, NULL, NULL)
    if windowHandle == NULL then
      throw new RuntimeException("Failed to create GLFW window")

    windowWidth = width
    windowHeight = height
    windowTitle = title

    val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
    if videoMode != null then
      glfwSetWindowPos(
        windowHandle,
        (videoMode.width() - width) / 2,
        (videoMode.height() - height) / 2
      )

    glfwMakeContextCurrent(windowHandle)
    glfwShowWindow(windowHandle)

    glfwSwapInterval(1)

    GL.createCapabilities()

    glViewport(0, 0, width, height)

    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

    isWindowInitialized = true
    println(s"S2D: Window initialized - ${width}x${height} '${title}'")

  def CloseWindow(): Unit =
    if isWindowInitialized then
      glfwDestroyWindow(windowHandle)
      windowHandle = NULL
      isWindowInitialized = false
      println("S2D: Window closed")

    if glfwInitialized then
      glfwTerminate()
      glfwInitialized = false

  def WindowShouldClose(): Boolean =
    if !isWindowInitialized then return true
    shouldClose || glfwWindowShouldClose(windowHandle)

  def IsWindowReady(): Boolean = isWindowInitialized

  def GetScreenWidth(): Int = windowWidth

  def GetScreenHeight(): Int = windowHeight

  def BeginDrawing(): Unit =
    if !isWindowInitialized then
      throw new RuntimeException("Window not initialized!")

    glfwPollEvents()

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

  def EndDrawing(): Unit =
    if !isWindowInitialized then
      throw new RuntimeException("Window not initialized!")

    glfwSwapBuffers(windowHandle)

  def ClearBackground(r: Float, g: Float, b: Float, a: Float = 1.0f): Unit =
    glClearColor(r, g, b, a)

  def ClearBackground(color: (Float, Float, Float)): Unit =
    ClearBackground(color._1, color._2, color._3)

  def ClearBackgroundRGB(r: Int, g: Int, b: Int): Unit =
    ClearBackground(r / 255.0f, g / 255.0f, b / 255.0f)

  def IsKeyDown(key: Int): Boolean =
    if !isWindowInitialized then return false
    glfwGetKey(windowHandle, key) == GLFW_PRESS

  def IsKeyEscape(): Boolean = IsKeyDown(GLFW_KEY_ESCAPE)

  object Keys:
    val ESCAPE = GLFW_KEY_ESCAPE
    val SPACE = GLFW_KEY_SPACE
    val ENTER = GLFW_KEY_ENTER
    val A = GLFW_KEY_A
    val B = GLFW_KEY_B
    val C = GLFW_KEY_C
    val D = GLFW_KEY_D
    val W = GLFW_KEY_W
    val S = GLFW_KEY_S
    val UP = GLFW_KEY_UP
    val DOWN = GLFW_KEY_DOWN
    val LEFT = GLFW_KEY_LEFT
    val RIGHT = GLFW_KEY_RIGHT