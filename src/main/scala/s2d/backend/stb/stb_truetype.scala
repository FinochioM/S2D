package s2d.backend.stb


import scalanative.unsafe.*
import scalanative.unsigned.*

opaque type stbtt_bakedchar = CStruct7[CShort, CShort, CShort, CShort, CFloat, CFloat, CFloat]

object stbtt_bakedchar:
  given _tag: Tag[stbtt_bakedchar] =
    Tag.materializeCStruct7Tag[CShort, CShort, CShort, CShort, CFloat, CFloat, CFloat]
  extension (s: stbtt_bakedchar)
    inline def x0: CShort       = s._1
    inline def x0_=(v: CShort)  = !s.at1 = v
    inline def y0: CShort       = s._2
    inline def y0_=(v: CShort)  = !s.at2 = v
    inline def x1: CShort       = s._3
    inline def x1_=(v: CShort)  = !s.at3 = v
    inline def y1: CShort       = s._4
    inline def y1_=(v: CShort)  = !s.at4 = v
    inline def xoff: CFloat     = s._5
    inline def xoff_=(v: CFloat) = !s.at5 = v
    inline def yoff: CFloat     = s._6
    inline def yoff_=(v: CFloat) = !s.at6 = v
    inline def xadvance: CFloat  = s._7
    inline def xadvance_=(v: CFloat) = !s.at7 = v

opaque type stbtt_aligned_quad = CStruct8[CFloat, CFloat, CFloat, CFloat, CFloat, CFloat, CFloat, CFloat]

object stbtt_aligned_quad:
  given _tag: Tag[stbtt_aligned_quad] =
    Tag.materializeCStruct8Tag[CFloat, CFloat, CFloat, CFloat, CFloat, CFloat, CFloat, CFloat]
  extension (q: stbtt_aligned_quad)
    inline def x0: CFloat = q._1
    inline def y0: CFloat = q._2
    inline def s0: CFloat = q._3
    inline def t0: CFloat = q._4
    inline def x1: CFloat = q._5
    inline def y1: CFloat = q._6
    inline def s1: CFloat = q._7
    inline def t1: CFloat = q._8

@extern
@link("stb")
object stbtt:
  def stbtt_BakeFontBitmap(
      data: Ptr[Byte],
      offset: CInt,
      pixel_height: CFloat,
      pixels: Ptr[Byte],
      pw: CInt,
      ph: CInt,
      first_char: CInt,
      num_chars: CInt,
      chardata: Ptr[stbtt_bakedchar]
  ): CInt = extern

  def stbtt_GetBakedQuad(
      chardata: Ptr[stbtt_bakedchar],
      pw: CInt,
      ph: CInt,
      char_index: CInt,
      xpos: Ptr[CFloat],
      ypos: Ptr[CFloat],
      q: Ptr[stbtt_aligned_quad],
      opengl_fillrule: CInt
  ): Unit = extern