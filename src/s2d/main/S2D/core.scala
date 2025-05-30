package S2D

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.{GLFWErrorCallback, GLFWImage}
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.*

object core:
  private var windowHandle: Long = NULL
  private var windowWidth: Int = 0
  private var windowHeight: Int = 0
  private var windowTitle: String = ""
  private var isWindowInitialized: Boolean = false
  private var shouldClose: Boolean = false
  private var windowResizedThisFrame: Boolean = false

  private var glfwInitialized: Boolean = false

  private var windowedX: Int = 0
  private var windowedY: Int = 0
  private var windowedWidth: Int = 0
  private var windowedHeight: Int = 0

  private var isBorderlessWindowed: Boolean = false

  private var eventWaitingEnabled: Boolean = false

  // WINDOW RELATED FUNCTIONS
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
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE)
    //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)

    windowHandle = glfwCreateWindow(width, height, title, NULL, NULL)
    if windowHandle == NULL then
      throw new RuntimeException("Failed to create GLFW window")

    windowWidth = width
    windowHeight = height
    windowTitle = title

    windowedWidth = width
    windowedHeight = height

    val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
    if videoMode != null then
      glfwSetWindowPos(
        windowHandle,
        (videoMode.width() - width) / 2,
        (videoMode.height() - height) / 2
      )

    val xPos = Array(0)
    val yPos = Array(0)
    glfwGetWindowPos(windowHandle, xPos, yPos)
    windowedX = xPos(0)
    windowedY = yPos(0)

    glfwMakeContextCurrent(windowHandle)
    glfwShowWindow(windowHandle)

    glfwSetWindowSizeCallback(windowHandle, (window, width, height) => {
      windowWidth = width
      windowHeight = height
      windowResizedThisFrame = true
      glViewport(0, 0, width, height)
    })

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
  def IsWindowFullscreen(): Boolean =
    if !isWindowInitialized then return false
    glfwGetWindowMonitor(windowHandle) != NULL
  def IsWindowHidden(): Boolean =
    if !isWindowInitialized then return false
    glfwGetWindowAttrib(windowHandle, GLFW_VISIBLE) == GLFW_FALSE
  def IsWindowMinimized(): Boolean =
    if !isWindowInitialized then return false
    glfwGetWindowAttrib(windowHandle, GLFW_ICONIFIED) == GLFW_TRUE
  def IsWindowMaximized(): Boolean =
    if !isWindowInitialized then return false
    glfwGetWindowAttrib(windowHandle, GLFW_MAXIMIZED) == GLFW_TRUE
  def IsWindowFocused(): Boolean =
    if !isWindowInitialized then return false
    glfwGetWindowAttrib(windowHandle, GLFW_FOCUSED) == GLFW_TRUE
  def IsWindowResized(): Boolean =
    if !isWindowInitialized then return false
    val wasResized = windowResizedThisFrame
    windowResizedThisFrame = false
    wasResized
  def IsWindowState(flag: Int): Boolean =
    if !isWindowInitialized then return false

    flag match
      case WindowFlags.RESIZABLE => glfwGetWindowAttrib(windowHandle, GLFW_RESIZABLE) == GLFW_TRUE
      case WindowFlags.UNDECORATED => glfwGetWindowAttrib(windowHandle, GLFW_DECORATED) == GLFW_FALSE
      case WindowFlags.TRANSPARENT => glfwGetWindowAttrib(windowHandle, GLFW_TRANSPARENT_FRAMEBUFFER) == GLFW_TRUE
      case WindowFlags.ALWAYS_ON_TOP => glfwGetWindowAttrib(windowHandle, GLFW_FLOATING) == GLFW_TRUE
      case WindowFlags.MAXIMIZED => glfwGetWindowAttrib(windowHandle, GLFW_MAXIMIZED) == GLFW_TRUE
      case WindowFlags.MINIMIZED => glfwGetWindowAttrib(windowHandle, GLFW_ICONIFIED) == GLFW_TRUE
      case WindowFlags.FOCUSED => glfwGetWindowAttrib(windowHandle, GLFW_FOCUSED) == GLFW_TRUE
      case WindowFlags.VISIBLE => glfwGetWindowAttrib(windowHandle, GLFW_VISIBLE) == GLFW_TRUE
      case WindowFlags.FULLSCREEN => glfwGetWindowMonitor(windowHandle) != NULL
      case _ => false
  def SetWindowState(flags: Int): Unit =
    if !isWindowInitialized then return

    if (flags & WindowFlags.RESIZABLE) != 0 then
      glfwSetWindowAttrib(windowHandle, GLFW_RESIZABLE, GLFW_TRUE)

    if (flags & WindowFlags.UNDECORATED) != 0 then
      glfwSetWindowAttrib(windowHandle, GLFW_DECORATED, GLFW_FALSE)

    if (flags & WindowFlags.TRANSPARENT) != 0 then
      glfwSetWindowAttrib(windowHandle, GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE)

    if (flags & WindowFlags.ALWAYS_ON_TOP) != 0 then
      glfwSetWindowAttrib(windowHandle, GLFW_FLOATING, GLFW_TRUE)

    if (flags & WindowFlags.MAXIMIZED) != 0 then
      glfwMaximizeWindow(windowHandle)

    if (flags & WindowFlags.MINIMIZED) != 0 then
      glfwIconifyWindow(windowHandle)

    if (flags & WindowFlags.FOCUSED) != 0 then
      glfwFocusWindow(windowHandle)

    if (flags & WindowFlags.VISIBLE) != 0 then
      glfwShowWindow(windowHandle)
  def ClearWindowState(flags: Int): Unit =
    if (flags & WindowFlags.RESIZABLE) != 0 then
      glfwSetWindowAttrib(windowHandle, GLFW_RESIZABLE, GLFW_FALSE)

    if (flags & WindowFlags.UNDECORATED) != 0 then
      glfwSetWindowAttrib(windowHandle, GLFW_DECORATED, GLFW_TRUE)

    if (flags & WindowFlags.TRANSPARENT) != 0 then
      glfwSetWindowAttrib(windowHandle, GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_FALSE)

    if (flags & WindowFlags.ALWAYS_ON_TOP) != 0 then
      glfwSetWindowAttrib(windowHandle, GLFW_FLOATING, GLFW_FALSE)

    if (flags & WindowFlags.MAXIMIZED) != 0 then
      glfwRestoreWindow(windowHandle)

    if (flags & WindowFlags.MINIMIZED) != 0 then
      glfwRestoreWindow(windowHandle)

    if (flags & WindowFlags.VISIBLE) != 0 then
      glfwHideWindow(windowHandle)
  def ToggleFullscreen(): Unit =
    if !isWindowInitialized then return

    if IsWindowFullscreen() then
      glfwSetWindowMonitor(windowHandle, NULL, windowedX, windowedY, windowedWidth, windowedHeight, GLFW_DONT_CARE)
    else
      val xPos = Array(0)
      val yPos = Array(0)
      val width = Array(0)
      val height = Array(0)
      glfwGetWindowPos(windowHandle, xPos, yPos)
      glfwGetWindowSize(windowHandle, width, height)

      windowedX = xPos(0)
      windowedY = yPos(0)
      windowedWidth = width(0)
      windowedHeight = height(0)

      val monitor = glfwGetPrimaryMonitor()
      val videoMode = glfwGetVideoMode(monitor)
      glfwSetWindowMonitor(windowHandle, monitor, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate())
  def ToggleBorderlessWindowed(): Unit =
    if !isWindowInitialized then return

    if isBorderlessWindowed then
      glfwSetWindowAttrib(windowHandle, GLFW_DECORATED, GLFW_TRUE)
      glfwSetWindowMonitor(windowHandle, NULL, windowedX, windowedY, windowedWidth, windowedHeight, GLFW_DONT_CARE)
      isBorderlessWindowed = false
    else
      val xPos = Array(0)
      val yPos = Array(0)
      val width = Array(0)
      val height = Array(0)
      glfwGetWindowPos(windowHandle, xPos, yPos)
      glfwGetWindowSize(windowHandle, width, height)

      windowedX = xPos(0)
      windowedY = yPos(0)
      windowedWidth = width(0)
      windowedHeight = height(0)

      glfwSetWindowAttrib(windowHandle, GLFW_DECORATED, GLFW_FALSE)
      val monitor = glfwGetPrimaryMonitor()
      val videoMode = glfwGetVideoMode(monitor)
      glfwSetWindowMonitor(windowHandle, NULL, 0, 0, videoMode.width(), videoMode.height(), GLFW_DONT_CARE)
      isBorderlessWindowed = true
  def MaximizeWindow(): Unit =
    if !isWindowInitialized then return

    if glfwGetWindowAttrib(windowHandle, GLFW_RESIZABLE) == GLFW_TRUE then
      glfwMaximizeWindow(windowHandle)
  def MinimizeWindow(): Unit =
    if !isWindowInitialized then return

    glfwIconifyWindow(windowHandle)
  def RestoreWindow(): Unit =
    if !isWindowInitialized then return

    glfwRestoreWindow(windowHandle)
  def SetWindowIcon(image: Image): Unit =
    if !isWindowInitialized then return

    val glfwImage = GLFWImage.malloc()
    glfwImage.width(image.width)
    glfwImage.height(image.height)
    glfwImage.pixels(image.data)

    val imageBuffer = GLFWImage.malloc(1)
    imageBuffer.put(0, glfwImage)

    glfwSetWindowIcon(windowHandle, imageBuffer)

    imageBuffer.free()
    glfwImage.free()
  def SetWindowIcons(images: Array[Image]): Unit =
    if !isWindowInitialized then return
    if images.isEmpty then return

    val imageBuffer = GLFWImage.malloc(images.length)

    for (i <- images.indices) {
      val glfwImage = GLFWImage.malloc()
      glfwImage.width(images(i).width)
      glfwImage.height(images(i).height)
      glfwImage.pixels(images(i).data)

      imageBuffer.put(i, glfwImage)
    }

    glfwSetWindowIcon(windowHandle, imageBuffer)

    for (i <- images.indices){
      imageBuffer.get(i).free()
    }

    imageBuffer.free()
  def SetWindowTitle(title: String): Unit =
    if !isWindowInitialized then return

    glfwSetWindowTitle(windowHandle, title)
    windowTitle = title
  def SetWindowPosition(x: Int, y: Int): Unit =
    if !isWindowInitialized then return

    glfwSetWindowPos(windowHandle, x, y)
  def SetWindowMonitor(monitor: Int): Unit =
    if !isWindowInitialized then return

    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then return

    val targetMonitor = monitors.get(monitor)
    val videoMode = glfwGetVideoMode(targetMonitor)

    if videoMode != null then
      glfwSetWindowMonitor(windowHandle, targetMonitor, 0, 0,
        videoMode.width(), videoMode.height(), videoMode.refreshRate())
  def SetWindowMinSize(width: Int, height: Int): Unit =
    if !isWindowInitialized then return

    glfwSetWindowSizeLimits(windowHandle, width, height, GLFW_DONT_CARE, GLFW_DONT_CARE)
  def SetWindowMaxSize(width: Int, height: Int): Unit =
    if !isWindowInitialized then return

    glfwSetWindowSizeLimits(windowHandle, GLFW_DONT_CARE, GLFW_DONT_CARE, width, height)
  def SetWindowSize(width: Int, height: Int): Unit =
    if !isWindowInitialized then return

    glfwSetWindowSize(windowHandle, width, height)
    windowWidth = width
    windowHeight = height
  def SetWindowOpacity(opacity: Float): Unit =
    if !isWindowInitialized then return

    val clampedOpacity = Math.max(0.0f, Math.min(1.0f, opacity))
    glfwSetWindowOpacity(windowHandle, opacity)
  def SetWindowFocused(): Unit =
    if !isWindowInitialized then return

    glfwFocusWindow(windowHandle)
  def SetClipboardText(text: String): Unit =
    if !isWindowInitialized then return

    glfwSetClipboardString(windowHandle, text)

  def GetWindowHandle(): Long =
    if !isWindowInitialized then return NULL

    windowHandle
  def GetScreenWidth(): Int = windowWidth
  def GetScreenHeight(): Int = windowHeight
  def GetRenderWidth(): Int =
    if !isWindowInitialized then return 0

    val width = Array(0)
    val height = Array(0)
    glfwGetFramebufferSize(windowHandle, width, height)
    width(0)
  def GetRenderHeight(): Int =
    if !isWindowInitialized then return 0

    val width = Array(0)
    val height = Array(0)
    glfwGetFramebufferSize(windowHandle, width, height)
    height(0)
  def GetMonitorCount(): Int =
    val monitors = glfwGetMonitors()
    if monitors == null then 0 else monitors.remaining()
  def GetCurrentMonitor(): Int =
    if !isWindowInitialized then return 0

    val windowX = Array(0)
    val windowY = Array(0)
    val windowWidth = Array(0)
    val windowHeight = Array(0)

    glfwGetWindowPos(windowHandle, windowX, windowY)
    glfwGetWindowSize(windowHandle, windowWidth, windowHeight)

    val windowCenterX = windowX(0) + windowWidth(0) / 2
    val windowCenterY = windowY(0) + windowHeight(0) / 2

    val monitors = glfwGetMonitors()
    if monitors == null then return 0

    for (i <- 0 until monitors.remaining()) {
      val monitor = monitors.get(i)
      val monitorX = Array(0)
      val monitorY = Array(0)
      glfwGetMonitorPos(monitor, monitorX, monitorY)

      val videoMode = glfwGetVideoMode(monitor)
      if videoMode != null then
        val monitorRight = monitorX(0) + videoMode.width()
        val monitorBottom = monitorY(0) + videoMode.height()

        if windowCenterX >= monitorX(0) && windowCenterX < monitorRight &&
          windowCenterY >= monitorY(0) && windowCenterY < monitorBottom then
          return i
    }

    0 // default to primary monitor
  def GetMonitorPosition(monitor: Int): Vector2 =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return Vector2(0.0f, 0.0f)

    val targetMonitor = monitors.get(monitor)
    val xPos = Array(0)
    val yPos = Array(0)

    glfwGetMonitorPos(targetMonitor, xPos, yPos)
    Vector2(xPos(0).toFloat, yPos(0).toFloat)
  def GetMonitorWidth(monitor: Int): Int =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return 0

    val targetMonitor = monitors.get(monitor)
    val videoMode = glfwGetVideoMode(targetMonitor)

    if videoMode != null then videoMode.width() else 0
  def GetMonitorHeight(monitor: Int): Int =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return 0

    val targetMonitor = monitors.get(monitor)
    val videoMode = glfwGetVideoMode(targetMonitor)

    if videoMode != null then videoMode.height() else 0
  def GetMonitorPhysicalWidth(monitor: Int): Int =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return 0

    val targetMonitor = monitors.get(monitor)
    val widthMM = Array(0)
    val heightMM = Array(0)

    glfwGetMonitorPhysicalSize(targetMonitor, widthMM, heightMM)
    widthMM(0)
  def GetMonitorPhysicalHeight(monitor: Int): Int =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return 0

    val targetMonitor = monitors.get(monitor)
    val widthMM = Array(0)
    val heightMM = Array(0)

    glfwGetMonitorPhysicalSize(targetMonitor, widthMM, heightMM)
    heightMM(0)
  def GetMonitorRefreshRate(monitor: Int): Int =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return 0

    val targetMonitor = monitors.get(monitor)
    val videoMode = glfwGetVideoMode(targetMonitor)

    if videoMode != null then videoMode.refreshRate() else 0
  def GetWindowPosition(): Vector2 =
    if !isWindowInitialized then return Vector2(0.0f, 0.0f)

    val xPos = Array(0)
    val yPos = Array(0)

    glfwGetWindowPos(windowHandle, xPos, yPos)
    Vector2(xPos(0).toFloat, yPos(0).toFloat)
  def GetWindowScaleDPI(): Vector2 =
    if !isWindowInitialized then return Vector2(1.0f, 1.0f)

    val xScale = Array(0.0f)
    val yScale = Array(0.0f)

    glfwGetWindowContentScale(windowHandle, xScale, yScale)
    Vector2(xScale(0), yScale(0))
  def GetMonitorName(monitor: Int): String =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return ""

    val targetMonitor = monitors.get(monitor)
    val name = glfwGetMonitorName(targetMonitor)

    if name != null then name else ""
  def GetClipboardText(): String =
    val clipboardContent = glfwGetClipboardString(windowHandle)
    if clipboardContent != null then clipboardContent else ""

  def EnableEventWaiting(): Unit =
    eventWaitingEnabled = true
  def DisableEventWaiting(): Unit =
    eventWaitingEnabled = false

  // CURSOR RELATED FUNCTIONS
  def ShowCursor(): Unit =
    if !isWindowInitialized then return

    glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
  def HideCursor(): Unit =
    if !isWindowInitialized then return

    glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN)
  def IsCursorHidden(): Boolean =
    if !isWindowInitialized then return false

    glfwGetInputMode(windowHandle, GLFW_CURSOR) == GLFW_CURSOR_HIDDEN
  def EnableCursor(): Unit =
    if !isWindowInitialized then return

    glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
  def DisableCursor(): Unit =
    if !isWindowInitialized then return

    glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
  def IsCursorOnScreen(): Boolean =
    if !isWindowInitialized then return false

    val xPos = Array(0.0)
    val yPos = Array(0.0)
    glfwGetCursorPos(windowHandle, xPos, yPos)

    val width = Array(0)
    val height = Array(0)
    glfwGetWindowSize(windowHandle, width, height)

    xPos(0) >= 0 && xPos(0) < width(0) && yPos(0) >= 0 && yPos(0) < height(0)

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
  object WindowFlags:
    val RESIZABLE = 0x00000001
    val UNDECORATED = 0x00000002
    val TRANSPARENT = 0x00000004
    val ALWAYS_ON_TOP = 0x00000008
    val MAXIMIZED = 0x00000010
    val MINIMIZED = 0x00000020
    val FOCUSED = 0x00000040
    val VISIBLE = 0x00000080
    val FULLSCREEN = 0x00000100