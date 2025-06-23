package s2d.core

import s2d.types.Key
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
end Input