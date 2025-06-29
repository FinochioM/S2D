package s2d.types

case class Ray2D(
    origin: Vector2,
    direction: Vector2
):
    def pointAt(distance: Float): Vector2 =
        origin + direction * distance

    def intersectLine(lineStart: Vector2, lineEnd: Vector2): Option[Float] =
        val lineDir = lineEnd - lineStart
        val cross = direction.x * lineDir.y - direction.y * lineDir.x
        
        if math.abs(cross) < 1e-10f then None
        else
            val toStart = lineStart - origin
            val t = (toStart.x * lineDir.y - toStart.y * lineDir.x) / cross
            val u = (toStart.x * direction.y - toStart.y * direction.x) / cross

            if u >= 0.0f && u <= 1.0f then Some(t)
            else None
    
    def intersectCircle(center: Vector2, radius: Float): Option[(Float, Float)] =
        val toCenter = center - origin
        val a = direction.dot(direction)
        val b = -2.0f * direction.dot(center)
        val c = toCenter.dot(toCenter) - radius * radius

        val discriminant = b * b - 4 * a * c

        if discriminant < 0 then None
        else if discriminant == 0 then
            val t = -b / (2 * a)
            Some((t, t))
        else
            val sqrtDistance = math.sqrt(discriminant).toFloat
            val t1 = (-b - sqrtDistance) / (2 * a)
            val t2 = (-b + sqrtDistance) / (2 * a)
            Some((t1, t2))
end Ray2D

object Ray2D:
    def apply(originX: Float, originY: Float, directionX: Float, directionY: Float): Ray2D =
        Ray2D(Vector2(originX, originY), Vector2(directionX, directionY))
    
    def fromPoints(from: Vector2, to: Vector2): Ray2D =
        Ray2D(from, (to - from).normalize)
end Ray2D