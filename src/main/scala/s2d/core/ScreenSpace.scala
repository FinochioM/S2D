package s2d.core

import s2d.types.{Vector2, Camera2D, Ray2D}

object ScreenSpace:
    def screenToRay(screenPosition: Vector2, camera: Camera2D): Ray2D =
        val worldPosition = screenToPoint(screenPosition, camera)
        val direction = (worldPosition - camera.target).normalize
        Ray2D(camera.target, direction)
    
    def screenToPoint(screenPosition: Vector2, camera: Camera2D): Vector2 =
        val offsetPos = screenPosition - camera.offset
        val zoomedPos = offsetPos / camera.zoom
        val rotatedPos = zoomedPos.rotate(-math.toRadians(camera.rotation).toFloat)

        rotatedPos + camera.target

    def worldToPoint(worldPosition: Vector2, camera: Camera2D): Vector2 =
        val targetPos = worldPosition - camera.target
        val rotatedPos = targetPos.rotate(math.toRadians(camera.rotation).toFloat)
        val zoomedPos = rotatedPos * camera.zoom

        zoomedPos + camera.offset
end ScreenSpace