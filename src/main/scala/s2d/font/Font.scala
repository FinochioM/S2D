package s2d.font

import s2d.types.{FontType, FontData}
import s2d.backend.stb.{stbtt, stbtt_bakedchar}
import s2d.backend.gl.GL.*
import s2d.backend.gl.GLExtras.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import scalanative.libc.stdlib.*
import scalanative.libc.stdio.*

object Font:
  private val ATLAS_WIDTH  = 512
  private val ATLAS_HEIGHT = 512
  private val FIRST_CHAR = 32
  private val NUM_CHARS = 96

  def load(path: String, size: Float): Option[FontType] =
    val pathBytes = path.getBytes("UTF-8")
    val pathBuf = stackalloc[CChar](pathBytes.length + 1)
    var i = 0
    while i < pathBytes.length do
      pathBuf(i) = pathBytes(i)
      i += 1
    pathBuf(pathBytes.length) = 0.toByte
    loadFromCString(pathBuf, size)

  def unload(font: FontType): Unit =
    val texId = stackalloc[GLuint](1)
    !texId = FontType.data(font).textureId.toUInt
    glDeleteTextures(1.toUInt, texId)

  private def loadFromCString(path: CString, size: Float): Option[FontType] =
    val file = fopen(path, c"rb")
    if file == null then return None

    fseek(file, 0, 2)
    val fileSize = ftell(file).toInt
    fseek(file, 0, 0)

    val ttfBuffer = malloc(fileSize.toUSize).asInstanceOf[Ptr[Byte]]
    if ttfBuffer == null then
      fclose(file)
      return None

    fread(ttfBuffer, 1.toUSize, fileSize.toUSize, file)
    fclose(file)

    val bitmap = malloc((ATLAS_WIDTH * ATLAS_HEIGHT).toUSize).asInstanceOf[Ptr[Byte]]
    if bitmap == null then
      free(ttfBuffer)
      return None

    val charDataSize = (NUM_CHARS * sizeof[stbtt_bakedchar].toLong).toUSize
    val charData = malloc(charDataSize).asInstanceOf[Ptr[stbtt_bakedchar]]
    if charData == null then
      free(ttfBuffer)
      free(bitmap)
      return None

    val result = stbtt.stbtt_BakeFontBitmap(
      ttfBuffer, 0, size,
      bitmap, ATLAS_WIDTH, ATLAS_HEIGHT,
      FIRST_CHAR, NUM_CHARS,
      charData
    )

    free(ttfBuffer)

    if result <= 0 then
      free(bitmap)
      free(charData)
      return None

    val texId = stackalloc[GLuint](1)
    glGenTextures(1.toUInt, texId)
    glBindTexture(GL_TEXTURE_2D.toUInt, !texId)
    glTexImage2D(
      GL_TEXTURE_2D.toUInt, 0, GL_RED.toInt,
      ATLAS_WIDTH.toUInt, ATLAS_HEIGHT.toUInt, 0,
      GL_RED.toUInt, GL_UNSIGNED_BYTE.toUInt,
      bitmap
    )
    glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_MIN_FILTER.toUInt, GL_LINEAR.toInt)
    glTexParameteri(GL_TEXTURE_2D.toUInt, GL_TEXTURE_MAG_FILTER.toUInt, GL_LINEAR.toInt)
    glBindTexture(GL_TEXTURE_2D.toUInt, 0.toUInt)

    free(bitmap)

    val arr = new Array[Float](NUM_CHARS * 7)
    var j = 0
    while j < NUM_CHARS do
      val c = charData + j
      arr(j*7 + 0) = (!c).x0.toFloat
      arr(j*7 + 1) = (!c).y0.toFloat
      arr(j*7 + 2) = (!c).x1.toFloat
      arr(j*7 + 3) = (!c).y1.toFloat
      arr(j*7 + 4) = (!c).xoff
      arr(j*7 + 5) = (!c).yoff
      arr(j*7 + 6) = (!c).xadvance
      j += 1

    free(charData)

    Some(FontType(FontData((!texId).toInt, arr, ATLAS_WIDTH, ATLAS_HEIGHT, size, FIRST_CHAR, NUM_CHARS)))