package S2D.textures

import S2D.types.{Image, PixelFormat}
import sdl2.SDL.*
import sdl2.Extras.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import scala.util.Using
import scalanative.libc.{stdlib, string, stdio}

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

  def loadRaw(fileName: String, width: Int, height: Int, format: Int, headerSize: Int): Option[Image] =
    Zone {
        try
          val fileNameCStr = toCString(fileName)
          val file = stdio.fopen(fileNameCStr, c"rb")

          if file == null then
            return None

          try
            stdio.fseek(file, 0, stdio.SEEK_END)
            val fileSize = stdio.ftell(file)
            stdio.fseek(file, 0, stdio.SEEK_SET)

            if fileSize <= headerSize then
              return None

            val pixelFormat = PixelFormat.fromValue(format).getOrElse(PixelFormat.UncompressedR8G8B8A8)
            val expectedDataSize = width * height * pixelFormat.bytesPerPixel
            val availableDataSize = fileSize - headerSize

            if availableDataSize < expectedDataSize then
              return None

            if headerSize > 0 then
              stdio.fseek(file, headerSize, stdio.SEEK_SET)

            val imageData = stdlib.malloc(expectedDataSize.toUSize).asInstanceOf[Ptr[Byte]]
            if imageData == null then
              return None

            val bytesRead = stdio.fread(imageData, 1.toUSize, expectedDataSize.toUSize, file)
            if bytesRead != expectedDataSize.toUSize then
              stdlib.free(imageData)
              return None

            Some(Image(imageData, width, height, 1, format))
          finally
            stdio.fclose(file)
        catch
          case _: Exception => None
    }