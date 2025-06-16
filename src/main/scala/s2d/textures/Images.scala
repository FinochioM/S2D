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

          val pixelData = malloc(expectedDataSize.toLong)
          if pixelData == null then
            return None

          fseek(file, headerSize, SEEK_SET)

          val bytesRead = fread(pixelData, 1.toCSize, expectedDataSize.toCSize, file)
          if bytesRead != expectedDataSize then
            free(pixelData)
            return None

          Some(Image(pixelData.asInstanceOf[Ptr[Byte]], width, height, 1, format))
        finally
          fclose(file)
      }
    catch
      case _: Exception => None

  def unload(image: Image): Unit =
    if image.data != null then
      free(image.data.asInstanceOf[Ptr[Byte]])