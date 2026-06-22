package s2d.audio

import s2d.backend.miniaudio.{MiniAudio, ma_sound}
import scalanative.unsafe.*
import scalanative.unsigned.*
import scalanative.libc.stdlib.*

object Audio:
  private val MA_SOUND_FLAG_STREAM = 0x00000002.toUInt

  def load(fileName: String): Option[Sound] =
    val ptr = malloc(sizeof[ma_sound]).asInstanceOf[Ptr[ma_sound]]
    if ptr == null then return None
    val bytes = fileName.getBytes("UTF-8")
    val buf = stackalloc[CChar](bytes.length + 1)
    var i = 0
    while i < bytes.length do
      buf(i) = bytes(i)
      i += 1
    buf(bytes.length) = 0.toByte
    val result = MiniAudio.ma_sound_init_from_file(
      AudioRegistry.enginePtr, buf, 0.toUInt, null, null, ptr
    )
    if result != 0 then
      free(ptr.asInstanceOf[Ptr[Byte]])
      None
    else
      Some(AudioRegistry.register(ptr))

  def unload(sound: Sound): Unit =
    AudioRegistry.get(sound).foreach { ptr =>
      MiniAudio.ma_sound_uninit(ptr)
      free(ptr.asInstanceOf[Ptr[Byte]])
      AudioRegistry.remove(sound)
    }

  def play(sound: Sound): Unit =
    AudioRegistry.get(sound).foreach(MiniAudio.ma_sound_start)

  def stop(sound: Sound): Unit =
    AudioRegistry.get(sound).foreach(MiniAudio.ma_sound_stop)

  def pause(sound: Sound): Unit =
    AudioRegistry.get(sound).foreach(MiniAudio.ma_sound_stop)

  def resume(sound: Sound): Unit =
    AudioRegistry.get(sound).foreach(MiniAudio.ma_sound_start)

  def isPlaying(sound: Sound): Boolean =
    AudioRegistry.get(sound).exists { ptr =>
      MiniAudio.ma_sound_is_playing(ptr) != 0.toUInt
    }

  def setVolume(sound: Sound, volume: Float): Unit =
    AudioRegistry.get(sound).foreach(MiniAudio.ma_sound_set_volume(_, volume))

  def setPitch(sound: Sound, pitch: Float): Unit =
    AudioRegistry.get(sound).foreach(MiniAudio.ma_sound_set_pitch(_, pitch))

  def loadMusic(fileName: String): Option[Music] =
    val ptr = malloc(sizeof[ma_sound]).asInstanceOf[Ptr[ma_sound]]
    if ptr == null then return None
    val bytes = fileName.getBytes("UTF-8")
    val buf = stackalloc[CChar](bytes.length + 1)
    var i = 0
    while i < bytes.length do
      buf(i) = bytes(i)
      i += 1
    buf(bytes.length) = 0.toByte
    val result = MiniAudio.ma_sound_init_from_file(
      AudioRegistry.enginePtr, buf, MA_SOUND_FLAG_STREAM, null, null, ptr
    )
    if result != 0 then
      free(ptr.asInstanceOf[Ptr[Byte]])
      None
    else
      MiniAudio.ma_sound_set_looping(ptr, 1.toUInt)
      Some(AudioRegistry.registerMusic(ptr))

  def unloadMusic(music: Music): Unit =
    AudioRegistry.getMusic(music).foreach { ptr =>
      MiniAudio.ma_sound_uninit(ptr)
      free(ptr.asInstanceOf[Ptr[Byte]])
      AudioRegistry.removeMusic(music)
    }

  def playMusic(music: Music): Unit =
    AudioRegistry.getMusic(music).foreach(MiniAudio.ma_sound_start)

  def stopMusic(music: Music): Unit =
    AudioRegistry.getMusic(music).foreach { ptr =>
      MiniAudio.ma_sound_stop(ptr)
      MiniAudio.ma_sound_seek_to_pcm_frame(ptr, 0.toULong)
    }

  def pauseMusic(music: Music): Unit =
    AudioRegistry.getMusic(music).foreach(MiniAudio.ma_sound_stop)

  def resumeMusic(music: Music): Unit =
    AudioRegistry.getMusic(music).foreach(MiniAudio.ma_sound_start)

  def isPlayingMusic(music: Music): Boolean =
    AudioRegistry.getMusic(music).exists { ptr =>
      MiniAudio.ma_sound_is_playing(ptr) != 0.toUInt
    }

  def setMusicVolume(music: Music, volume: Float): Unit =
    AudioRegistry.getMusic(music).foreach(MiniAudio.ma_sound_set_volume(_, volume))

  def setMusicPitch(music: Music, pitch: Float): Unit =
    AudioRegistry.getMusic(music).foreach(MiniAudio.ma_sound_set_pitch(_, pitch))

  def updateMusic(music: Music): Unit = ()