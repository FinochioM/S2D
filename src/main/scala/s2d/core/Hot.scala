package s2d.core

import java.io.File
import scala.collection.mutable.HashMap

object Hot:
    private var enabled: Boolean = false

    private final class Watched(val path: String, var mtime: Long)
    private val watched = HashMap.empty[Int, Watched]

    def enable(): Unit = enabled = true
    def disable(): Unit = enabled = false
    def isEnabled(): Boolean = enabled

    def watchedCount: Int = watched.size

    private[s2d] def track(path: String, glId: Int): Unit =
        if !enabled then return
        watched(glId) = new Watched(path, fileMtime(path))

    private def fileMtime(path: String): Long =
        new File(path).lastModified()

    private[s2d] def poll(): Unit =
        if !enabled then return

        // watch & reload files