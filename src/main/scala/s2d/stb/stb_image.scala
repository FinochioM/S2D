package stb_image

import _root_.scala.scalanative.unsafe.*
import _root_.scala.scalanative.unsigned.*
import _root_.scala.scalanative.libc.*
import _root_.scala.scalanative.*

object aliases:
  import _root_.stb_image.aliases.*
  import _root_.stb_image.structs.*
  type FILE = libc.stdio.FILE
  object FILE:
    val _tag: Tag[FILE] = summon[Tag[libc.stdio.FILE]]
    inline def apply(inline o: libc.stdio.FILE): FILE = o
    extension (v: FILE)
      inline def value: libc.stdio.FILE = v

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  opaque type stbi_uc = CUnsignedChar
  object stbi_uc:
    given _tag: Tag[stbi_uc] = Tag.UByte
    inline def apply(inline o: CUnsignedChar): stbi_uc = o
    extension (v: stbi_uc)
      inline def value: CUnsignedChar = v

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  opaque type stbi_us = CUnsignedShort
  object stbi_us:
    given _tag: Tag[stbi_us] = Tag.UShort
    inline def apply(inline o: CUnsignedShort): stbi_us = o
    extension (v: stbi_us)
      inline def value: CUnsignedShort = v

object structs:
  import _root_.stb_image.aliases.*
  import _root_.stb_image.structs.*
  /**
   * ///////////////////////////////////////////////////////////////////////////

   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  opaque type stbi_io_callbacks = CStruct3[CFuncPtr3[Ptr[Byte], CString, CInt, CInt], CFuncPtr2[Ptr[Byte], CInt, Unit], CFuncPtr1[Ptr[Byte], CInt]]
  object stbi_io_callbacks:
    given _tag: Tag[stbi_io_callbacks] = Tag.materializeCStruct3Tag[CFuncPtr3[Ptr[Byte], CString, CInt, CInt], CFuncPtr2[Ptr[Byte], CInt, Unit], CFuncPtr1[Ptr[Byte], CInt]]
    def apply()(using Zone): Ptr[stbi_io_callbacks] = scala.scalanative.unsafe.alloc[stbi_io_callbacks](1)
    def apply(read : CFuncPtr3[Ptr[Byte], CString, CInt, CInt], skip : CFuncPtr2[Ptr[Byte], CInt, Unit], eof : CFuncPtr1[Ptr[Byte], CInt])(using Zone): Ptr[stbi_io_callbacks] =
      val ____ptr = apply()
      (!____ptr).read = read
      (!____ptr).skip = skip
      (!____ptr).eof = eof
      ____ptr
    extension (struct: stbi_io_callbacks)
      def read : CFuncPtr3[Ptr[Byte], CString, CInt, CInt] = struct._1
      def read_=(value: CFuncPtr3[Ptr[Byte], CString, CInt, CInt]): Unit = !struct.at1 = value
      def skip : CFuncPtr2[Ptr[Byte], CInt, Unit] = struct._2
      def skip_=(value: CFuncPtr2[Ptr[Byte], CInt, Unit]): Unit = !struct.at2 = value
      def eof : CFuncPtr1[Ptr[Byte], CInt] = struct._3
      def eof_=(value: CFuncPtr1[Ptr[Byte], CInt]): Unit = !struct.at3 = value


@extern
private[stb_image] object extern_functions:
  import _root_.stb_image.aliases.*
  import _root_.stb_image.structs.*
  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_convert_iphone_png_to_rgb(flag_true_if_should_convert : CInt): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_convert_iphone_png_to_rgb_thread(flag_true_if_should_convert : CInt): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_failure_reason(): CString = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_hdr_to_ldr_gamma(gamma : Float): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_hdr_to_ldr_scale(scale : Float): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_image_free(retval_from_stbi_load : Ptr[Byte]): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_info(filename : CString, x : Ptr[CInt], y : Ptr[CInt], comp : Ptr[CInt]): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_info_from_callbacks(clbk : Ptr[stbi_io_callbacks], user : Ptr[Byte], x : Ptr[CInt], y : Ptr[CInt], comp : Ptr[CInt]): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_info_from_file(f : Ptr[FILE], x : Ptr[CInt], y : Ptr[CInt], comp : Ptr[CInt]): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_info_from_memory(buffer : Ptr[stbi_uc], len : CInt, x : Ptr[CInt], y : Ptr[CInt], comp : Ptr[CInt]): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_is_16_bit(filename : CString): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_is_16_bit_from_callbacks(clbk : Ptr[stbi_io_callbacks], user : Ptr[Byte]): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_is_16_bit_from_file(f : Ptr[FILE]): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_is_16_bit_from_memory(buffer : Ptr[stbi_uc], len : CInt): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_is_hdr(filename : CString): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_is_hdr_from_callbacks(clbk : Ptr[stbi_io_callbacks], user : Ptr[Byte]): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_is_hdr_from_file(f : Ptr[FILE]): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_is_hdr_from_memory(buffer : Ptr[stbi_uc], len : CInt): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_ldr_to_hdr_gamma(gamma : Float): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_ldr_to_hdr_scale(scale : Float): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_load(filename : CString, x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[stbi_uc] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_load_16(filename : CString, x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[stbi_us] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_load_16_from_callbacks(clbk : Ptr[stbi_io_callbacks], user : Ptr[Byte], x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[stbi_us] = extern

  /**
   * /////////////////////////////////

   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_load_16_from_memory(buffer : Ptr[stbi_uc], len : CInt, x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[stbi_us] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_load_from_callbacks(clbk : Ptr[stbi_io_callbacks], user : Ptr[Byte], x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[stbi_uc] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_load_from_file(f : Ptr[FILE], x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[stbi_uc] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_load_from_file_16(f : Ptr[FILE], x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[stbi_us] = extern

  /**
   * /////////////////////////////////

   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_load_from_memory(buffer : Ptr[stbi_uc], len : CInt, x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[stbi_uc] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_load_gif_from_memory(buffer : Ptr[stbi_uc], len : CInt, delays : Ptr[Ptr[CInt]], x : Ptr[CInt], y : Ptr[CInt], z : Ptr[CInt], comp : Ptr[CInt], req_comp : CInt): Ptr[stbi_uc] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_loadf(filename : CString, x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[Float] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_loadf_from_callbacks(clbk : Ptr[stbi_io_callbacks], user : Ptr[Byte], x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[Float] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_loadf_from_file(f : Ptr[FILE], x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[Float] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_loadf_from_memory(buffer : Ptr[stbi_uc], len : CInt, x : Ptr[CInt], y : Ptr[CInt], channels_in_file : Ptr[CInt], desired_channels : CInt): Ptr[Float] = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_set_flip_vertically_on_load(flag_true_if_should_flip : CInt): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_set_flip_vertically_on_load_thread(flag_true_if_should_flip : CInt): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_set_unpremultiply_on_load(flag_true_if_should_unpremultiply : CInt): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_set_unpremultiply_on_load_thread(flag_true_if_should_unpremultiply : CInt): Unit = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_zlib_decode_buffer(obuffer : CString, olen : CInt, ibuffer : CString, ilen : CInt): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_zlib_decode_malloc(buffer : CString, len : CInt, outlen : Ptr[CInt]): CString = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_zlib_decode_malloc_guesssize(buffer : CString, len : CInt, initial_size : CInt, outlen : Ptr[CInt]): CString = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_zlib_decode_malloc_guesssize_headerflag(buffer : CString, len : CInt, initial_size : CInt, outlen : Ptr[CInt], parse_header : CInt): CString = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_zlib_decode_noheader_buffer(obuffer : CString, olen : CInt, ibuffer : CString, ilen : CInt): CInt = extern

  /**
   * [bindgen] header: C:\libs\STB\include\stb\stb_image.h
  */
  def stbi_zlib_decode_noheader_malloc(buffer : CString, len : CInt, outlen : Ptr[CInt]): CString = extern


object functions:
  import _root_.stb_image.aliases.*
  import _root_.stb_image.structs.*
  import extern_functions.*
  export extern_functions.*

object constants:
  val STBI_default: CInt = 0
  val STBI_grey: CInt = 1
  val STBI_grey_alpha: CInt = 2
  val STBI_rgb: CInt = 3
  val STBI_rgb_alpha: CInt = 4

object types:
  export _root_.stb_image.structs.*
  export _root_.stb_image.aliases.*

object all:
  export _root_.stb_image.aliases.FILE
  export _root_.stb_image.aliases.stbi_uc
  export _root_.stb_image.aliases.stbi_us
  export _root_.stb_image.structs.stbi_io_callbacks
  export _root_.stb_image.functions.stbi_convert_iphone_png_to_rgb
  export _root_.stb_image.functions.stbi_convert_iphone_png_to_rgb_thread
  export _root_.stb_image.functions.stbi_failure_reason
  export _root_.stb_image.functions.stbi_hdr_to_ldr_gamma
  export _root_.stb_image.functions.stbi_hdr_to_ldr_scale
  export _root_.stb_image.functions.stbi_image_free
  export _root_.stb_image.functions.stbi_info
  export _root_.stb_image.functions.stbi_info_from_callbacks
  export _root_.stb_image.functions.stbi_info_from_file
  export _root_.stb_image.functions.stbi_info_from_memory
  export _root_.stb_image.functions.stbi_is_16_bit
  export _root_.stb_image.functions.stbi_is_16_bit_from_callbacks
  export _root_.stb_image.functions.stbi_is_16_bit_from_file
  export _root_.stb_image.functions.stbi_is_16_bit_from_memory
  export _root_.stb_image.functions.stbi_is_hdr
  export _root_.stb_image.functions.stbi_is_hdr_from_callbacks
  export _root_.stb_image.functions.stbi_is_hdr_from_file
  export _root_.stb_image.functions.stbi_is_hdr_from_memory
  export _root_.stb_image.functions.stbi_ldr_to_hdr_gamma
  export _root_.stb_image.functions.stbi_ldr_to_hdr_scale
  export _root_.stb_image.functions.stbi_load
  export _root_.stb_image.functions.stbi_load_16
  export _root_.stb_image.functions.stbi_load_16_from_callbacks
  export _root_.stb_image.functions.stbi_load_16_from_memory
  export _root_.stb_image.functions.stbi_load_from_callbacks
  export _root_.stb_image.functions.stbi_load_from_file
  export _root_.stb_image.functions.stbi_load_from_file_16
  export _root_.stb_image.functions.stbi_load_from_memory
  export _root_.stb_image.functions.stbi_load_gif_from_memory
  export _root_.stb_image.functions.stbi_loadf
  export _root_.stb_image.functions.stbi_loadf_from_callbacks
  export _root_.stb_image.functions.stbi_loadf_from_file
  export _root_.stb_image.functions.stbi_loadf_from_memory
  export _root_.stb_image.functions.stbi_set_flip_vertically_on_load
  export _root_.stb_image.functions.stbi_set_flip_vertically_on_load_thread
  export _root_.stb_image.functions.stbi_set_unpremultiply_on_load
  export _root_.stb_image.functions.stbi_set_unpremultiply_on_load_thread
  export _root_.stb_image.functions.stbi_zlib_decode_buffer
  export _root_.stb_image.functions.stbi_zlib_decode_malloc
  export _root_.stb_image.functions.stbi_zlib_decode_malloc_guesssize
  export _root_.stb_image.functions.stbi_zlib_decode_malloc_guesssize_headerflag
  export _root_.stb_image.functions.stbi_zlib_decode_noheader_buffer
  export _root_.stb_image.functions.stbi_zlib_decode_noheader_malloc