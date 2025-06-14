package S2D.core

import gl.GL._
import gl.GLExtras._
import S2D.types.*
import sdl2.SDL._
import sdl2.Extras._
import S2D.core.Window.*
import scalanative.unsafe._
import scalanative.unsigned._

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