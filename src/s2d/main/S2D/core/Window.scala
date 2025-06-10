package S2D.core

import S2D.types.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.{GLFWErrorCallback, GLFWImage}
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.*

object Window:
  var windowHandle: Long = NULL
  var windowWidth: Int = 0
  var windowHeight: Int = 0
  var windowTitle: String = ""
  var isWindowInitialized: Boolean = false
  var shouldClose: Boolean = false
  var windowResizedThisFrame: Boolean = false
  var glfwInitialized: Boolean = false
  var windowedX: Int = 0
  var windowedY: Int = 0
  var windowedWidth: Int = 0
  var windowedHeight: Int = 0
  var isBorderlessWindowed: Boolean = false
  var eventWaitingEnabled: Boolean = false

  // WINDOW RELATED FUNCTIONS
  def create(width: Int, height: Int, title: String): Unit =
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
  def close(): Unit =
    if isWindowInitialized then
      glfwDestroyWindow(windowHandle)
      windowHandle = NULL
      isWindowInitialized = false
      println("S2D: Window closed")

    if glfwInitialized then
      glfwTerminate()
      glfwInitialized = false
  def shouldCloseWindow(): Boolean =
    if !isWindowInitialized then return true
    shouldClose || glfwWindowShouldClose(windowHandle)
  def isReady: Boolean = isWindowInitialized
  def isFullscreen: Boolean =
    if !isWindowInitialized then return false
    glfwGetWindowMonitor(windowHandle) != NULL
  def isHidden: Boolean =
    if !isWindowInitialized then return false
    glfwGetWindowAttrib(windowHandle, GLFW_VISIBLE) == GLFW_FALSE
  def isMinimized: Boolean =
    if !isWindowInitialized then return false
    glfwGetWindowAttrib(windowHandle, GLFW_ICONIFIED) == GLFW_TRUE
  def isMaximized: Boolean =
    if !isWindowInitialized then return false
    glfwGetWindowAttrib(windowHandle, GLFW_MAXIMIZED) == GLFW_TRUE
  def isFocused: Boolean =
    if !isWindowInitialized then return false
    glfwGetWindowAttrib(windowHandle, GLFW_FOCUSED) == GLFW_TRUE
  def isResized: Boolean =
    if !isWindowInitialized then return false
    val wasResized = windowResizedThisFrame
    windowResizedThisFrame = false
    wasResized
  def hasState(flag: WindowFlag): Boolean =
    if !isWindowInitialized then return false

    flag match
      case WindowFlag.Resizable => glfwGetWindowAttrib(windowHandle, GLFW_RESIZABLE) == GLFW_TRUE
      case WindowFlag.Undecorated => glfwGetWindowAttrib(windowHandle, GLFW_DECORATED) == GLFW_FALSE
      case WindowFlag.Transparent => glfwGetWindowAttrib(windowHandle, GLFW_TRANSPARENT_FRAMEBUFFER) == GLFW_TRUE
      case WindowFlag.AlwaysOnTop => glfwGetWindowAttrib(windowHandle, GLFW_FLOATING) == GLFW_TRUE
      case WindowFlag.Maximized => glfwGetWindowAttrib(windowHandle, GLFW_MAXIMIZED) == GLFW_TRUE
      case WindowFlag.Minimized => glfwGetWindowAttrib(windowHandle, GLFW_ICONIFIED) == GLFW_TRUE
      case WindowFlag.Focused => glfwGetWindowAttrib(windowHandle, GLFW_FOCUSED) == GLFW_TRUE
      case WindowFlag.Visible => glfwGetWindowAttrib(windowHandle, GLFW_VISIBLE) == GLFW_TRUE
      case WindowFlag.Fullscreen => glfwGetWindowMonitor(windowHandle) != NULL
  def setState(flags: WindowFlag*): Unit =
    if !isWindowInitialized then return

    val combinedFlags = WindowFlag.combine(flags: _*)

    if WindowFlag.contains(combinedFlags, WindowFlag.Resizable) then
      glfwSetWindowAttrib(windowHandle, GLFW_RESIZABLE, GLFW_TRUE)

    if WindowFlag.contains(combinedFlags, WindowFlag.Undecorated) then
      glfwSetWindowAttrib(windowHandle, GLFW_DECORATED, GLFW_FALSE)

    if WindowFlag.contains(combinedFlags, WindowFlag.Transparent) then
      glfwSetWindowAttrib(windowHandle, GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE)

    if WindowFlag.contains(combinedFlags, WindowFlag.AlwaysOnTop) then
      glfwSetWindowAttrib(windowHandle, GLFW_FLOATING, GLFW_TRUE)

    if WindowFlag.contains(combinedFlags, WindowFlag.Maximized)then
      glfwMaximizeWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Minimized) then
      glfwIconifyWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Focused) then
      glfwFocusWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Visible)then
      glfwShowWindow(windowHandle)
  def clearState(flags: WindowFlag*): Unit =
    if !isWindowInitialized then return

    val combinedFlags = WindowFlag.combine(flags: _*)

    if WindowFlag.contains(combinedFlags, WindowFlag.Resizable) then
      glfwSetWindowAttrib(windowHandle, GLFW_RESIZABLE, GLFW_FALSE)

    if WindowFlag.contains(combinedFlags, WindowFlag.Undecorated) then
      glfwSetWindowAttrib(windowHandle, GLFW_DECORATED, GLFW_TRUE)

    if WindowFlag.contains(combinedFlags, WindowFlag.Transparent) then
      glfwSetWindowAttrib(windowHandle, GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_FALSE)

    if WindowFlag.contains(combinedFlags, WindowFlag.AlwaysOnTop) then
      glfwSetWindowAttrib(windowHandle, GLFW_FLOATING, GLFW_FALSE)

    if WindowFlag.contains(combinedFlags, WindowFlag.Maximized) then
      glfwRestoreWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Minimized) then
      glfwRestoreWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Visible) then
      glfwHideWindow(windowHandle)
  def toggleFullscreen(): Unit =
    if !isWindowInitialized then return

    if isFullscreen then
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
  def toggleBorderless(): Unit =
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
  def maximize(): Unit =
    if !isWindowInitialized then return

    if glfwGetWindowAttrib(windowHandle, GLFW_RESIZABLE) == GLFW_TRUE then
      glfwMaximizeWindow(windowHandle)
  def minimize(): Unit =
    if !isWindowInitialized then return

    glfwIconifyWindow(windowHandle)
  def restore(): Unit =
    if !isWindowInitialized then return

    glfwRestoreWindow(windowHandle)
  def setIcon(image: Image): Unit =
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
  def setIcons(images: Array[Image]): Unit =
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

    for (i <- images.indices) {
      imageBuffer.get(i).free()
    }

    imageBuffer.free()
  def setTitle(title: String): Unit =
    if !isWindowInitialized then return

    glfwSetWindowTitle(windowHandle, title)
    windowTitle = title
  def setPosition(x: Int, y: Int): Unit =
    if !isWindowInitialized then return

    glfwSetWindowPos(windowHandle, x, y)
  def setMonitor(monitor: Int): Unit =
    if !isWindowInitialized then return

    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then return

    val targetMonitor = monitors.get(monitor)
    val videoMode = glfwGetVideoMode(targetMonitor)

    if videoMode != null then
      glfwSetWindowMonitor(windowHandle, targetMonitor, 0, 0,
        videoMode.width(), videoMode.height(), videoMode.refreshRate())
  def setMinSize(width: Int, height: Int): Unit =
    if !isWindowInitialized then return

    glfwSetWindowSizeLimits(windowHandle, width, height, GLFW_DONT_CARE, GLFW_DONT_CARE)
  def setMaxSize(width: Int, height: Int): Unit =
    if !isWindowInitialized then return

    glfwSetWindowSizeLimits(windowHandle, GLFW_DONT_CARE, GLFW_DONT_CARE, width, height)
  def setSize(width: Int, height: Int): Unit =
    if !isWindowInitialized then return

    glfwSetWindowSize(windowHandle, width, height)
    windowWidth = width
    windowHeight = height
  def setOpacity(opacity: Float): Unit =
    if !isWindowInitialized then return

    val clampedOpacity = Math.max(0.0f, Math.min(1.0f, opacity))
    glfwSetWindowOpacity(windowHandle, opacity)
  def setFocused(): Unit =
    if !isWindowInitialized then return

    glfwFocusWindow(windowHandle)
  def setClipboard(text: String): Unit =
    if !isWindowInitialized then return

    glfwSetClipboardString(windowHandle, text)
  def handle: Long =
    if !isWindowInitialized then return NULL

    windowHandle
  def width: Int = windowWidth
  def height: Int = windowHeight
  def renderWidth: Int =
    if !isWindowInitialized then return 0

    val width = Array(0)
    val height = Array(0)
    glfwGetFramebufferSize(windowHandle, width, height)
    width(0)
  def renderHeight: Int =
    if !isWindowInitialized then return 0

    val width = Array(0)
    val height = Array(0)
    glfwGetFramebufferSize(windowHandle, width, height)
    height(0)
  def monitorCount: Int =
    val monitors = glfwGetMonitors()
    if monitors == null then 0 else monitors.remaining()
  def currentMonitor: Int =
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
  def monitorPosition(monitor: Int): Vector2 =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return Vector2(0.0f, 0.0f)

    val targetMonitor = monitors.get(monitor)
    val xPos = Array(0)
    val yPos = Array(0)

    glfwGetMonitorPos(targetMonitor, xPos, yPos)
    Vector2(xPos(0).toFloat, yPos(0).toFloat)
  def monitorWidth(monitor: Int): Int =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return 0

    val targetMonitor = monitors.get(monitor)
    val videoMode = glfwGetVideoMode(targetMonitor)

    if videoMode != null then videoMode.width() else 0
  def monitorHeight(monitor: Int): Int =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return 0

    val targetMonitor = monitors.get(monitor)
    val videoMode = glfwGetVideoMode(targetMonitor)

    if videoMode != null then videoMode.height() else 0
  def monitorPhysicalWidth(monitor: Int): Int =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return 0

    val targetMonitor = monitors.get(monitor)
    val widthMM = Array(0)
    val heightMM = Array(0)

    glfwGetMonitorPhysicalSize(targetMonitor, widthMM, heightMM)
    widthMM(0)
  def monitorPhysicalHeight(monitor: Int): Int =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return 0

    val targetMonitor = monitors.get(monitor)
    val widthMM = Array(0)
    val heightMM = Array(0)

    glfwGetMonitorPhysicalSize(targetMonitor, widthMM, heightMM)
    heightMM(0)
  def monitorRefreshRate(monitor: Int): Int =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return 0

    val targetMonitor = monitors.get(monitor)
    val videoMode = glfwGetVideoMode(targetMonitor)

    if videoMode != null then videoMode.refreshRate() else 0
  def position: Vector2 =
    if !isWindowInitialized then return Vector2(0.0f, 0.0f)

    val xPos = Array(0)
    val yPos = Array(0)

    glfwGetWindowPos(windowHandle, xPos, yPos)
    Vector2(xPos(0).toFloat, yPos(0).toFloat)
  def scaleDPI: Vector2 =
    if !isWindowInitialized then return Vector2(1.0f, 1.0f)

    val xScale = Array(0.0f)
    val yScale = Array(0.0f)

    glfwGetWindowContentScale(windowHandle, xScale, yScale)
    Vector2(xScale(0), yScale(0))
  def monitorName(monitor: Int): String =
    val monitors = glfwGetMonitors()
    if monitors == null || monitor < 0 || monitor >= monitors.remaining() then
      return ""

    val targetMonitor = monitors.get(monitor)
    val name = glfwGetMonitorName(targetMonitor)

    if name != null then name else ""
  def getClipboard: String =
    val clipboardContent = glfwGetClipboardString(windowHandle)
    if clipboardContent != null then clipboardContent else ""
  def enableEventWaiting(): Unit =
    eventWaitingEnabled = true
  def disableEventWaiting(): Unit =
    eventWaitingEnabled = false