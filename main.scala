import S2D.core.Window
import S2D.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main def testWindow(): Unit =
  Window.create(800, 600, "Test Window")

  Thread.sleep(1000)
  Window.setOpacity(0.5f)
  Thread.sleep(1000)
  Window.setOpacity(1.0f)

  Thread.sleep(1000)

  Window.close()
end testWindow
