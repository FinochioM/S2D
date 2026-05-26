package s2d.textures

import s2d.types.* 
import scalanative.unsafe.*

object Pixels:
    def get(image: Image, x: Int, y: Int): Option[Color] =
        if image.data == null then return None
        if x < 0 || x >= image.width || y < 0 || y >= image.height then return None

        val channels = 4
        val offset = (y * image.width + x) * channels
        val ptr = image.data.asInstanceOf[Ptr[Byte]]

        val r = (ptr(offset) & 0xFF)
        val g = (ptr(offset + 1) & 0xFF)
        val b = (ptr(offset + 2) & 0xFF)
        val a = (ptr(offset + 3) & 0xFF)
        Some(Color(r, g, b, a))

    def set(image: Image, x: Int, y: Int, color: Color): Unit =
        if image.data == null then return return
        if x < 0 || x >= image.width || y < 0 || y >= image.height then return return
        
        val channels = 4
        val offset = (y * image.width + x) * channels
        val ptr = image.data.asInstanceOf[Ptr[Byte]]

        ptr(offset) = color.r.toByte
        ptr(offset + 1) = color.g.toByte
        ptr(offset + 2) = color.b.toByte
        ptr(offset + 3) = color.a.toByte

    def fill(image: Image, color: Color): Unit =
        if image.data == null then return

        for
            y <- 0 until image.height
            x <- 0 until image.width
        do set(image, x, y, color)
end Pixels