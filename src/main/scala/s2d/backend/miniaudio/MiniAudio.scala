package s2d.backend.miniaudio

import scalanative.unsafe.*
import scalanative.unsigned.*

type ma_engine = CArray[Byte, Nat.Digit4[Nat._1, Nat._3, Nat._6, Nat._0]]
type ma_sound  = CArray[Byte, Nat.Digit4[Nat._1, Nat._0, Nat._2, Nat._4]]
type ma_result = CInt

@extern
@link("m")
@link("pthread")
@link("dl")
object MiniAudio:
  def ma_engine_init(pConfig: Ptr[Byte], pEngine: Ptr[ma_engine]): ma_result = extern
  def ma_engine_uninit(pEngine: Ptr[ma_engine]): Unit = extern

  def ma_sound_init_from_file(pEngine: Ptr[ma_engine], pFilePath: CString, flags: CUnsignedInt, pGroup: Ptr[Byte], pDoneFence: Ptr[Byte], pSound: Ptr[ma_sound]): ma_result = extern
  def ma_sound_uninit(pSound: Ptr[ma_sound]): Unit = extern

  def ma_sound_start(pSound: Ptr[ma_sound]): ma_result = extern
  def ma_sound_stop(pSound: Ptr[ma_sound]): ma_result = extern

  def ma_sound_set_volume(pSound: Ptr[ma_sound], volume: Float): Unit = extern
  def ma_sound_set_pitch(pSound: Ptr[ma_sound], pitch: Float): Unit = extern

  def ma_sound_is_playing(pSound: Ptr[ma_sound]): CUnsignedInt = extern
  def ma_sound_set_looping(pSound: Ptr[ma_sound], isLooping: CUnsignedInt): Unit = extern
  def ma_sound_is_looping(pSound: Ptr[ma_sound]): CUnsignedInt = extern

  def ma_sound_seek_to_pcm_frame(pSound: Ptr[ma_sound], frameIndex: CUnsignedLongLong): ma_result = extern