package S2D.textures

import S2D.types.PixelFormat.UncompressedR8G8B8A8
import S2D.types.{Image, PixelFormat, Texture2D}
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
  def loadAnim(fileName: String): Option[(Image, Int)] =
    try
      val stack = MemoryStack.stackPush()
      try
        val width = stack.mallocInt(1)
        val height = stack.mallocInt(1)
        val channels = stack.mallocInt(1)
        val frames = stack.mallocInt(1)
        val delays = stack.mallocPointer(1)

        val fileBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(fileName))
        val fileBuffer = stack.malloc(fileBytes.length)
        fileBuffer.put(fileBytes)
        fileBuffer.flip()

        val data = stbi_load_gif_from_memory(
          fileBuffer,
          delays,
          width, height, frames, channels, 0
        )

        if data != null then
          val w = width.get(0)
          val h = height.get(0)
          val c = channels.get(0)
          val frameCount = frames.get(0)

          val format = c match
            case 1 => PixelFormat.UncompressedGrayscale.value
            case 2 => PixelFormat.UncompressedGrayAlpha.value
            case 3 => PixelFormat.UncompressedRGB8.value
            case 4 => PixelFormat.UncompressedR8G8B8A8.value
            case _ => PixelFormat.UncompressedR8G8B8A8.value

          Some((Image(data, w, h, 1, format), frameCount))
        else
          None
      finally
        MemoryStack.stackPop()
    catch
      case _: Exception => None
  def loadAnimFromMemory(fileType: String, fileData: Array[Byte], dataSize: Int): Option[(Image, Int)] =
    try
      val stack = MemoryStack.stackPush()
      try
        val width = stack.mallocInt(1)
        val height = stack.mallocInt(1)
        val channels = stack.mallocInt(1)
        val frames = stack.mallocInt(1)
        val delays = stack.mallocPointer(1)

        val dataBuffer = stack.malloc(dataSize)
        dataBuffer.put(fileData, 0, dataSize)
        dataBuffer.flip()

        val data = stbi_load_gif_from_memory(
          dataBuffer,
          delays,
          width, height, frames, channels, 0
        )

        if data != null then
          val w = width.get(0)
          val h = height.get(0)
          val c = channels.get(0)
          val frameCount = frames.get(0)

          val format = c match
            case 1 => PixelFormat.UncompressedGrayscale.value
            case 2 => PixelFormat.UncompressedGrayAlpha.value
            case 3 => PixelFormat.UncompressedRGB8.value
            case 4 => PixelFormat.UncompressedR8G8B8A8.value
            case _ => PixelFormat.UncompressedR8G8B8A8.value

          Some((Image(data, w, h, 1, format), frameCount))
        else
          None
      finally
        MemoryStack.stackPop()
    catch
      case _: Exception => None
  def loadFromMemory(fileType: String, fileData: Array[Byte], dataSize: Int): Option[Image] =
    try
      val stack = MemoryStack.stackPush()
      try
        val width = stack.mallocInt(1)
        val height = stack.mallocInt(1)
        val channels = stack.mallocInt(1)

        val dataBuffer = stack.malloc(dataSize)
        dataBuffer.put(fileData, 0, dataSize)
        dataBuffer.flip()

        val data = stbi_load_from_memory(dataBuffer, width, height, channels, 0)

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
      finally
        MemoryStack.stackPop()
    catch
      case _: Exception => None
  def loadFromTexture(texture: Texture2D): Option[Image] =
    try
      import org.lwjgl.opengl.GL11.*
      import org.lwjgl.opengl.GL12.*

      if texture.id == 0 then return None

      val pixelFormat = PixelFormat.fromValue(texture.format).getOrElse(PixelFormat.UncompressedR8G8B8A8)
      val bytesPerPixel = pixelFormat.bytesPerPixel
      val dataSize = texture.width * texture.height * bytesPerPixel

      val pixelData = java.nio.ByteBuffer.allocateDirect(dataSize)

      glBindTexture(GL_TEXTURE_2D, texture.id)

      val (glFormat, glType) = texture.format match
        case f if f == PixelFormat.UncompressedGrayscale.value => (GL_LUMINANCE, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedGrayAlpha.value => (GL_LUMINANCE_ALPHA, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedR5G6B5.value => (GL_RGB, GL_UNSIGNED_SHORT_5_6_5)
        case f if f == PixelFormat.UncompressedR5G6B5A1.value => (GL_RGBA, GL_UNSIGNED_SHORT_5_5_5_1)
        case f if f == PixelFormat.UncompressedR4G4B4A4.value => (GL_RGBA, GL_UNSIGNED_SHORT_4_4_4_4)
        case _ => (GL_RGBA, GL_UNSIGNED_BYTE)

      glGetTexImage(GL_TEXTURE_2D, 0, glFormat, glType, pixelData)
      glBindTexture(GL_TEXTURE_2D, 0)

      Some(Image(pixelData, texture.width, texture.height, texture.mipmaps, texture.format))
    catch
      case _: Exception => None
  def loadFromScreen(): Option[Image] =
    try
      import org.lwjgl.opengl.GL11.*
      import S2D.core.Window

      if !Window.isReady then return None

      val width = Window.width
      val height = Window.height
      val bytesPerPixel = 4
      val dataSize = width * height * bytesPerPixel

      val pixelData = java.nio.ByteBuffer.allocateDirect(dataSize)

      glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixelData)

      val flippedData = java.nio.ByteBuffer.allocateDirect(dataSize)
      val rowSize = width * bytesPerPixel

      for (y <- 0 until height) {
        val srcRow = (height - 1 - y) * rowSize
        val dstRow = y * rowSize

        pixelData.position(srcRow)
        val rowData = new Array[Byte](rowSize)
        pixelData.get(rowData)

        flippedData.position(dstRow)
        flippedData.put(rowData)
      }

      flippedData.flip()

      Some(Image(flippedData, width, height, 1, PixelFormat.UncompressedR8G8B8A8.value))
    catch
      case _: Exception => None
  def isValid(image: Image): Boolean =
    image.data != null &&
      image.width > 0 &&
      image.height > 0 &&
      image.mipmaps > 0 &&
      PixelFormat.fromValue(image.format).isDefined
  def unloadImage(image: Image): Unit =
    try
      if image.data != null then
        import org.lwjgl.stb.STBImage.*
        stbi_image_free(image.data)
    catch
      case _: Exception => None
  def exportImage (image: Image, fileName: String): Boolean =
    try
      import org.lwjgl.stb.STBImageWrite.*

      if !isValid(image) then return false

      val extension = fileName.toLowerCase.substring(fileName.lastIndexOf('.'))
      val result = extension match
        case ".png" => stbi_write_png(fileName, image.width, image.height,
          getChannelCount(image.format), image.data, image.width * getBytesPerPixel(image.format))
        case ".jpg" | ".jpeg" => stbi_write_jpg(fileName, image.width, image.height,
          getChannelCount(image.format), image.data, 90)
        case ".bmp" => stbi_write_bmp(fileName, image.width, image.height,
          getChannelCount(image.format), image.data)
        case ".tga" => stbi_write_tga(fileName, image.width, image.height,
          getChannelCount(image.format), image.data)
        case _ => false

      result
    catch
      case _: Exception => false
  def exportToMemory(image: Image, fileType: String): Option[(Array[Byte], Int)] =
    try
      if !isValid(image) then return None

      val tempFile = java.nio.file.Files.createTempFile("s2d_export", s".$fileType")

      try
        val exportSuccess =
        exportImage (image, tempFile.toString)

        if exportSuccess then
          val fileBytes = java.nio.file.Files.readAllBytes(tempFile)
          Some((fileBytes, fileBytes.length))
        else
          None
      finally
        try
          java.nio.file.Files.deleteIfExists(tempFile)
        catch
          case _: Exception =>
    catch
      case _: Exception => None
  def exportAsCode(image: Image, fileName: String): Boolean =
    try
      if !isValid(image) then return false

      import java.io.PrintWriter

      val writer = new PrintWriter(fileName)
      try
        val dataSize = image.width * image.height * getBytesPerPixel(image.format)
        val imageName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.'))

        writer.println(s"// Image data for $imageName")
        writer.println(s"// Width: ${image.width}, Height: ${image.height}, Format: ${image.format}")
        writer.println(s"val ${imageName}Width = ${image.width}")
        writer.println(s"val ${imageName}Height = ${image.height}")
        writer.println(s"val ${imageName}Format = ${image.format}")
        writer.println(s"val ${imageName}Data = Array[Byte](")

        for (i <- 0 until dataSize) {
          if i % 16 == 0 then writer.print("  ")
          writer.print(f"0x${image.data.get(i) & 0xFF}%02x")
          if i < dataSize - 1 then writer.print(", ")
          if (i + 1) % 16 == 0 then writer.println()
        }

        if dataSize % 16 != 0 then writer.println()
        writer.println(")")

        true
      finally
        writer.close()
    catch
      case _: Exception => false
  def getChannelCount(format: Int): Int =
    PixelFormat.fromValue(format) match
      case Some(PixelFormat.UncompressedGrayscale) => 1
      case Some(PixelFormat.UncompressedGrayAlpha) => 2
      case Some(PixelFormat.UncompressedRGB8) => 3
      case Some(PixelFormat.UncompressedR8G8B8A8) => 4
      case _ => 4
  def getBytesPerPixel(format: Int): Int =
    PixelFormat.fromValue(format).map(_.bytesPerPixel).getOrElse(4)