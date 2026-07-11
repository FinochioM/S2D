package s2d.core

import java.io.File
import scala.collection.mutable.HashMap
import scala.collection.mutable.Queue

object Hot:
  @volatile private var enabled: Boolean = false

  private final class Watched(val path: String, var mtime: Long)
  private val watched = HashMap.empty[Int, Watched]
  private val dirty   = Queue.empty[Int]
  private val lock    = new Object

  private var thread: Thread = null

  def enable(): Unit =
    enabled = true
    if thread == null then
      thread = new Thread(() => watchLoop())
      thread.setDaemon(true)
      thread.start()

  def disable(): Unit = enabled = false
  def isEnabled: Boolean = enabled

  def watchedCount: Int = lock.synchronized { watched.size }

  private[s2d] def track(path: String, glId: Int): Unit =
    if !enabled then return
    lock.synchronized { watched(glId) = new Watched(path, fileMtime(path)) }

  private def fileMtime(path: String): Long =
    new File(path).lastModified()

  private def watchLoop(): Unit =
    while true do
      Thread.sleep(200)
      if enabled then
        val snapshot = lock.synchronized {
          watched.map((id, w) => (id, w.path, w.mtime)).toArray
        }
        
        var i = 0
        while i < snapshot.length do
          val (id, path, old) = snapshot(i)
          val m = fileMtime(path)
          if m != 0L && m != old then
            lock.synchronized {
              watched.get(id).foreach { w =>
                if w.mtime != m then
                  w.mtime = m
                  dirty.enqueue(id)
              }
            }
          i += 1

  private[s2d] def poll(): Unit =
    if !enabled then return
    lock.synchronized {
      while dirty.nonEmpty do
        val id = dirty.dequeue()
        println(s"reload called ID $id")
    }