package s2d.audio

opaque type Music = Int

object Music:
    private[audio] inline def apply(id: Int): Music = id
    private[audio] inline def id(m: Music): Int = m
