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