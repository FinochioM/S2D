package S2D.core

import S2D.core.Window.*

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