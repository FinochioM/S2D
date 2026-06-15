package s2d.audio

import s2d.backend.miniaudio.{MiniAudio, ma_engine, ma_sound}
import scalanative.unsafe.*
import scalanative.libc.stdlib.*

private[audio] object AudioRegistry:
  private val sounds = scala.collection.mutable.HashMap.empty[Int, Long]
  private var nextId = 1

  private val engine: Ptr[ma_engine] =
    val ptr = malloc(sizeof[ma_engine]).asInstanceOf[Ptr[ma_engine]]
    val result = MiniAudio.ma_engine_init(null, ptr)
    if result != 0 then
      free(ptr.asInstanceOf[Ptr[Byte]])
      throw new RuntimeException(s"ma_engine_init failed: $result")
    ptr

  private[audio] def enginePtr: Ptr[ma_engine] = engine

  private[audio] def register(ptr: Ptr[ma_sound]): Sound =
    val id = nextId
    nextId += 1
    sounds(id) = ptr.asInstanceOf[Long]
    Sound(id)

  private[audio] def get(s: Sound): Option[Ptr[ma_sound]] =
    sounds.get(Sound.id(s)).map(_.asInstanceOf[Ptr[ma_sound]])

  private[audio] def remove(s: Sound): Unit =
    sounds.remove(Sound.id(s))

  private[audio] def uninit(): Unit =
    MiniAudio.ma_engine_uninit(engine)
    free(engine.asInstanceOf[Ptr[Byte]])