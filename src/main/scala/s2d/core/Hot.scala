package s2d.core

object Hot:
    private var enabled: Boolean = false

    def enable(): Unit = enabled = true
    def disable(): Unit = enabled = false
    def isEnabled(): Boolean = enabled

    private[s2d] def poll(): Unit =
        if !enabled then return

        // watch & reload files