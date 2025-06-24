package s2d.core

import s2d.types.{Key, MouseButton, Vector2}
import s2d.backend.sdl2.SDL.* 
import s2d.backend.sdl2.Extras.* 
import scalanative.unsafe.* 
import scalanative.unsigned.*
import scala.collection.mutable

object Input:
    private val currentKeyStates = mutable.Set[Key]()
    private val previousKeyStates = mutable.Set[Key]()
    private val keyPressQueue = mutable.Queue[Key]()
    private val charPressQueue = mutable.Queue[Int]()
    private var exitKey: Key = Key.Escape
    private val currentMouseStates = mutable.Set[MouseButton]()
    private val previousMouseStates = mutable.Set[MouseButton]()
    private var mouseX: Int = 0
    private var mouseY: Int = 0
    private var previousMouseX: Int = 0
    private var previousMouseY: Int = 0
    private var mouseOffsetX: Int = 0
    private var mouseOffsetY: Int = 0
    private var mouseScaleX: Float = 1.0f
    private var mouseScaleY: Float = 1.0f
    private var mouseWheelX: Float = 0.0f
    private var mouseWheelY: Float = 0.0f

    private[core] def processKeyEvent(event: Ptr[SDL_KeyboardEvent]): Unit =
      val keycode = event.keysym.sym
      val key = Key.fromKeycode(keycode)
      val isPressed = event.state == SDL_PRESSED.toUByte
        
      if isPressed then
        if !currentKeyStates.contains(key) then
          keyPressQueue.enqueue(key)
        currentKeyStates += key
      else
        currentKeyStates -= key
    end processKeyEvent

    private[core] def processTextEvent(event: Ptr[SDL_TextInputEvent]): Unit =
        val textBytes = event.text
        
        var i = 0
        while i < 32 && textBytes(i) != 0 do // do 32 because SDL_TEXTINPUTEVENT_TEXT_SIZE is 32
            val byte = textBytes(i).toInt & 0xFF
            if byte == 0 then return
            
            if (byte & 0x80) == 0 then
                charPressQueue.enqueue(byte)
                i += 1
            else
                i += 1
    end processTextEvent

    private[core] def updateKeyStates(): Unit =
        previousKeyStates.clear()
        previousKeyStates ++= currentKeyStates
    end updateKeyStates

    private[core] def processMouseEvent(event: Ptr[SDL_MouseButtonEvent]): Unit =
        val button = MouseButton.fromValue(event.button.toInt)
        val isPressed = event.state == SDL_PRESSED.toUByte

        if isPressed then
            currentMouseStates += button
        else
            currentMouseStates -= button
    end processMouseEvent

    private[core] def updateMouseStates(): Unit =
        previousMouseStates.clear()
        previousMouseStates ++= currentMouseStates
    end updateMouseStates

    private[core] def updateMousePosition(): Unit =
        if !Window.isWindowInitialized then return

        previousMouseX = mouseX
        previousMouseY = mouseY

        Zone {
            val x = stackalloc[CInt]()
            val y = stackalloc[CInt]()
            SDL_GetMouseState(x, y)
            mouseX = !x
            mouseY = !y
        }
    end updateMousePosition
    
    private[core] def processMouseWheelEvent(event: Ptr[SDL_Event]): Unit =
        val eventData = event.asInstanceOf[Ptr[Byte]]
        val x = !(event + 16).asInstanceOf[Ptr[Int]]
        val y = !(event + 20).asInstanceOf[Ptr[Int]]

        mouseWheelX = x.toFloat
        mouseWheelY = y.toFloat
    end processMouseWheelEvent

    private[core] def resetMouseWheel(): Unit =
        mouseWheelX = 0.0f
        mouseWheelY = 0.0f
    end resetMouseWheel

    private[core] def checkExitKey(): Boolean =
        isKeyPressed(exitKey)
    end checkExitKey

    def setExitKey(key: Key): Unit =
        exitKey = key
    end setExitKey

    def isKeyPressed(key: Key): Boolean =
        currentKeyStates.contains(key) && !previousKeyStates.contains(key)
    
    def isKeyDown(key: Key): Boolean =
        currentKeyStates.contains(key)
    
    def isKeyReleased(key: Key): Boolean =
        !currentKeyStates.contains(key) && previousKeyStates.contains(key)

    def isKeyUp(key: Key): Boolean =
        !currentKeyStates.contains(key)

    def getKeyPressed(): Key =
        if keyPressQueue.nonEmpty then
            keyPressQueue.dequeue()
        else
            Key.Unknown
    end getKeyPressed

    def getCharPressed(): Int =
        if charPressQueue.nonEmpty then
            charPressQueue.dequeue()
        else
            0
    end getCharPressed

    def isMouseButtonPressed(button: MouseButton): Boolean =
        currentMouseStates.contains(button) && !previousMouseStates.contains(button)

    def isMouseButtonDown(button: MouseButton): Boolean =
        currentMouseStates.contains(button)

    def isMouseButtonReleased(button: MouseButton): Boolean =
        !currentMouseStates.contains(button) && previousMouseStates.contains(button)

    def isMouseButtonUp(button: MouseButton): Boolean =
        !currentMouseStates.contains(button)

    def getMouseX(): Int = 
        ((mouseX + mouseOffsetX) * mouseScaleX).toInt
    
    def getMouseY(): Int = 
        ((mouseY + mouseOffsetY) * mouseScaleY).toInt

    def getMousePosition(): Vector2 =
        Vector2(getMouseX().toFloat, getMouseY().toFloat)
    
    def getMouseDelta(): Vector2 =
        val deltaX = ((mouseX - previousMouseX) * mouseScaleX).toFloat
        val deltaY = ((mouseY - previousMouseY) * mouseScaleY).toFloat
        Vector2(deltaX, deltaY)

    def setMousePosition(x: Int, y: Int): Unit =
        if !Window.isWindowInitialized then return
        SDL_WarpMouseInWindow(Window.windowHandle, x, y)
    end setMousePosition

    def setMouseOffset(offsetX: Int, offsetY: Int): Unit =
        mouseOffsetX = offsetX
        mouseOffsetY = offsetY
    end setMouseOffset

    def setMouseScale(scaleX: Float, scaleY: Float): Unit =
        mouseScaleX = scaleX
        mouseScaleY = scaleY
    end setMouseScale

    def getWheelMove(): Float =
        if math.abs(mouseWheelX) > math.abs(mouseWheelY) then mouseWheelX else mouseWheelY
    
    def getWheelMoveVector(): Vector2 =
        Vector2(mouseWheelX, mouseWheelY)
end Input