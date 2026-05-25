package s2d.core

import s2d.types.*

object Random:
    private var rng = new scala.util.Random()

    def seed(value: Long): Unit =
        rng = new scala.util.Random(value)

    def int(min: Int, max: Int): Int =
        if min >= max then min
        else min + rng.nextInt(max - min + 1)

    def float(min: Float, max: Float): Float =
        if min >= max then min
        else min + rng.nextFloat() * (max - min)

    def boolean: Boolean =
        rng.nextBoolean()

    def vector2(minX: Float, maxX: Float, minY: Float, maxY: Float): Vector2 =
        Vector2(float(minX, maxX), float(minY, maxY))

    def color(alpha: Int = 255): Color =
        Color(int(0, 255), int(0, 255), int(0, 255), alpha)

    def choose[A](items: A*): Option[A] =
        if items.isEmpty then None
        else Some(items(int(0, items.length - 1)))

end Random