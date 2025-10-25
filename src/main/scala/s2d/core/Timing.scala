package s2d.core

import s2d.backend.sdl2.SDL.* 
import scalanative.unsigned.* 

object Timing:
    private var lastFrameTime: Double = 0.0
    private var deltaTime: Double = 0.0

    private[core] def updateDelta(): Unit =
        val currentTime = getTime()
        deltaTime = currentTime - lastFrameTime
        lastFrameTime = currentTime

    def getTime(): Double =
        SDL_GetTicks().toDouble / 1000.0

    def getDelta(): Double =
        deltaTime
end Timing
