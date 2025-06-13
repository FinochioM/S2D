import S2D.core.Window
import S2D.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main def testWindow(): Unit =
  Window.create(800, 600, "Test Window")

  Thread.sleep(1000)

  println(Window.monitorWidth(0))
  println(Window.monitorHeight(0))
  println(Window.monitorPhysicalWidth(0))
  println(Window.monitorPhysicalHeight(0))

  Thread.sleep(1000)

  Window.close()
end testWindow
