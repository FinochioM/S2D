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

    private[core] def processKeyEvent(event: Ptr[SDL_KeyboardEvent]): Unit =
        val keycode = event.keysym.sym
        val key = Key.fromKeycode(keycode)
        val isPressed = event.state == SDL_PRESSED.toUByte

        if isPressed then
            currentKeyStates += key
        else
            currentKeyStates -= key
    end processKeyEvent

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
end Input