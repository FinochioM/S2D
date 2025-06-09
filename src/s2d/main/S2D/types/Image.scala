package S2D.types

import java.nio.ByteBuffer

case class Image(
           data: ByteBuffer,
           width: Int,
           height: Int,
           mipmaps: Int,
           format: Int,
           )