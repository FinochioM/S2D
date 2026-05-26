package s2d.textures

import s2d.types.*
import s2d.core.Window
import s2d.backend.gl.GL.*
import s2d.backend.gl.GLEWHelper
import s2d.backend.gl.GLExtras.*
import s2d.backend.stb.all.*
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
    if texture.id == 0 then return None

    val pixelFormat = PixelFormat.fromValue(texture.format).getOrElse(PixelFormat.UncompressedR8G8B8A8)
    val bytesPerPixel = pixelFormat.bytesPerPixel
    val dataSize = texture.width * texture.height * bytesPerPixel

    val pixelData = malloc(dataSize.toLong)
    if pixelData == null then
      return None
    try
      Zone {
        val framebufferArray = alloc[GLuint](1)
        GLEWHelper.glGenFramebuffers(1.toUInt, framebufferArray)
        val framebuffer = !framebufferArray

        if framebuffer == 0.toUInt then
          free(pixelData)
          return None

        GLEWHelper.glBindFramebuffer(GL_FRAMEBUFFER.toUInt, framebuffer)
        GLEWHelper.glFramebufferTexture2D(GL_FRAMEBUFFER.toUInt, GL_COLOR_ATTACHMENT0.toUInt, GL_TEXTURE_2D.toUInt, texture.id.toUInt, 0)

        val status = GLEWHelper.glCheckFramebufferStatus(GL_FRAMEBUFFER.toUInt)
        if status != GL_FRAMEBUFFER_COMPLETE.toUInt then
          GLEWHelper.glBindFramebuffer(GL_FRAMEBUFFER.toUInt, 0.toUInt)
          val deleteArray = alloc[GLuint](1)
          !deleteArray = framebuffer
          GLEWHelper.glDeleteFramebuffers(1.toUInt, deleteArray)
          free(pixelData)
          return None

        val (glFormat, glType) = texture.format match
          case f if f == PixelFormat.UncompressedGrayscale.value => (GL_RED.toUInt, GL_UNSIGNED_BYTE.toUInt)
          case f if f == PixelFormat.UncompressedGrayAlpha.value => (GL_RG.toUInt, GL_UNSIGNED_BYTE.toUInt)
          case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB.toUInt, GL_UNSIGNED_BYTE.toUInt)
          case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA.toUInt, GL_UNSIGNED_BYTE.toUInt)
          case f if f == PixelFormat.UncompressedR5G6B5.value => (GL_RGB.toUInt, GL_UNSIGNED_SHORT_5_6_5.toUInt)
          case f if f == PixelFormat.UncompressedR5G6B5A1.value => (GL_RGBA.toUInt, GL_UNSIGNED_SHORT_5_5_5_1.toUInt)
          case f if f == PixelFormat.UncompressedR4G4B4A4.value => (GL_RGBA.toUInt, GL_UNSIGNED_SHORT_4_4_4_4.toUInt)
          case _ => (GL_RGBA.toUInt, GL_UNSIGNED_BYTE.toUInt)

        glReadPixels(0, 0, texture.width.toUInt, texture.height.toUInt, glFormat, glType, pixelData.asInstanceOf[Ptr[Byte]])

        GLEWHelper.glBindFramebuffer(GL_FRAMEBUFFER.toUInt, 0.toUInt)
        val deleteArray = alloc[GLuint](1)
        !deleteArray = framebuffer
        GLEWHelper.glDeleteFramebuffers(1.toUInt, deleteArray)
      }

      Some(Image(pixelData.asInstanceOf[Ptr[Byte]], texture.width, texture.height, texture.mipmaps, texture.format))
    catch
      case _: Exception =>
        free(pixelData)
        None

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

  def copy(image: Image): Option[Image] =
    if !isValid(image) then return None
    val channels = getChannelCount(image.format)
    val dataSize = image.width * image.height * channels
    val newData = malloc(dataSize.toLong)
    if newData == null then return None
    memcpy(newData.asInstanceOf[Ptr[Byte]], image.data, dataSize.toCSize)
    Some(Image(newData.asInstanceOf[Ptr[Byte]], image.width, image.height, image.mipmaps, image.format))

  def crop(image: Image, rect: Rectangle): Option[Image] =
      if !isValid(image) then return None
      val x      = rect.x.toInt.max(0)
      val y      = rect.y.toInt.max(0)
      val width  = rect.width.toInt.min(image.width - x)
      val height = rect.height.toInt.min(image.height - y)
      if width <= 0 || height <= 0 then return None
      val channels = getChannelCount(image.format)
      val dataSize = width * height * channels
      val newData = malloc(dataSize.toLong)
      if newData == null then return None
      var row = 0
      while row < height do
          val srcOffset = ((y + row) * image.width + x) * channels
          val dstOffset = row * width * channels
          memcpy(
              newData.asInstanceOf[Ptr[Byte]] + dstOffset,
              image.data + srcOffset,
              (width * channels).toCSize
          )
          row += 1
      Some(Image(newData.asInstanceOf[Ptr[Byte]], width, height, 1, image.format))

  def resize(image: Image, newWidth: Int, newHeight: Int): Option[Image] =
      if !isValid(image) then return None
      if newWidth <= 0 || newHeight <= 0 then return None
      val channels = getChannelCount(image.format)
      val dataSize = newWidth * newHeight * channels
      val newData = malloc(dataSize.toLong)
      if newData == null then return None
      var y = 0
      while y < newHeight do
          var x = 0
          while x < newWidth do
              val srcX = (x * image.width / newWidth).min(image.width - 1)
              val srcY = (y * image.height / newHeight).min(image.height - 1)
              val srcOffset = (srcY * image.width + srcX) * channels
              val dstOffset = (y * newWidth + x) * channels
              var c = 0
              while c < channels do
                  newData.asInstanceOf[Ptr[Byte]](dstOffset + c) = image.data(srcOffset + c)
                  c += 1
              x += 1
          y += 1
      Some(Image(newData.asInstanceOf[Ptr[Byte]], newWidth, newHeight, 1, image.format))

  def flipHorizontal(image: Image): Unit =
      if !isValid(image) then return
      val channels = getChannelCount(image.format)
      var y = 0
      while y < image.height do
          var x = 0
          while x < image.width / 2 do
              val leftOffset  = (y * image.width + x) * channels
              val rightOffset = (y * image.width + (image.width - 1 - x)) * channels
              var c = 0
              while c < channels do
                  val tmp = image.data(leftOffset + c)
                  image.data(leftOffset + c)  = image.data(rightOffset + c)
                  image.data(rightOffset + c) = tmp
                  c += 1
              x += 1
          y += 1

  def flipVertical(image: Image): Unit =
      if !isValid(image) then return
      val channels = getChannelCount(image.format)
      val rowSize = image.width * channels
      val tmp = malloc(rowSize.toLong)
      if tmp == null then return
      var y = 0
      while y < image.height / 2 do
          val topOffset    = y * rowSize
          val bottomOffset = (image.height - 1 - y) * rowSize
          memcpy(tmp, image.data + topOffset, rowSize.toCSize)
          memcpy(image.data + topOffset, image.data + bottomOffset, rowSize.toCSize)
          memcpy(image.data + bottomOffset, tmp, rowSize.toCSize)
          y += 1
      free(tmp)

  def tint(image: Image, color: Color): Unit =
      if !isValid(image) then return
      val channels = getChannelCount(image.format)
      val total = image.width * image.height
      var i = 0
      while i < total do
          val offset = i * channels
          image.data(offset)     = ((image.data(offset)     & 0xFF) * color.r / 255).toByte
          image.data(offset + 1) = ((image.data(offset + 1) & 0xFF) * color.g / 255).toByte
          image.data(offset + 2) = ((image.data(offset + 2) & 0xFF) * color.b / 255).toByte
          if channels == 4 then
              image.data(offset + 3) = ((image.data(offset + 3) & 0xFF) * color.a / 255).toByte
          i += 1

  def toGrayscale(image: Image): Unit =
      if !isValid(image) then return
      val channels = getChannelCount(image.format)
      if channels < 3 then return
      val total = image.width * image.height
      var i = 0
      while i < total do
          val offset = i * channels
          val r = image.data(offset)     & 0xFF
          val g = image.data(offset + 1) & 0xFF
          val b = image.data(offset + 2) & 0xFF
          val lum = (0.299f * r + 0.587f * g + 0.114f * b).toInt.toByte
          image.data(offset)     = lum
          image.data(offset + 1) = lum
          image.data(offset + 2) = lum
          i += 1

  def create(width: Int, height: Int, color: Color): Option[Image] =
      if width <= 0 || height <= 0 then return None
      val channels = 4
      val dataSize = width * height * channels
      val newData = malloc(dataSize.toLong)
      if newData == null then return None
      var i = 0
      while i < width * height do
          val offset = i * channels
          newData.asInstanceOf[Ptr[Byte]](offset)     = color.r.toByte
          newData.asInstanceOf[Ptr[Byte]](offset + 1) = color.g.toByte
          newData.asInstanceOf[Ptr[Byte]](offset + 2) = color.b.toByte
          newData.asInstanceOf[Ptr[Byte]](offset + 3) = color.a.toByte
          i += 1
      Some(Image(newData.asInstanceOf[Ptr[Byte]], width, height, 1, PixelFormat.UncompressedR8G8B8A8.value))

  def createGradient(width: Int, height: Int, from: Color, to: Color, horizontal: Boolean): Option[Image] =
      if width <= 0 || height <= 0 then return None
      val channels = 4
      val dataSize = width * height * channels
      val newData = malloc(dataSize.toLong)
      if newData == null then return None
      var y = 0
      while y < height do
          var x = 0
          while x < width do
              val t = if horizontal then x.toFloat / (width - 1).max(1)
                      else y.toFloat / (height - 1).max(1)
              val r = (from.r + (to.r - from.r) * t).toInt.max(0).min(255)
              val g = (from.g + (to.g - from.g) * t).toInt.max(0).min(255)
              val b = (from.b + (to.b - from.b) * t).toInt.max(0).min(255)
              val a = (from.a + (to.a - from.a) * t).toInt.max(0).min(255)
              val offset = (y * width + x) * channels
              newData.asInstanceOf[Ptr[Byte]](offset)     = r.toByte
              newData.asInstanceOf[Ptr[Byte]](offset + 1) = g.toByte
              newData.asInstanceOf[Ptr[Byte]](offset + 2) = b.toByte
              newData.asInstanceOf[Ptr[Byte]](offset + 3) = a.toByte
              x += 1
          y += 1
      Some(Image(newData.asInstanceOf[Ptr[Byte]], width, height, 1, PixelFormat.UncompressedR8G8B8A8.value))

  def createCheckerboard(width: Int, height: Int, checksX: Int, checksY: Int, col1: Color, col2: Color): Option[Image] =
      if width <= 0 || height <= 0 then return None
      val channels = 4
      val dataSize = width * height * channels
      val newData = malloc(dataSize.toLong)
      if newData == null then return None
      val checkW = (width  / checksX).max(1)
      val checkH = (height / checksY).max(1)
      var y = 0
      while y < height do
          var x = 0
          while x < width do
              val color = if ((x / checkW) + (y / checkH)) % 2 == 0 then col1 else col2
              val offset = (y * width + x) * channels
              newData.asInstanceOf[Ptr[Byte]](offset)     = color.r.toByte
              newData.asInstanceOf[Ptr[Byte]](offset + 1) = color.g.toByte
              newData.asInstanceOf[Ptr[Byte]](offset + 2) = color.b.toByte
              newData.asInstanceOf[Ptr[Byte]](offset + 3) = color.a.toByte
              x += 1
          y += 1
      Some(Image(newData.asInstanceOf[Ptr[Byte]], width, height, 1, PixelFormat.UncompressedR8G8B8A8.value))

  def drawPixel(image: Image, x: Int, y: Int, color: Color): Unit =
      if !isValid(image) then return
      if x < 0 || x >= image.width || y < 0 || y >= image.height then return
      val channels = getChannelCount(image.format)
      val offset = (y * image.width + x) * channels
      image.data(offset)     = color.r.toByte
      image.data(offset + 1) = color.g.toByte
      image.data(offset + 2) = color.b.toByte
      if channels == 4 then image.data(offset + 3) = color.a.toByte

  def drawLine(image: Image, start: Vector2, end: Vector2, color: Color): Unit =
      if !isValid(image) then return
      var x0 = start.x.toInt
      var y0 = start.y.toInt
      val x1 = end.x.toInt
      val y1 = end.y.toInt
      val dx = math.abs(x1 - x0)
      val dy = math.abs(y1 - y0)
      val sx = if x0 < x1 then 1 else -1
      val sy = if y0 < y1 then 1 else -1
      var err = dx - dy
      var running = true
      while running do
          drawPixel(image, x0, y0, color)
          if x0 == x1 && y0 == y1 then
              running = false
          else
              val e2 = 2 * err
              if e2 > -dy then
                  err -= dy
                  x0  += sx
              if e2 < dx then
                  err += dx
                  y0  += sy

  def drawRect(image: Image, rect: Rectangle, color: Color): Unit =
      if !isValid(image) then return
      val x0 = rect.x.toInt.max(0)
      val y0 = rect.y.toInt.max(0)
      val x1 = (rect.x + rect.width).toInt.min(image.width)
      val y1 = (rect.y + rect.height).toInt.min(image.height)
      var y = y0
      while y < y1 do
          var x = x0
          while x < x1 do
              drawPixel(image, x, y, color)
              x += 1
          y += 1

  def drawCircle(image: Image, center: Vector2, radius: Float, color: Color): Unit =
      if !isValid(image) then return
      val cx = center.x.toInt
      val cy = center.y.toInt
      val r  = radius.toInt
      var y = -r
      while y <= r do
          var x = -r
          while x <= r do
              if x * x + y * y <= r * r then
                  drawPixel(image, cx + x, cy + y, color)
              x += 1
          y += 1

  def drawImage(dst: Image, src: Image, position: Vector2): Unit =
      if !isValid(dst) || !isValid(src) then return
      val dstChannels = getChannelCount(dst.format)
      val srcChannels = getChannelCount(src.format)
      var y = 0
      while y < src.height do
          var x = 0
          while x < src.width do
              val dstX = x + position.x.toInt
              val dstY = y + position.y.toInt
              if dstX >= 0 && dstX < dst.width && dstY >= 0 && dstY < dst.height then
                  val srcOffset = (y * src.width + x) * srcChannels
                  val sr = src.data(srcOffset)     & 0xFF
                  val sg = src.data(srcOffset + 1) & 0xFF
                  val sb = src.data(srcOffset + 2) & 0xFF
                  val sa = if srcChannels == 4 then src.data(srcOffset + 3) & 0xFF else 255
                  if sa == 255 then
                      drawPixel(dst, dstX, dstY, Color(sr, sg, sb, sa))
                  else if sa > 0 then
                      val dstOffset = (dstY * dst.width + dstX) * dstChannels
                      val dr = dst.data(dstOffset)     & 0xFF
                      val dg = dst.data(dstOffset + 1) & 0xFF
                      val db = dst.data(dstOffset + 2) & 0xFF
                      val a  = sa / 255.0f
                      val r  = (sr * a + dr * (1 - a)).toInt
                      val g  = (sg * a + dg * (1 - a)).toInt
                      val b  = (sb * a + db * (1 - a)).toInt
                      drawPixel(dst, dstX, dstY, Color(r, g, b, 255))
              x += 1
          y += 1