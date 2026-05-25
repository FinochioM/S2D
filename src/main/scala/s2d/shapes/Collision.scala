package s2d.shapes


import s2d.types.* 

object Collision:
    def pointInRect(point: Vector2, rect: Rectangle): Boolean =
        point.x >= rect.x &&
        point.x <= rect.x + rect.width &&
        point.y >= rect.y &&
        point.y <= rect.y + rect.height

    def pointInCircle(point: Vector2, center: Vector2, radius: Float): Boolean =
        val dx = point.x - center.x
        val dy = point.y - center.y

        (dx * dx + dy * dy) <= (radius * radius)

    def pointInTriangle(point: Vector2, v1: Vector2, v2: Vector2, v3: Vector2): Boolean =
        def sign(p1: Vector2, p2: Vector2, p3: Vector2): Float =
            (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y)

        val d1 = sign(point, v1, v2)
        val d2 = sign(point, v2, v3)
        val d3 = sign(point, v3, v1)
        val hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0)
        val hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0)
        !(hasNeg && hasPos)

    def rects(r1: Rectangle, r2: Rectangle): Boolean =
        r1.x < r2.x + r2.width &&
        r1.x + r1.width > r2.x &&
        r1.y < r2.y + r2.height &&
        r1.y + r1.height > r2.y

    def circles(c1: Vector2, r1: Float, c2: Vector2, r2: Float): Boolean = 
        val dx = c1.x - c2.x
        val dy = c1.y - c2.y
        val distance = math.sqrt(dx * dx + dy * dy).toFloat
        distance <= (r1 + r2)

    def circleRect(center: Vector2, radius: Float, rect: Rectangle): Boolean =
        val nearestX = math.max(rect.x, math.min(center.x, rect.x + rect.width)).toFloat
        val nearestY = math.max(rect.y, math.min(center.y, rect.y + rect.height)).toFloat
        val dx = center.x - nearestX
        val dy = center.y - nearestY
        (dx * dx + dy * dy) <= (radius * radius)

    def lines(start1: Vector2, end1: Vector2, start2: Vector2, end2: Vector2): Boolean =
        linesAt(start1, end1, start2, end2).isDefined

    def linesAt(start1: Vector2, end1: Vector2, start2: Vector2, end2: Vector2): Option[Vector2] =
        val d1x = end1.x - start1.x
        val d1y = end1.y - start1.y
        val d2x = end2.x - start2.x
        val d2y = end2.y - start2.y
        val denom = d1x * d2y - d1y * d2x
        if math.abs(denom) < 1e-10f then return None  // parallel
        val t = ((start2.x - start1.x) * d2y - (start2.y - start1.y) * d2x) / denom
        val u = ((start2.x - start1.x) * d1y - (start2.y - start1.y) * d1x) / denom
        if t >= 0 && t <= 1 && u >= 0 && u <= 1 then
            Some(Vector2(start1.x + (t * d1x).toFloat, start1.y + (t * d1y).toFloat))
        else
            None

    def lineRect(start: Vector2, end: Vector2, rect: Rectangle): Boolean =
        if pointInRect(start, rect) || pointInRect(end, rect) then return true
        val tl = Vector2(rect.x, rect.y)
        val tr = Vector2(rect.x + rect.width, rect.y)
        val bl = Vector2(rect.x, rect.y + rect.height)
        val br = Vector2(rect.x + rect.width, rect.y + rect.height)
        lines(start, end, tl, tr) ||
        lines(start, end, tr, br) ||
        lines(start, end, br, bl) ||
        lines(start, end, bl, tl)

    def lineCircle(start: Vector2, end: Vector2, center: Vector2, radius: Float): Boolean =
        if pointInCircle(start, center, radius) || pointInCircle(end, center, radius) then return true
        val dx = end.x - start.x
        val dy = end.y - start.y
        val fx = start.x - center.x
        val fy = start.y - center.y
        val a = dx * dx + dy * dy
        val b = 2 * (fx * dx + fy * dy)
        val c = fx * fx + fy * fy - radius * radius
        var discriminant = b * b - 4 * a * c
        if discriminant < 0 then return false
        discriminant = math.sqrt(discriminant).toFloat
        val t1 = (-b - discriminant) / (2 * a)
        val t2 = (-b + discriminant) / (2 * a)
        (t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1)

    def rectOverlap(r1: Rectangle, r2: Rectangle): Option[Rectangle] =
        if !rects(r1, r2) then return None
        val x = math.max(r1.x, r2.x)
        val y = math.max(r1.y, r2.y)
        val width  = math.min(r1.x + r1.width,  r2.x + r2.width)  - x
        val height = math.min(r1.y + r1.height, r2.y + r2.height) - y
        Some(Rectangle(x.toFloat, y.toFloat, width.toFloat, height.toFloat))

end Collision