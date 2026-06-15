package s2d.audio

opaque type Sound = Int

object Sound:
    private[audio] inline def apply(id: Int): Sound = id
    private[audio] inline def id(s: Sound): Int = s