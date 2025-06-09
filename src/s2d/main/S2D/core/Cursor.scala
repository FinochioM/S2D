package S2D.core

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.*

object Cursor:
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
