package S2D.types

import java.nio.ByteBuffer

case class Image(
           data: ByteBuffer,
           width: Int,
           height: Int,
           mipmaps: Int,
           format: Int,
           )

object PixelFormat:
  val UNCOMPRESSED_GRAYSCALE = 1 // 8 bit per pixel no alpha
  val UNCOMPRESSED_GRAY_ALPHA = 2 // 8*2 bpp (2 channels)
  val UNCOMPRESSED_R5G6B5 = 3 // 16 bpp
  val UNCOMPRESSED_RGB8 = 4 // 24 bpp
  val UNCOMPRESSED_R5G6B5A1 = 5 // 16 bpp (1 bit alpha)
  val UNCOMPRESSED_R4G4B4A4 = 6 // 16 bpp (4 bit alpha)
  val UNCOMPRESSED_R8G8B8A8 = 7 // 32 bpp
  val UNCOMPRESSED_R32 = 8 // 32 bpp (1 channel - float)
  val UNCOMPRESSED_R32G32B32 = 9 // 32*3 bpp (3 channels - float)
  val UNCOMPRESSED_R32G32B32A32 = 10 // 32*4 bpp (4 channels - float)