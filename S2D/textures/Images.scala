package S2D.textures

import S2D.types.{Image, PixelFormat}
import sdl2.SDL.*
import sdl2.Extras.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import scala.util.Using
import scalanative.libc.{stdlib, string}

object Images:
  def load(fileName: String): Option[Image] =
    Zone {
      try
        val fileNameCStr = toCString(fileName)
        val surface = SDL_LoadBMP(fileNameCStr)

        if surface == null then
          None
        else
          try
            val w = surface._3
            val h = surface._4
            val pixels = surface._6
            val pitch = surface._8
            val pixelFormat = surface._2

            val bytesPerPixel = if pixelFormat != null then
              SDL_BYTESPERPIXEL(pixelFormat._1)  // format field
            else 4.toUByte

            val format = bytesPerPixel match
              case x if x == 1.toUByte => PixelFormat.UncompressedGrayscale.value
              case x if x == 2.toUByte => PixelFormat.UncompressedGrayAlpha.value
              case x if x == 3.toUByte => PixelFormat.UncompressedRGB8.value
              case x if x == 4.toUByte => PixelFormat.UncompressedR8G8B8A8.value
              case _ => PixelFormat.UncompressedR8G8B8A8.value

            val dataSize = h * pitch

            val imageData = stdlib.malloc(dataSize.toUSize).asInstanceOf[Ptr[Byte]]
            if imageData != null then
              string.memcpy(imageData, pixels, dataSize.toUSize)
              Some(Image(imageData, w, h, 1, format))
            else
              None
          finally
            SDL_FreeSurface(surface)
      catch
        case _: Exception => None
    }