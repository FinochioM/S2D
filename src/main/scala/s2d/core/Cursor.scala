package s2d.core

import s2d.gl.GL.*
import s2d.gl.GLExtras.*
import s2d.types.*
import s2d.sdl2.SDL.*
import s2d.sdl2.Extras.*
import s2d.core.Window.*
import scalanative.unsafe.*
import scalanative.unsigned.*

object Cursor:
  def show(): Unit =
    if !Window.isWindowInitialized then return

    SDL_ShowCursor(1)

  def hide(): Unit =
    if !Window.isWindowInitialized then return

    SDL_ShowCursor(0)

  def isHidden: Boolean =
    if !Window.isWindowInitialized then return false

    SDL_ShowCursor(-1) == 0

  def enable(): Unit =
    if !Window.isWindowInitialized then return

    SDL_ShowCursor(1)

  def disable(): Unit =
    if !Window.isWindowInitialized then return

    SDL_SetRelativeMouseMode(1.toUInt)

  def isOnScreen: Boolean =
    if !Window.isWindowInitialized then return false

    Zone {
      val xPos = stackalloc[CInt]()
      val yPos = stackalloc[CInt]()
      SDL_GetMouseState(xPos, yPos)

      val width = stackalloc[CInt]()
      val height = stackalloc[CInt]()
      SDL_GetWindowSize(Window.windowHandle, width, height)

      val x = !xPos
      val y = !yPos
      val w = !width
      val h = !height

      x >= 0 && x < w && y >= 0 && y < h
    }
  end isOnScreen
end Cursor
