package S2D.types

enum PixelFormat(val value: Int, val bytesPerPixel: Int, val description: String):
  case UncompressedGrayscale extends PixelFormat(1, 1, "8 bit per pixel, no alpha")
  case UncompressedGrayAlpha extends PixelFormat(2, 2, "8*2 bpp (2 channels)")
  case UncompressedR5G6B5 extends PixelFormat(3, 2, "16 bpp")
  case UncompressedRGB8 extends PixelFormat(4, 3, "24 bpp")
  case UncompressedR5G6B5A1 extends PixelFormat(5, 2, "16 bpp (1 bit alpha)")
  case UncompressedR4G4B4A4 extends PixelFormat(6, 2, "16 bpp (4 bit alpha)")
  case UncompressedR8G8B8A8 extends PixelFormat(7, 4, "32 bpp")
  case UncompressedR32 extends PixelFormat(8, 4, "32 bpp (1 channel - float)")
  case UncompressedR32G32B32 extends PixelFormat(9, 12, "32*3 bpp (3 channels - float)")
  case UncompressedR32G32B32A32 extends PixelFormat(10, 16, "32*4 bpp (4 channels - float)")

object PixelFormat:
  def fromValue(value: Int): Option[PixelFormat] =
    PixelFormat.values.find(_.value == value)