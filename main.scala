import S2D.core.Window
import S2D.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main def testWindow(): Unit =
  Window.create(800, 600, "Test Window")

  Thread.sleep(1000)
  Window.setMonitor(1)

  Thread.sleep(1000)
  Window.setMonitor(0)

  Thread.sleep(1000)
  Window.setMonitor(2)

  Window.close()
end testWindow
