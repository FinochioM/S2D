package s2d.textures

import s2d.types.{Image, PixelFormat}
import s2d.sdl2.SDL.*
import s2d.sdl2.Extras.*
import s2d.stb.all.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import scala.util.Using
import scalanative.libc.stdio.*
import scalanative.libc.stdlib.*
import scalanative.libc.string.*

object Images:
  def load(fileName: String): Option[Image] =
    try
      Zone {
        val width = alloc[CInt](1)
        val height = alloc[CInt](1)
        val channels = alloc[CInt](1)

        val data = stbi_load(toCString(fileName), width, height, channels, 0)

        if data != null then
          val w = !width
          val h = !height
          val c = !channels

          val format = c match
            case 1 => PixelFormat.UncompressedGrayscale.value
            case 2 => PixelFormat.UncompressedGrayAlpha.value
            case 3 => PixelFormat.UncompressedRGB8.value
            case 4 => PixelFormat.UncompressedR8G8B8A8.value
            case _ => PixelFormat.UncompressedR8G8B8A8.value

          Some(Image(data.asInstanceOf[Ptr[Byte]], w, h, 1, format))
        else
          None
      }
    catch
      case _: Exception => None

  def loadRaw(fileName: String, width: Int, height: Int, format: Int, headerSize: Int): Option[Image] =
      try
        Zone {
          val file = fopen(toCString(fileName), c"rb")
          if file == null then
            return None

          try
            fseek(file, 0, SEEK_END)
            val fileSize = ftell(file)
            fseek(file, 0, SEEK_SET)

            if fileSize <= headerSize then
              return None

            val pixelFormat = PixelFormat.fromValue(format).getOrElse(PixelFormat.UncompressedR8G8B8A8)
            val expectedDataSize = width * height * pixelFormat.bytesPerPixel
            val availableDataSize = fileSize - headerSize

            if availableDataSize < expectedDataSize then
              return None

            val pixelData = malloc(expectedDataSize.toULong).asInstanceOf[Ptr[stbi_uc]]
            if pixelData == null then
              return None

            fseek(file, headerSize, SEEK_SET)

            val bytesRead = fread(pixelData.asInstanceOf[Ptr[Byte]], 1.toULong, expectedDataSize.toULong, file)
            if bytesRead != expectedDataSize then
              stbi_image_free(pixelData.asInstanceOf[Ptr[Byte]])
              return None

            Some(Image(pixelData.asInstanceOf[Ptr[Byte]], width, height, 1, format))
          finally
            fclose(file)
        }
      catch
        case _: Exception => None

  def loadAnim(fileName: String): Option[(Image, Int)] =
    try
      Zone {
        val file = fopen(toCString(fileName), c"rb")
        if file == null then
          return None

        try
          fseek(file, 0, SEEK_END)
          val fileSize = ftell(file)
          fseek(file, 0, SEEK_SET)

          if fileSize <= 0 then
            return None

          val fileBuffer = alloc[stbi_uc](fileSize.toULong)
          if fileBuffer == null then
            return None

          val bytesRead = fread(fileBuffer.asInstanceOf[Ptr[Byte]], 1.toULong, fileSize.toULong, file)
          if bytesRead != fileSize then
            return None

          val width = alloc[CInt](1)
          val height = alloc[CInt](1)
          val channels = alloc[CInt](1)
          val frames = alloc[CInt](1)
          val delays = alloc[Ptr[CInt]](1)

          val data = stbi_load_gif_from_memory(
            fileBuffer,
            fileSize,
            delays,
            width, height, frames, channels, 0
          )

          if data != null then
            val w = !width
            val h = !height
            val c = !channels
            val frameCount = !frames

            val format = c match
              case 1 => PixelFormat.UncompressedGrayscale.value
              case 2 => PixelFormat.UncompressedGrayAlpha.value
              case 3 => PixelFormat.UncompressedRGB8.value
              case 4 => PixelFormat.UncompressedR8G8B8A8.value
              case _ => PixelFormat.UncompressedR8G8B8A8.value

            Some((Image(data.asInstanceOf[Ptr[Byte]], w, h, 1, format), frameCount))
          else
            None
        finally
          fclose(file)
      }
    catch
      case _: Exception => None

  def loadAnimFromMemory(fileType: String, fileData: Array[Byte], dataSize: Int): Option[(Image, Int)] =
      try
        Zone {
          if dataSize <= 0 || fileData.length < dataSize then
            return None

          val dataBuffer = alloc[stbi_uc](dataSize.toULong)
          if dataBuffer == null then
            return None

          var i = 0
          while i < dataSize do
            dataBuffer(i) = fileData(i).toUByte
            i += 1

          val width = alloc[CInt](1)
          val height = alloc[CInt](1)
          val channels = alloc[CInt](1)
          val frames = alloc[CInt](1)
          val delays = alloc[Ptr[CInt]](1)

          val data = stbi_load_gif_from_memory(
            dataBuffer,
            dataSize,
            delays,
            width, height, frames, channels, 0
          )

          if data != null then
            val w = !width
            val h = !height
            val c = !channels
            val frameCount = !frames

            val format = c match
              case 1 => PixelFormat.UncompressedGrayscale.value
              case 2 => PixelFormat.UncompressedGrayAlpha.value
              case 3 => PixelFormat.UncompressedRGB8.value
              case 4 => PixelFormat.UncompressedR8G8B8A8.value
              case _ => PixelFormat.UncompressedR8G8B8A8.value

            Some((Image(data.asInstanceOf[Ptr[Byte]], w, h, 1, format), frameCount))
          else
            None
        }
      catch
        case _: Exception => None

  def loadFromMemory(fileType: String, fileData: Array[Byte], dataSize: Int): Option[Image] =
      try
        Zone {
          if dataSize <= 0 || fileData.length < dataSize then
            return None

          val dataBuffer = alloc[stbi_uc](dataSize.toULong)
          if dataBuffer == null then
            return None

          var i = 0
          while i < dataSize do
            dataBuffer(i) = fileData(i).toUByte
            i += 1

          val width = alloc[CInt](1)
          val height = alloc[CInt](1)
          val channels = alloc[CInt](1)

          val data = stbi_load_from_memory(dataBuffer, dataSize, width, height, channels, 0)

          if data != null then
            val w = !width
            val h = !height
            val c = !channels

            val format = c match
              case 1 => PixelFormat.UncompressedGrayscale.value
              case 2 => PixelFormat.UncompressedGrayAlpha.value
              case 3 => PixelFormat.UncompressedRGB8.value
              case 4 => PixelFormat.UncompressedR8G8B8A8.value
              case _ => PixelFormat.UncompressedR8G8B8A8.value

            Some(Image(data.asInstanceOf[Ptr[Byte]], w, h, 1, format))
          else
            None
        }
      catch
        case _: Exception => None

  def unload(image: Image): Unit =
    if image.data != null then
      stbi_image_free(image.data)