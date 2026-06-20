package s2d.audio

import s2d.backend.miniaudio.{MiniAudio, ma_sound}
import scalanative.unsafe.*
import scalanative.unsigned.*
import scalanative.libc.stdlib.*

object Audio:
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