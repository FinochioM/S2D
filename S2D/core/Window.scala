package S2D.core

import S2D.types.*

import sdl2.SDL._
import sdl2.Extras._
import gl.GL._
import gl.Extras._
import scalanative.unsafe._
import scalanative.unsigned._

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
    SDL_GL_SetAttribute(SDL_GL_CONTEXT_PROFILE_MASK.toUInt, SDL_GL_CONTEXT_PROFILE_COMPATIBILITY.toInt)
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

      glViewport(0, 0, width.toUInt, height.toUInt)
      glEnable(GL_BLEND)
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

      isWindowInitialized = true
    }

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

  def hasState(flag: WindowFlag): Boolean =
    if !isWindowInitialized then return false

    val flags = SDL_GetWindowFlags(windowHandle)

    flag match
      case WindowFlag.Resizable => (flags & SDL_WINDOW_RESIZABLE) != 0.toUInt
      case WindowFlag.Undecorated => (flags & SDL_WINDOW_BORDERLESS) != 0.toUInt
      case WindowFlag.Transparent => false // I do not know if SDL has this.
      case WindowFlag.AlwaysOnTop => false // Same as Transparent.
      case WindowFlag.Maximized => (flags & SDL_WINDOW_MAXIMIZED) != 0.toUInt
      case WindowFlag.Minimized => (flags & SDL_WINDOW_MINIMIZED) != 0.toUInt
      case WindowFlag.Focused => (flags & SDL_WINDOW_INPUT_FOCUS) != 0.toUInt
      case WindowFlag.Visible => (flags & SDL_WINDOW_SHOWN) != 0.toUInt
      case WindowFlag.Fullscreen => (flags & (SDL_WINDOW_FULLSCREEN | SDL_WINDOW_FULLSCREEN_DESKTOP)) != 0.toUInt

  def setState(flags: WindowFlag*): Unit =
    if !isWindowInitialized then return

    val combinedFlags = WindowFlag.combine(flags: _*)

    // Resizable and Undecorated cannot be changed once the window is created.

    if WindowFlag.contains(combinedFlags, WindowFlag.Maximized) then
      SDL_MaximizeWindow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Minimized) then
      SDL_MinimizeWIndow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Focused) then
      SDL_RaiseWIndow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Visible) then
      SDL_ShowWIndow(windowHandle)

    // I dont know about Transparent and AlwaysOnTop on SDL2.

  def clearState(flags: WindowFlag*): Unit =
    if !isWindowInitialized then return

    val combinedFlags = WindowFlag.combine(flags: _*)

    // Resizable and Undecorated cannot be changed once the window is created.

    if WindowFlag.contains(combinedFlags, WindowFlag.Maximized) then
      SDL_RestoreWIndow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Minimized) then
      SDL_RestoreWIndow(windowHandle)

    if WindowFlag.contains(combinedFlags, WindowFlag.Visible) then
      SDL_HideWIndow(windowHandle)

    // I dont know about Transparent and AlwaysOnTop on SDL2.

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

  def maximize(): Unit =
    if !isWindowInitialized then return

    val flags = SDL_GetWindowFlags(windowHandle)
    if (flags & SDL_WINDOW_RESIZABLE) != 0.toUInt then
      SDL_MaximzeWIndow(windowHandle)

  def minimize(): Unit =
    if !isWindowInitialized then return

    SDL_MinimizeWIndow(windowHandle)

  def restore(): Unit =
    if !isWindowInitialized then return

    SDL_RestoreWIndow(windowHandle)