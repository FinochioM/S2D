package s2d.textures

import s2d.textures.Images.*
import s2d.types.*
import s2d.gl.GL.*
import s2d.gl.GLExtras.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import scalanative.libc.stdio.*
import scalanative.libc.stdlib.*
import scalanative.libc.string.*

object Textures:
  def load(fileName: String): Option[Texture2D] =
    Images.load(fileName) match
      case Some(image) =>
        try
          Zone {
            val textureIdArray = alloc[GLuint](1)
            glGenTextures(1.toUInt, textureIdArray)
            val textureId = !textureIdArray
            if textureId == 0.toUInt then return None

            glBindTexture(GL_TEXTURE_2D.toUInt, textureId)

            val (internalFormat, format, dataType) = image.format match
              case f if f == PixelFormat.UncompressedGrayscale.value => (GL_LUMINANCE.toUInt, GL_LUMINANCE.toUInt, GL_UNSIGNED_BYTE.toUInt)
              case f if f == PixelFormat.UncompressedGrayAlpha.value => (GL_LUMINANCE_ALPHA.toUInt, GL_LUMINANCE_ALPHA.toUInt, GL_UNSIGNED_BYTE.toUInt)
              case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB.toUInt, GL_RGB.toUInt, GL_UNSIGNED_BYTE.toUInt)
              case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA.toUInt, GL_RGBA.toUInt, GL_UNSIGNED_BYTE.toUInt)
              case f if f == PixelFormat.UncompressedR5G6B5.value => (GL_RGB.toUInt, GL_RGB.toUInt, GL_UNSIGNED_SHORT_5_6_5.toUInt)
              case f if f == PixelFormat.UncompressedR5G6B5A1.value => (GL_RGBA.toUInt, GL_RGBA.toUInt, GL_UNSIGNED_SHORT_5_5_5_1.toUInt)
              case f if f == PixelFormat.UncompressedR4G4B4A4.value => (GL_RGBA.toUInt, GL_RGBA.toUInt, GL_UNSIGNED_SHORT_4_4_4_4.toUInt)
              case _ => (GL_RGBA.toUInt, GL_RGBA.toUInt, GL_UNSIGNED_BYTE.toUInt)

            glTexImage2D(GL_TEXTURE_2D.toUInt, 0, internalFormat.toInt, image.width.toUInt, image.height.toUInt, 0, format, dataType, image.data)

            glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_WRAP_S.toUInt, GL_REPEAT.toInt)
            glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_WRAP_T.toUInt, GL_REPEAT.toInt)
            glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_MIN_FILTER.toUInt, GL_LINEAR.toInt)
            glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_MAG_FILTER.toUInt, GL_LINEAR.toInt)

            glGenerateMipmap(GL_TEXTURE_2D.toUInt)
            glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)
            Images.unload(image)

            val mipmaps = (math.log(math.max(image.width, image.height)) / math.log(2)).toInt + 1

            Some(Texture2D(textureId.toInt, image.width, image.height, image.format, mipmaps))
          }
        catch
          case _: Exception => None
      case None => None

  def loadFromImage(image: Image): Option[Texture2D] =
    try
      if !Images.isValid(image) then return None

      Zone {
        val textureIdArray = alloc[GLuint](1)
        glGenTextures(1.toUInt, textureIdArray)
        val textureId = !textureIdArray

        if textureId == 0.toUInt then return None

        glBindTexture(GL_TEXTURE_2D.toUInt, textureId)

        val (internalFormat, format, dataType) = image.format match
          case f if f == PixelFormat.UncompressedGrayscale.value => (GL_LUMINANCE.toUInt, GL_LUMINANCE.toUInt, GL_UNSIGNED_BYTE.toUInt)
          case f if f == PixelFormat.UncompressedGrayAlpha.value => (GL_LUMINANCE_ALPHA.toUInt, GL_LUMINANCE_ALPHA.toUInt, GL_UNSIGNED_BYTE.toUInt)
          case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB.toUInt, GL_RGB.toUInt, GL_UNSIGNED_BYTE.toUInt)
          case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA.toUInt, GL_RGBA.toUInt, GL_UNSIGNED_BYTE.toUInt)
          case f if f == PixelFormat.UncompressedR5G6B5.value => (GL_RGB.toUInt, GL_RGB.toUInt, GL_UNSIGNED_SHORT_5_6_5.toUInt)
          case f if f == PixelFormat.UncompressedR5G6B5A1.value => (GL_RGBA.toUInt, GL_RGBA.toUInt, GL_UNSIGNED_SHORT_5_5_5_1.toUInt)
          case f if f == PixelFormat.UncompressedR4G4B4A4.value => (GL_RGBA.toUInt, GL_RGBA.toUInt, GL_UNSIGNED_SHORT_4_4_4_4.toUInt)
          case _ => (GL_RGBA.toUInt, GL_RGBA.toUInt, GL_UNSIGNED_BYTE.toUInt)

        glTexImage2D(GL_TEXTURE_2D.toUInt, 0, internalFormat.toInt, image.width.toUInt, image.height.toUInt, 0, format, dataType, image.data)

        glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_WRAP_S.toUInt, GL_REPEAT.toInt)
        glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_WRAP_T.toUInt, GL_REPEAT.toInt)
        glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_MIN_FILTER.toUInt, GL_LINEAR.toInt)
        glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_MAG_FILTER.toUInt, GL_LINEAR.toInt)

        glGenerateMipmap(GL_TEXTURE_2D.toUInt)

        glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)

        val mipmaps = (math.log(math.max(image.width, image.height)) / math.log(2)).toInt + 1

        Some(Texture2D(textureId.toInt, image.width, image.height, image.format, mipmaps))
      }
    catch
      case _: Exception => None

  def loadCubemap(image: Image, layout: Int): Option[Int] =
    try
      if !Images.isValid(image) then return None

      Zone {
        val textureIdArray = alloc[GLuint](1)
        glGenTextures(1.toUInt, textureIdArray)
        val textureId = !textureIdArray

        if textureId == 0.toUInt then return None

        glBindTexture(GL_TEXTURE_CUBE_MAP.toUInt, textureId)

        val faceSize = image.height
        if image.width != faceSize * 6 then
          val textureDeleteArray = alloc[GLuint](1)
          !textureDeleteArray = textureId
          glDeleteTextures(1.toUInt, textureDeleteArray)
          return None

        val (format, dataType) = image.format match
          case f if f == PixelFormat.UncompressedRGB8.value => (GL_RGB.toUInt, GL_UNSIGNED_BYTE.toUInt)
          case f if f == PixelFormat.UncompressedR8G8B8A8.value => (GL_RGBA.toUInt, GL_UNSIGNED_BYTE.toUInt)
          case _ => (GL_RGBA.toUInt, GL_UNSIGNED_BYTE.toUInt)

        val bytesPerPixel = getBytesPerPixel(image.format)
        val faceDataSize = faceSize * faceSize * bytesPerPixel

        val faces = Array(
          GL_TEXTURE_CUBE_MAP_POSITIVE_X.toUInt, GL_TEXTURE_CUBE_MAP_NEGATIVE_X.toUInt,
          GL_TEXTURE_CUBE_MAP_POSITIVE_Y.toUInt, GL_TEXTURE_CUBE_MAP_NEGATIVE_Y.toUInt,
          GL_TEXTURE_CUBE_MAP_POSITIVE_Z.toUInt, GL_TEXTURE_CUBE_MAP_NEGATIVE_Z.toUInt
        )

        for (i <- faces.indices) {
          val faceData = malloc(faceDataSize.toLong)
          if faceData == null then
            val textureDeleteArray = alloc[GLuint](1)
            !textureDeleteArray = textureId
            glDeleteTextures(1.toUInt, textureDeleteArray)
            return None

          var y = 0
          while y < faceSize do
            val srcOffset = (y * image.width + i * faceSize) * bytesPerPixel
            val dstOffset = y * faceSize * bytesPerPixel
            val rowSize = faceSize * bytesPerPixel

            memcpy(
              faceData.asInstanceOf[Ptr[Byte]] + dstOffset,
              image.data + srcOffset,
              rowSize.toCSize
            )
            y += 1

          glTexImage2D(faces(i), 0, format.toInt, faceSize.toUInt, faceSize.toUInt, 0, format, dataType, faceData.asInstanceOf[Ptr[Byte]])

          free(faceData)
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP.toUInt, GL_TEXTURE_WRAP_S.toUInt, GL_CLAMP_TO_EDGE.toInt)
        glTexParameteri(GL_TEXTURE_CUBE_MAP.toUInt, GL_TEXTURE_WRAP_T.toUInt, GL_CLAMP_TO_EDGE.toInt)
        glTexParameteri(GL_TEXTURE_CUBE_MAP.toUInt, GL_TEXTURE_WRAP_R.toUInt, GL_CLAMP_TO_EDGE.toInt)
        glTexParameteri(GL_TEXTURE_CUBE_MAP.toUInt, GL_TEXTURE_MIN_FILTER.toUInt, GL_LINEAR.toInt)
        glTexParameteri(GL_TEXTURE_CUBE_MAP.toUInt, GL_TEXTURE_MAG_FILTER.toUInt, GL_LINEAR.toInt)

        glBindTexture(GL_TEXTURE_CUBE_MAP.toUInt, 0.toUInt)

        Some(textureId.toInt)
      }
    catch
      case _: Exception => None

  def loadRenderTexture(width: Int, height: Int): Option[RenderTexture2D] =
    try
      if width <= 0 || height <= 0 then return None

      Zone {
        val framebufferIdArray = alloc[GLuint](1)
        glGenFramebuffers(1.toUInt, framebufferIdArray)
        val framebufferId = !framebufferIdArray
        if framebufferId == 0.toUInt then return None

        val colorTextureIdArray = alloc[GLuint](1)
        glGenTextures(1.toUInt, colorTextureIdArray)
        val colorTextureId = !colorTextureIdArray
        if colorTextureId == 0.toUInt then
          val framebufferDeleteArray = alloc[GLuint](1)
          !framebufferDeleteArray = framebufferId
          glDeleteFramebuffers(1.toUInt, framebufferDeleteArray)
          return None

        val depthRenderbufferIdArray = alloc[GLuint](1)
        glGenRenderbuffers(1.toUInt, depthRenderbufferIdArray)
        val depthRenderbufferId = !depthRenderbufferIdArray
        if depthRenderbufferId == 0.toUInt then
          val framebufferDeleteArray = alloc[GLuint](1)
          !framebufferDeleteArray = framebufferId
          glDeleteFramebuffers(1.toUInt, framebufferDeleteArray)
          val textureDeleteArray = alloc[GLuint](1)
          !textureDeleteArray = colorTextureId
          glDeleteTextures(1.toUInt, textureDeleteArray)
          return None

        glBindTexture(GL_TEXTURE_2D.toUInt, colorTextureId)
        glTexImage2D(GL_TEXTURE_2D.toUInt, 0, GL_RGBA.toInt, width.toUInt, height.toUInt, 0, GL_RGBA.toUInt, GL_UNSIGNED_BYTE.toUInt, null.asInstanceOf[Ptr[Byte]])
        glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_MIN_FILTER.toUInt, GL_LINEAR.toInt)
        glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_MAG_FILTER.toUInt, GL_LINEAR.toInt)
        glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_WRAP_S.toUInt, GL_CLAMP_TO_EDGE.toInt)
        glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_WRAP_T.toUInt, GL_CLAMP_TO_EDGE.toInt)
        glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)

        glBindRenderbuffer(GL_RENDERBUFFER.toUInt, depthRenderbufferId)
        glRenderbufferStorage(GL_RENDERBUFFER.toUInt, GL_DEPTH_COMPONENT.toUInt, width.toUInt, height.toUInt)
        glBindRenderbuffer(GL_RENDERBUFFER.toUInt, 0.toUInt)

        glBindFramebuffer(GL_FRAMEBUFFER.toUInt, framebufferId)
        glFramebufferTexture2D(GL_FRAMEBUFFER.toUInt, GL_COLOR_ATTACHMENT0.toUInt, GL_TEXTURE_2D.toUInt, colorTextureId, 0)
        glFramebufferRenderbuffer(GL_FRAMEBUFFER.toUInt, GL_DEPTH_ATTACHMENT.toUInt, GL_RENDERBUFFER.toUInt, depthRenderbufferId)

        val status = glCheckFramebufferStatus(GL_FRAMEBUFFER.toUInt)
        if status != GL_FRAMEBUFFER_COMPLETE.toUInt then
          val framebufferDeleteArray = alloc[GLuint](1)
          !framebufferDeleteArray = framebufferId
          glDeleteFramebuffers(1.toUInt, framebufferDeleteArray)
          val textureDeleteArray = alloc[GLuint](1)
          !textureDeleteArray = colorTextureId
          glDeleteTextures(1.toUInt, textureDeleteArray)
          val renderbufferDeleteArray = alloc[GLuint](1)
          !renderbufferDeleteArray = depthRenderbufferId
          glDeleteRenderbuffers(1.toUInt, renderbufferDeleteArray)
          glBindFramebuffer(GL_FRAMEBUFFER.toUInt, 0.toUInt)
          return None

        glBindFramebuffer(GL_FRAMEBUFFER.toUInt, 0.toUInt)

        val colorTexture = Texture2D(colorTextureId.toInt, width, height, PixelFormat.UncompressedR8G8B8A8.value, 1)
        Some(RenderTexture2D(framebufferId.toInt, colorTexture, depthRenderbufferId.toInt))
      }
    catch
      case _: Exception => None

  def isValid(texture: Texture2D): Boolean =
    try
      texture.id > 0 && glIsTexture(texture.id.toUInt) != 0.toUByte
    catch
      case _: Exception => false

  def unload(texture: Texture2D): Unit =
    try
      if texture.id > 0 then
        Zone {
          val textureArray = alloc[GLuint](1)
          !textureArray = texture.id.toUInt
          glDeleteTextures(1.toUInt, textureArray)
        }
    catch
      case _: Exception =>

  def isRenderTextureValid(target: RenderTexture2D): Boolean =
    try
      target.id > 0 &&
        glIsFramebuffer(target.id.toUInt) != 0.toUByte &&
        isValid(target.texture) &&
        target.depth > 0 &&
        glIsRenderbuffer(target.depth.toUInt) != 0.toUByte
    catch
      case _: Exception => false

  def unloadRenderTexture(target: RenderTexture2D): Unit =
    try
      Zone {
        if target.id > 0 then
          val framebufferArray = alloc[GLuint](1)
          !framebufferArray = target.id.toUInt
          glDeleteFramebuffers(1.toUInt, framebufferArray)

        unload(target.texture)

        if target.depth > 0 then
          val renderbufferArray = alloc[GLuint](1)
          !renderbufferArray = target.depth.toUInt
          glDeleteRenderbuffers(1.toUInt, renderbufferArray)
      }
    catch
      case _: Exception =>