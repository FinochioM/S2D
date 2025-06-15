package s2d.types

import scalanative.unsafe.*

case class Image(
    data: Ptr[Byte],
    width: Int,
    height: Int,
    mipmaps: Int,
    format: Int
)
