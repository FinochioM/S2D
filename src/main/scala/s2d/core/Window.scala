package s2d.core

import s2d.types.*
import s2d.backend.sdl2.SDL.*
import s2d.backend.sdl2.Extras.*
import s2d.backend.gl.GL.*
import s2d.backend.gl.GLExtras.*
import s2d.backend.gl.GLEWHelper
import scalanative.unsafe.*
import scalanative.unsigned.*
import scala.util.boundary, boundary.break

object Window:
  var windowHandle: Ptr[SDL_Window] = null
  var windowWidth: Int = 0
  var windowHeight: Int = 0
  var windowTitle: String = ""
  var isWindowInitialized: Boolean = false
  var shouldClose: Boolean = false
  var windowResizedThisFrame: Boolean = false
  var sdlInitialized: Boolean = false
  var windowedX: Int = 0
  var windowedY: Int = 0
  var windowedWidth: Int = 0
  var windowedHeight: Int = 0
  var isBorderlessWindowed: Boolean = false
  var eventWaitingEnabled: Boolean = false
  var glContext: SDL_GLContext = null

  def create(width: Int, height: Int, title: String): Unit =
    if isWindowInitialized then
      throw new RuntimeException("Window already initialized!")

    if !sdlInitialized then
      if SDL_Init(SDL_INIT_VIDEO) < 0 then
        throw new RuntimeException("Unable to initialize SDL")
      sdlInitialized = true

    SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION.toUInt, 3)
    SDL_GL_SetAttribute(SDL_GL_CONTEXT_MINOR_VERSION.toUInt, 3)
    SDL_GL_SetAttribute(
      SDL_GL_CONTEXT_PROFILE_MASK.toUInt,
      SDL_GL_CONTEXT_PROFILE_CORE.toInt
    )
    SDL_GL_SetAttribute(SDL_GL_DOUBLEBUFFER.toUInt, 1)

    Zone {
      val titleCStr = toCString(title)
      windowHandle = SDL_CreateWindow(
        titleCStr,
        SDL_WINDOWPOS_CENTERED,
        SDL_WINDOWPOS_CENTERED,
        width,
        height,
        (SDL_WINDOW_OPENGL | SDL_WINDOW_SHOWN | SDL_WINDOW_RESIZABLE).toUInt
      )

      if windowHandle == null then
        System.err.println(fromCString(SDL_GetError()))
        throw new RuntimeException("Failed to create SDL Window")

      windowWidth = width
      windowHeight = height
      windowTitle = title
      windowedWidth = width
      windowedHeight = height

      val xPos = stackalloc[CInt]()
      val yPos = stackalloc[CInt]()
      SDL_GetWindowPosition(windowHandle, xPos, yPos)
      windowedX = !xPos
      windowedY = !yPos

      glContext = SDL_GL_CreateContext(windowHandle)
      if glContext == null then
        throw new RuntimeException("Failed to create OpenGL Context")

      SDL_GL_MakeCurrent(windowHandle, glContext)
      SDL_GL_SetSwapInterval(1)

      if !GLEWHelper.initializeGLEW() then
        throw new RuntimeException("Failed to initialize GLEW")

      glViewport(0, 0, width.toUInt, height.toUInt)
      glEnable(GL_BLEND)
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

      isWindowInitialized = true
    }
  end create

  def close(): Unit =
    if isWindowInitialized then
      if glContext != null then
        SDL_GL_DeleteContext(glContext)
        glContext = null

      if windowHandle != null then
        SDL_DestroyWindow(windowHandle)
        windowHandle = null

      isWindowInitialized = false

  def shouldCloseWindow(): Boolean =
    if !isWindowInitialized then return true
    shouldClose

  def isReady: Boolean = isWindowInitialized

  def isFullscreen: Boolean =
    if !isWindowInitialized then return false
    val flags = SDL_GetWindowFlags(windowHandle)
    (flags & (SDL_WINDOW_FULLSCREEN | SDL_WINDOW_FULLSCREEN_DESKTOP)) != 0.toUInt

  def isHidden: Boolean =
    if !isWindowInitialized then return false
    val flags = SDL_GetWindowFlags(windowHandle)
    (flags & SDL_WINDOW_SHOWN) == 0.toUInt

  def isMinimized: Boolean =
    if !isWindowInitialized then return false
    val flags = SDL_GetWindowFlags(windowHandle)
    (flags & SDL_WINDOW_MINIMIZED) != 0.toUInt

  def isMaximized: Boolean =
    if !isWindowInitialized then return false
    val flags = SDL_GetWindowFlags(windowHandle)
    (flags & SDL_WINDOW_MAXIMIZED) != 0.toUInt

  def isFocused: Boolean =
    if !isWindowInitialized then return false
    val flags = SDL_GetWindowFlags(windowHandle)
    (flags & SDL_WINDOW_INPUT_FOCUS) != 0.toUInt

  def isResized: Boolean =
    if !isWindowInitialized then return false
    val wasResized = windowResizedThisFrame
    windowResizedThisFrame = true
    wasResized
  end isResized

  def hasState(flag: WindowFlag): Boolean =
    if !isWindowInitialized then return false

    val flags = SDL_GetWindowFlags(windowHandle)

    flag match
      case WindowFlag.Resizable   => (flags & SDL_WINDOW_RESIZABLE) != 0.toUInt
      case WindowFlag.Undecorated => (flags & SDL_WINDOW_BORDERLESS) != 0.toUInt
      case WindowFlag.Transparent => false // I do not know if SDL has this.
      case WindowFlag.AlwaysOnTop => false // Same as Transparent.
      case WindowFlag.Maximized   => (flags & SDL_WINDOW_MAXIMIZED) != 0.toUInt
      case WindowFlag.Minimized   => (flags & SDL_WINDOW_MINIMIZED) != 0.toUInt
      case WindowFlag.Focused => (flags & SDL_WINDOW_INPUT_FOCUS) != 0.toUInt
      case WindowFlag.Visible => (flags & SDL_WINDOW_SHOWN) != 0.toUInt
      case WindowFlag.Fullscreen =>
        (flags & (SDL_WINDOW_FULLSCREEN | SDL_WINDOW_FULLSCREEN_DESKTOP)) != 0.toUInt
    end match
  end hasState

  def setState(flags: WindowFlag*): Unit =
    if !isWindowInitialized then return

    val combinedFlags = WindowFlag.combine(flags*)

    // Resizable and Undecorated cannot be changed once the window is created.

    if WindowFlag.contains(combinedFlags, WindowFlag.Maximized) then
      SDL_MaximizeWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Minimized) then
      SDL_MinimizeWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Focused) then
      SDL_RaiseWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Visible) then
      SDL_ShowWindow(windowHandle)

    // I dont know about Transparent and AlwaysOnTop on SDL2.
  end setState

  def clearState(flags: WindowFlag*): Unit =
    if !isWindowInitialized then return

    val combinedFlags = WindowFlag.combine(flags*)

    // Resizable and Undecorated cannot be changed once the window is created.

    if WindowFlag.contains(combinedFlags, WindowFlag.Maximized) then
      SDL_RestoreWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Minimized) then
      SDL_RestoreWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Visible) then
      SDL_HideWindow(windowHandle)

    // I dont know about Transparent and AlwaysOnTop on SDL2.
  end clearState

  def toggleFullscreen(): Unit =
    if !isWindowInitialized then return

    if isFullscreen then
      SDL_SetWindowFullscreen(windowHandle, 0.toUInt)
      SDL_SetWindowPosition(windowHandle, windowedX, windowedY)
      SDL_SetWindowSize(windowHandle, windowedWidth, windowedHeight)
    else
      val xPos = stackalloc[CInt]()
      val yPos = stackalloc[CInt]()
      val width = stackalloc[CInt]()
      val height = stackalloc[CInt]()

      SDL_GetWindowPosition(windowHandle, xPos, yPos)
      SDL_GetWindowSize(windowHandle, width, height)

      windowedX = !xPos
      windowedY = !yPos
      windowedWidth = !width
      windowedHeight = !height

      SDL_SetWindowFullscreen(windowHandle, SDL_WINDOW_FULLSCREEN_DESKTOP)
    end if
  end toggleFullscreen

  def toggleBorderless(): Unit =
    if !isWindowInitialized then return

    // As far as I can see SDL2 does not have a true borderless fullscreen mode.
    // So what I will do is toggle between a fullscreen mode and a normal windowed mode.

    if isBorderlessWindowed then
      SDL_SetWindowPosition(windowHandle, windowedX, windowedY)
      SDL_SetWindowSize(windowHandle, windowedWidth, windowedHeight)
      isBorderlessWindowed = false
    else
      val xPos = stackalloc[CInt]()
      val yPos = stackalloc[CInt]()
      val width = stackalloc[CInt]()
      val height = stackalloc[CInt]()

      SDL_GetWindowPosition(windowHandle, xPos, yPos)
      SDL_GetWindowSize(windowHandle, width, height)

      windowedX = !xPos
      windowedY = !yPos
      windowedWidth = !width
      windowedHeight = !height

      SDL_SetWindowFullscreen(windowHandle, SDL_WINDOW_FULLSCREEN_DESKTOP)
      isBorderlessWindowed = true
    end if
  end toggleBorderless

  def maximize(): Unit =
    if !isWindowInitialized then return

    val flags = SDL_GetWindowFlags(windowHandle)
    if (flags & SDL_WINDOW_RESIZABLE) != 0.toUInt then
      SDL_MaximizeWindow(windowHandle)
  end maximize

  def minimize(): Unit =
    if !isWindowInitialized then return

    SDL_MinimizeWindow(windowHandle)

  def restore(): Unit =
    if !isWindowInitialized then return

    SDL_RestoreWindow(windowHandle)

  def setIcon(image: Image): Unit =
    if !isWindowInitialized then return

    Zone {
      val surface = SDL_CreateRGBSurfaceFrom(
        image.data,
        image.width,
        image.height,
        32,
        image.width * 4,
        0x000000ff.toUInt,
        0x0000ff00.toUInt,
        0x00ff0000.toUInt,
        0xff000000.toUInt
      )

      if surface != null then
        SDL_SetWindowIcon(windowHandle, surface)
        SDL_FreeSurface(surface)
      else println("Failed to create SDL surface for window icon")
    }
  end setIcon

  def setTitle(title: String): Unit =
    if !isWindowInitialized then return

    Zone {
      val titleCStr = toCString(title)
      SDL_SetWindowTitle(windowHandle, titleCStr)
    }

    windowTitle = title
  end setTitle

  def setPosition(x: Int, y: Int): Unit =
    if !isWindowInitialized then return

    SDL_SetWindowPosition(windowHandle, x, y)

  def setMonitor(monitor: Int): Unit =
    if !isWindowInitialized then return

    val numDisplays = SDL_GetNumVideoDisplays()
    if monitor < 0 || monitor >= numDisplays then return

    Zone {
      val displayBounds = stackalloc[SDL_Rect]()

      if SDL_GetDisplayBounds(monitor, displayBounds) == 0 then
        SDL_SetWindowPosition(windowHandle, displayBounds.x, displayBounds.y)
    }
  end setMonitor

  def setMinSize(width: Int, height: Int): Unit =
    if !isWindowInitialized then return

    SDL_SetWindowMinimumSize(windowHandle, width, height)

  def setMaxSize(width: Int, height: Int): Unit =
    if !isWindowInitialized then return

    SDL_SetWindowMaximumSize(windowHandle, width, height)

  def setSize(width: Int, height: Int): Unit =
    if !isWindowInitialized then return

    SDL_SetWindowSize(windowHandle, width, height)
    windowWidth = width
    windowHeight = height
  end setSize

  def setFocused(): Unit =
    if !isWindowInitialized then return

    SDL_RaiseWindow(windowHandle)

  def setOpacity(opacity: Float): Unit =
    if !isWindowInitialized then return

    val clampedOpacity = Math.max(0.0f, Math.min(1.0f, opacity))
    SDL_SetWindowOpacity(windowHandle, clampedOpacity)
  end setOpacity

  def setClipboard(text: String): Unit =
    if !isWindowInitialized then return

    Zone {
      val textCStr = toCString(text)
      SDL_SetClipboardText(textCStr)
    }
  end setClipboard

  def handle: Ptr[SDL_Window] =
    if !isWindowInitialized then null
    else windowHandle

  def width: Int = windowWidth

  def height: Int = windowHeight

  def renderWidth: Int =
    if !isWindowInitialized then return 0

    Zone {
      val width = stackalloc[CInt]()
      val height = stackalloc[CInt]()
      SDL_GL_GetDrawableSize(windowHandle, width, height)
      !width
    }
  end renderWidth

  def renderHeight: Int =
    if !isWindowInitialized then return 0

    Zone {
      val width = stackalloc[CInt]()
      val height = stackalloc[CInt]()
      SDL_GL_GetDrawableSize(windowHandle, width, height)
      !height
    }
  end renderHeight

  def monitorCount: Int =
    SDL_GetNumVideoDisplays()

  def currentMonitor: Int =
    if !isWindowInitialized then return 0
    boundary:
      Zone {
        val windowX = stackalloc[CInt]()
        val windowY = stackalloc[CInt]()
        val windowWidth = stackalloc[CInt]()
        val windowHeight = stackalloc[CInt]()

        SDL_GetWindowPosition(windowHandle, windowX, windowY)
        SDL_GetWindowSize(windowHandle, windowWidth, windowHeight)

        val windowCenterX = !windowX + !windowWidth / 2
        val windowCenterY = !windowY + !windowHeight / 2

        val numDisplays = SDL_GetNumVideoDisplays()
        if numDisplays <= 0 then return 0

        for i <- 0 until numDisplays do
          val displayBounds = stackalloc[SDL_Rect]()

          if SDL_GetDisplayBounds(i, displayBounds) == 0 then
            val monitorRight = displayBounds.x + displayBounds.w
            val monitorBottom = displayBounds.y + displayBounds.h

            if windowCenterX >= displayBounds.x && windowCenterX < monitorRight &&
              windowCenterY >= displayBounds.y && windowCenterY < monitorBottom
            then break(i)
          end if
        end for

        break(0) // default to primary monitor
      }
  end currentMonitor

  def monitorWidth(monitor: Int): Int =
    val numDisplays = SDL_GetNumVideoDisplays()
    if monitor < 0 || monitor >= numDisplays then return 0

    Zone {
      val displayMode = stackalloc[SDL_DisplayMode]()

      if SDL_GetDesktopDisplayMode(monitor, displayMode) == 0 then displayMode.w
      else 0
    }
  end monitorWidth

  def monitorHeight(monitor: Int): Int =
    val numDisplays = SDL_GetNumVideoDisplays()
    if monitor < 0 || monitor >= numDisplays then return 0

    Zone {
      val displayMode = stackalloc[SDL_DisplayMode]()

      if SDL_GetDesktopDisplayMode(monitor, displayMode) == 0 then displayMode.h
      else 0
    }
  end monitorHeight

  def monitorPhysicalWidth(monitor: Int): Int =
    val numDisplays = SDL_GetNumVideoDisplays()
    if monitor < 0 || monitor >= numDisplays then return 0

    Zone {
      val ddpi = stackalloc[CFloat]()
      val hdpi = stackalloc[CFloat]()
      val vdpi = stackalloc[CFloat]()

      if SDL_GetDisplayDPI(monitor, ddpi, hdpi, vdpi) == 0 then
        val displayMode = stackalloc[SDL_DisplayMode]()
        if SDL_GetDesktopDisplayMode(monitor, displayMode) == 0 then
          // pixels to millimeters: (pixels * 25.4) / DPI
          ((displayMode.w * 25.4) / !hdpi).toInt
        else 0
      else 0
      end if
    }
  end monitorPhysicalWidth

  def monitorPhysicalHeight(monitor: Int): Int =
    val numDisplays = SDL_GetNumVideoDisplays()
    if monitor < 0 || monitor >= numDisplays then return 0

    Zone {
      val ddpi = stackalloc[CFloat]()
      val hdpi = stackalloc[CFloat]()
      val vdpi = stackalloc[CFloat]()

      if SDL_GetDisplayDPI(monitor, ddpi, hdpi, vdpi) == 0 then
        val displayMode = stackalloc[SDL_DisplayMode]()
        if SDL_GetDesktopDisplayMode(monitor, displayMode) == 0 then
          // pixels to millimeters: (pixels * 25.4) / DPI
          ((displayMode.h * 25.4) / !vdpi).toInt
        else 0
      else 0
      end if
    }
  end monitorPhysicalHeight

  def monitorRefreshRate(monitor: Int): Int =
    val numDisplays = SDL_GetNumVideoDisplays()
    if monitor < 0 || monitor >= numDisplays then return 0

    Zone {
      val displayMode = stackalloc[SDL_DisplayMode]()

      if SDL_GetDesktopDisplayMode(monitor, displayMode) == 0 then
        displayMode.refresh_rate
      else 0
    }
  end monitorRefreshRate

  def position: Vector2 =
    if !isWindowInitialized then return Vector2(0.0f, 0.0f)

    Zone {
      val xPos = stackalloc[CInt]()
      val yPos = stackalloc[CInt]()

      SDL_GetWindowPosition(windowHandle, xPos, yPos)
      Vector2((!xPos).toFloat, (!yPos).toFloat)
    }
  end position

  def scaleDPI: Vector2 =
    if !isWindowInitialized then return Vector2(1.0f, 1.0f)

    val currentDisplay = SDL_GetWindowDisplayIndex(windowHandle)
    if currentDisplay < 0 then return Vector2(1.0f, 1.0f)

    Zone {
      val ddpi = stackalloc[CFloat]()
      val hdpi = stackalloc[CFloat]()
      val vdpi = stackalloc[CFloat]()

      if SDL_GetDisplayDPI(currentDisplay, ddpi, hdpi, vdpi) == 0 then
        val standardDPI = 96.0f
        val scaleX = !hdpi / standardDPI
        val scaleY = !vdpi / standardDPI
        Vector2(scaleX, scaleY)
      else Vector2(1.0f, 1.0f)
      end if
    }
  end scaleDPI

  def monitorName(monitor: Int): String =
    val numDisplays = SDL_GetNumVideoDisplays()
    if monitor < 0 || monitor >= numDisplays then return ""

    val name = SDL_GetDisplayName(monitor)
    if name != null then fromCString(name) else ""
  end monitorName

  def getClipboardComplete: String =
    val clipboardText = SDL_GetClipboardText()
    if clipboardText != null then
      val result = fromCString(clipboardText)
      SDL_free(clipboardText.asInstanceOf[Ptr[Byte]])
      result
    else ""
    end if
  end getClipboardComplete

  def enableEventWaiting(): Unit =
    eventWaitingEnabled = true

  def disableEventWaiting(): Unit =
    eventWaitingEnabled = false
end Window
