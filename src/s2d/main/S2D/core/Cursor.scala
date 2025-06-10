package S2D.core

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.*
import S2D.core.Window.*

object Cursor:
  // CURSOR RELATED FUNCTIONS
  def show(): Unit =
    if !isWindowInitialized then return

    glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
  def hide(): Unit =
    if !isWindowInitialized then return

    glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN)
  def isHidden: Boolean =
    if !isWindowInitialized then return false

    glfwGetInputMode(windowHandle, GLFW_CURSOR) == GLFW_CURSOR_HIDDEN
  def enable(): Unit =
    if !isWindowInitialized then return

    glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
  def disable(): Unit =
    if !isWindowInitialized then return

    glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
  def isOnScreen: Boolean =
    if !isWindowInitialized then return false

    val xPos = Array(0.0)
    val yPos = Array(0.0)
    glfwGetCursorPos(windowHandle, xPos, yPos)

    val width = Array(0)
    val height = Array(0)
    glfwGetWindowSize(windowHandle, width, height)

    xPos(0) >= 0 && xPos(0) < width(0) && yPos(0) >= 0 && yPos(0) < height(0)