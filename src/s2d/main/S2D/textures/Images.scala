package S2D.textures

import S2D.types.PixelFormat.UncompressedR8G8B8A8
import S2D.types.{Image, PixelFormat}
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack

object Images:
  def load(fileName: String): Option[Image] =
    try
      val stack = MemoryStack.stackPush()
      try
        val width = stack.mallocInt(1)
        val height = stack.mallocInt(1)
        val channels = stack.mallocInt(1)

        val data = stbi_load(fileName, width, height, channels, 0)

        if data != null then
          val w = width.get(0)
          val h = height.get(0)
          val c = channels.get(0)

          val format = c match
            case 1 => PixelFormat.UncompressedGrayscale.value
            case 2 => PixelFormat.UncompressedGrayAlpha.value
            case 3 => PixelFormat.UncompressedRGB8.value
            case 4 => PixelFormat.UncompressedR8G8B8A8.value
            case _ => PixelFormat.UncompressedR8G8B8A8.value

          Some(Image(data, w, h, 1, format))
        else
          None
      finally MemoryStack.stackPop()
    catch
      case _: Exception => None
  def loadRaw(fileName: String, width: Int, height: Int, format: Int, headerSize: Int): Option[Image] =
    try
      val fileData = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(fileName))

      if fileData.length <= headerSize then
        return None

      val pixelFormat = PixelFormat.fromValue(format).getOrElse(UncompressedR8G8B8A8)
      val expectedDataSize = width * height * pixelFormat.bytesPerPixel
      val availableDataSize = fileData.length - headerSize

      if availableDataSize < expectedDataSize then
        return None

      val pixelData = java.nio.ByteBuffer.allocateDirect(expectedDataSize)
      pixelData.put(fileData, headerSize, expectedDataSize)
      pixelData.flip()

      Some(Image(pixelData, width, height, 1, format))
    catch
      case _: Exception => None