package s2d.core

import s2d.backend.sdl2.SDL.* 
import scalanative.unsigned.* 

object Timing:
    private var lastFrameTime: Double = 0.0
    private var deltaTime: Double = 0.0
    private var targetFPS: Int = 0
    private var targetFrameTime: Double = 0.0
    private var frameStartTime: Double = 0.0

    private[core] def updateDelta(): Unit =
        val currentTime = time
        deltaTime = currentTime - lastFrameTime
        lastFrameTime = currentTime
        frameStartTime = currentTime

    private[core] def waitForTargetFPS(): Unit =
        if targetFPS > 0 then
            val currentTime = time
            val frameTime = currentTime - frameStartTime
            val waitTime = targetFrameTime - frameTime

            if waitTime > 0.0 then
                SDL_Delay((waitTime * 1000.0).toInt.toUInt)

    def time: Double = 
        SDL_GetTicks().toDouble / 1000.0

    def delta: Double = 
        deltaTime

    def setTargetFPS(fps: Int): Unit =
        targetFPS = fps
        if fps > 0 then
            targetFrameTime = 1.0 / fps.toDouble
        else
            targetFrameTime = 0.0

end Timing