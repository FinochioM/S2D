package s2d.textures

import s2d.types.{Image, PixelFormat, Texture2D}
import s2d.core.Window
import s2d.gl.GL.*
import s2d.gl.GLExtras.*
import s2d.stb.all.*
import scalanative.unsafe.*
import scalanative.unsigned.*
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

            val pixelData = malloc(expectedDataSize.toLong).asInstanceOf[Ptr[stbi_uc]]
            if pixelData == null then
              return None

            fseek(file, headerSize, SEEK_SET)

            val bytesRead = fread(pixelData.asInstanceOf[Ptr[Byte]], 1.toCSize, expectedDataSize.toCSize, file)
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

          val fileBuffer = alloc[stbi_uc](fileSize.toInt)
          if fileBuffer == null then
            return None

          val bytesRead = fread(fileBuffer.asInstanceOf[Ptr[Byte]], 1.toCSize, fileSize.toCSize, file)
          if bytesRead != fileSize then
            return None

          val width = alloc[CInt](1)
          val height = alloc[CInt](1)
          val channels = alloc[CInt](1)
          val frames = alloc[CInt](1)
          val delays = alloc[Ptr[CInt]](1)

          val data = stbi_load_gif_from_memory(
            fileBuffer,
            fileSize.toInt,
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

          val dataBuffer = alloc[stbi_uc](dataSize.toInt)
          if dataBuffer == null then
            return None

          var i = 0
          while i < dataSize do
            dataBuffer(i) = fileData(i).asInstanceOf[stbi_uc]
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

          val dataBuffer = alloc[stbi_uc](dataSize.toInt)
          if dataBuffer == null then
            return None

          var i = 0
          while i < dataSize do
            dataBuffer(i) = fileData(i).asInstanceOf[stbi_uc]
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

  def loadFromTexture(texture: Texture2D): Option[Image] =
      try
        if texture.id == 0 then return None

        val pixelFormat = PixelFormat.fromValue(texture.format).getOrElse(PixelFormat.UncompressedR8G8B8A8)
        val bytesPerPixel = pixelFormat.bytesPerPixel
        val dataSize = texture.width * texture.height * bytesPerPixel

        val pixelData = malloc(dataSize.toLong)
        if pixelData == null then
          return None

        glBindTexture(GL_TEXTURE_2D, texture.id.toUInt)

        val (glFormat, glType) = texture.format match
          case f if f == PixelFormat.UncompressedGrayscale.value => (GL_LUMINANCE, GL_UNSIGNED_BYTE)
          case f if f == PixelFormat.UncompressedGrayAlpha.value => (GL_LUMINANCE_ALPHA, GL_UNSIGNED_BYTE)
          case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB, GL_UNSIGNED_BYTE)
          case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA, GL_UNSIGNED_BYTE)
          case f if f == PixelFormat.UncompressedR5G6B5.value => (GL_RGB, GL_UNSIGNED_SHORT_5_6_5)
          case f if f == PixelFormat.UncompressedR5G6B5A1.value => (GL_RGBA, GL_UNSIGNED_SHORT_5_5_5_1)
          case f if f == PixelFormat.UncompressedR4G4B4A4.value => (GL_RGBA, GL_UNSIGNED_SHORT_4_4_4_4)
          case _ => (GL_RGBA, GL_UNSIGNED_BYTE)

        glGetTexImage(GL_TEXTURE_2D, 0, glFormat, glType, pixelData.asInstanceOf[Ptr[Byte]])
        glBindTexture(GL_TEXTURE_2D, 0.toUInt)

        Some(Image(pixelData.asInstanceOf[Ptr[Byte]], texture.width, texture.height, texture.mipmaps, texture.format))
      catch
        case _: Exception => None

  def loadFromScreen(): Option[Image] =
      try
        if !Window.isReady then return None

        val width = Window.width
        val height = Window.height
        val bytesPerPixel = 4
        val dataSize = width * height * bytesPerPixel

        val tempPixelData = malloc(dataSize.toLong)
        if tempPixelData == null then
          return None

        glReadPixels(0, 0, width.toUInt, height.toUInt, GL_RGBA, GL_UNSIGNED_BYTE, tempPixelData.asInstanceOf[Ptr[Byte]])

        val pixelData = malloc(dataSize.toLong)
        if pixelData == null then
          free(tempPixelData)
          return None

        val rowSize = width * bytesPerPixel
        var y = 0
        while y < height do
          val srcRow = (height - 1 - y) * rowSize
          val dstRow = y * rowSize

          memcpy(
            pixelData.asInstanceOf[Ptr[Byte]] + dstRow,
            tempPixelData.asInstanceOf[Ptr[Byte]] + srcRow,
            rowSize.toCSize
          )
          y += 1

        free(tempPixelData)

        Some(Image(pixelData.asInstanceOf[Ptr[Byte]], width, height, 1, PixelFormat.UncompressedR8G8B8A8.value))
      catch
        case _: Exception => None

  def isValid(image: Image): Boolean =
      image.data != null &&
        image.width > 0 &&
        image.height > 0 &&
        image.mipmaps > 0 &&
        PixelFormat.fromValue(image.format).isDefined

  def unload(image: Image): Unit =
    try
      if image.data != null then
        stbi_image_free(image.data)
    catch
      case _: Exception =>

  def getChannelCount(format: Int): Int =
    PixelFormat.fromValue(format) match
      case Some(PixelFormat.UncompressedGrayscale) => 1
      case Some(PixelFormat.UncompressedGrayAlpha) => 2
      case Some(PixelFormat.UncompressedR5G6B5) => 3
      case Some(PixelFormat.UncompressedRGB8) => 3
      case Some(PixelFormat.UncompressedR5G6B5A1) => 4
      case Some(PixelFormat.UncompressedR4G4B4A4) => 4
      case Some(PixelFormat.UncompressedR8G8B8A8) => 4
      case Some(PixelFormat.UncompressedR32) => 1
      case Some(PixelFormat.UncompressedR32G32B32) => 3
      case Some(PixelFormat.UncompressedR32G32B32A32) => 4
      case _ => 4

  def getBytesPerPixel(format: Int): Int =
    PixelFormat.fromValue(format).map(_.bytesPerPixel).getOrElse(4)