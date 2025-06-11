package S2D.textures

import S2D.types.{Image, PixelFormat, Rectangle, RenderTexture2D, Texture2D}
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.*
import org.lwjgl.opengl.GL30.*

object Textures:
  def load(fileName: String): Option[Texture2D] =
    Images.load(fileName) match
      case Some(image) =>
        try
          val textureId = glGenTextures()
          if textureId == 0 then return None

          glBindTexture(GL_TEXTURE_2D, textureId)

          val (internalFormat, format, dataType) = image.format match
            case f if f == PixelFormat.UncompressedGrayscale.value => (GL_LUMINANCE, GL_LUMINANCE, GL_UNSIGNED_BYTE)
            case f if f == PixelFormat.UncompressedGrayAlpha.value => (GL_LUMINANCE_ALPHA, GL_LUMINANCE_ALPHA, GL_UNSIGNED_BYTE)
            case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB, GL_RGB, GL_UNSIGNED_BYTE)
            case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE)
            case f if f == PixelFormat.UncompressedR5G6B5.value => (GL_RGB, GL_RGB, GL_UNSIGNED_SHORT_5_6_5)
            case f if f == PixelFormat.UncompressedR5G6B5A1.value => (GL_RGBA, GL_RGBA, GL_UNSIGNED_SHORT_5_5_5_1)
            case f if f == PixelFormat.UncompressedR4G4B4A4.value => (GL_RGBA, GL_RGBA, GL_UNSIGNED_SHORT_4_4_4_4)
            case _ => (GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE)

          glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, image.width, image.height, 0, format, dataType, image.data)

          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

          glGenerateMipmap(GL_TEXTURE_2D)
          glBindTexture(GL_TEXTURE_2D, 0)
          Images.unloadImage(image)

          val mipmaps = (math.log(math.max(image.width, image.height)) / math.log(2)).toInt + 1

          Some(Texture2D(textureId, image.width, image.height, mipmaps, image.format))
        catch
          case _: Exception => None
      case None => None
  def loadFromImage(image: Image): Option[Texture2D] =
    try
      if !Images.isValid(image) then return None

      val textureId = glGenTextures()

      if textureId == 0 then return None

      glBindTexture(GL_TEXTURE_2D, textureId)

      val (internalFormat, format, dataType) = image.format match
        case f if f == PixelFormat.UncompressedGrayscale.value => (GL_LUMINANCE, GL_LUMINANCE, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedGrayAlpha.value => (GL_LUMINANCE_ALPHA, GL_LUMINANCE_ALPHA, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB, GL_RGB, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedR5G6B5.value => (GL_RGB, GL_RGB, GL_UNSIGNED_SHORT_5_6_5)
        case f if f == PixelFormat.UncompressedR5G6B5A1.value => (GL_RGBA, GL_RGBA, GL_UNSIGNED_SHORT_5_5_5_1)
        case f if f == PixelFormat.UncompressedR4G4B4A4.value => (GL_RGBA, GL_RGBA, GL_UNSIGNED_SHORT_4_4_4_4)
        case _ => (GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE)

      glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, image.width, image.height, 0, format, dataType, image.data)

      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

      glGenerateMipmap(GL_TEXTURE_2D)

      glBindTexture(GL_TEXTURE_2D, 0)

      val mipmaps = (math.log(math.max(image.width, image.height)) / math.log(2)).toInt + 1

      Some(Texture2D(textureId, image.width, image.height, mipmaps, image.format))
    catch
      case _: Exception => None
  def loadCubemap(image: Image, layout: Int): Option[Int] =
    try
      import org.lwjgl.opengl.GL13.*

      if !Images.isValid(image) then return None

      val textureId = glGenTextures()

      if textureId == 0 then return None

      glBindTexture(GL_TEXTURE_CUBE_MAP, textureId)

      val faceSize = image.height

      if image.width != faceSize * 6 then
        glDeleteTextures(textureId)
        return None

      val (format, dataType) = image.format match
        case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA, GL_UNSIGNED_BYTE)
        case _ => (GL_RGBA, GL_UNSIGNED_BYTE)

      val bytesPerPixel = Images.getBytesPerPixel(image.format)
      val faceDataSize = faceSize * faceSize * bytesPerPixel

      val faces = Array(
        GL_TEXTURE_CUBE_MAP_POSITIVE_X, GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
        GL_TEXTURE_CUBE_MAP_POSITIVE_Y, GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
        GL_TEXTURE_CUBE_MAP_POSITIVE_Z, GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
      )

      for (i <- faces.indices) {
        val faceData = java.nio.ByteBuffer.allocateDirect(faceDataSize)

        for (y <- 0 until faceSize) {
          val srcOffset = (y * image.width + i * faceSize) * bytesPerPixel
          image.data.position(srcOffset)
          val rowData = new Array[Byte](faceSize * bytesPerPixel)
          image.data.get(rowData)
          faceData.put(rowData)
        }

        faceData.flip()
        glTexImage2D(faces(i), 0, format, faceSize, faceSize, 0, format, dataType, faceData)
      }

      glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
      glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
      glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE)
      glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
      glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

      glBindTexture(GL_TEXTURE_CUBE_MAP, 0)

      Some(textureId)
    catch
      case _: Exception => None
  def loadRenderTexture(width: Int, height: Int): Option[RenderTexture2D] =
    try
      import org.lwjgl.opengl.GL30.*

      if width <= 0 || height <= 0 then return None

      val framebufferId = glGenFramebuffers()
      if framebufferId == 0 then return None

      val colorTextureId = glGenTextures()
      if colorTextureId == 0 then
        glDeleteFramebuffers(framebufferId)
        return None

      val depthRenderbufferId = glGenRenderbuffers()
      if depthRenderbufferId == 0 then
        glDeleteFramebuffers(framebufferId)
        glDeleteTextures(colorTextureId)
        return None

      glBindTexture(GL_TEXTURE_2D, colorTextureId)
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null.asInstanceOf[java.nio.ByteBuffer])
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
      glBindTexture(GL_TEXTURE_2D, 0)

      glBindRenderbuffer(GL_RENDERBUFFER, depthRenderbufferId)
      glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height)
      glBindRenderbuffer(GL_RENDERBUFFER, 0)

      glBindFramebuffer(GL_FRAMEBUFFER, framebufferId)
      glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorTextureId, 0)
      glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderbufferId)

      val status = glCheckFramebufferStatus(GL_FRAMEBUFFER)
      if status != GL_FRAMEBUFFER_COMPLETE then
        glDeleteFramebuffers(framebufferId)
        glDeleteTextures(colorTextureId)
        glDeleteRenderbuffers(depthRenderbufferId)
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        return None

      glBindFramebuffer(GL_FRAMEBUFFER, 0)

      val colorTexture = Texture2D(colorTextureId, width, height, 1, PixelFormat.UncompressedR8G8B8A8.value)
      Some(RenderTexture2D(framebufferId, colorTexture, depthRenderbufferId))
    catch
      case _: Exception => None
  def isValid(texture: Texture2D): Boolean =
    try
      texture.id > 0 && glIsTexture(texture.id)
    catch
      case _: Exception => false
  def unload(texture: Texture2D): Unit =
    try
      if texture.id > 0 then
        glDeleteTextures(texture.id)
    catch
      case _: Exception =>
  def isRenderTextureValid(target: RenderTexture2D): Boolean =
    try
      import org.lwjgl.opengl.GL30.*
      target.id > 0 &&
        glIsFramebuffer(target.id) &&
        isValid(target.texture) &&
        target.depth > 0 &&
        glIsRenderbuffer(target.depth)
    catch
      case _: Exception => false
  def unloadRenderTexture(target: RenderTexture2D): Unit =
    try
      import org.lwjgl.opengl.GL30.*

      if target.id > 0 then
        glDeleteFramebuffers(target.id)

      unload(target.texture)

      if target.depth > 0 then
        glDeleteRenderbuffers(target.depth)
    catch
      case _: Exception =>
  def update(texture: Texture2D, pixels: java.nio.ByteBuffer): Unit =
    try
      if !isValid(texture) || pixels == null then return

      glBindTexture(GL_TEXTURE_2D, texture.id)

      val (format, dataType) = texture.format match
        case f if f == PixelFormat.UncompressedGrayscale.value => (GL_LUMINANCE, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedGrayAlpha.value => (GL_LUMINANCE_ALPHA, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedR5G6B5.value => (GL_RGB, GL_UNSIGNED_SHORT_5_6_5)
        case f if f == PixelFormat.UncompressedR5G6B5A1.value => (GL_RGBA, GL_UNSIGNED_SHORT_5_5_5_1)
        case f if f == PixelFormat.UncompressedR4G4B4A4.value => (GL_RGBA, GL_UNSIGNED_SHORT_4_4_4_4)
        case _ => (GL_RGBA, GL_UNSIGNED_BYTE)

      glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, texture.width, texture.height, format, dataType, pixels)
      glBindTexture(GL_TEXTURE_2D, 0)
    catch
      case _: Exception =>
  def updateRec(texture: Texture2D, rec: Rectangle, pixels: java.nio.ByteBuffer): Unit =
    try
      if !isValid(texture) || pixels == null then return

      if rec.x < 0 || rec.y < 0 ||
        rec.x + rec.width > texture.width ||
        rec.y + rec.height > texture.height then return

      glBindTexture(GL_TEXTURE_2D, texture.id)

      val (format, dataType) = texture.format match
        case f if f == PixelFormat.UncompressedGrayscale.value => (GL_LUMINANCE, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedGrayAlpha.value => (GL_LUMINANCE_ALPHA, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA, GL_UNSIGNED_BYTE)
        case f if f == PixelFormat.UncompressedR5G6B5.value => (GL_RGB, GL_UNSIGNED_SHORT_5_6_5)
        case f if f == PixelFormat.UncompressedR5G6B5A1.value => (GL_RGBA, GL_UNSIGNED_SHORT_5_5_5_1)
        case f if f == PixelFormat.UncompressedR4G4B4A4.value => (GL_RGBA, GL_UNSIGNED_SHORT_4_4_4_4)
        case _ => (GL_RGBA, GL_UNSIGNED_BYTE)

      glTexSubImage2D(GL_TEXTURE_2D, 0, rec.x.toInt, rec.y.toInt, rec.width.toInt, rec.height.toInt, format, dataType, pixels)
      glBindTexture(GL_TEXTURE_2D, 0)
    catch
      case _: Exception =>