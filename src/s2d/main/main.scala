import S2D.{Image, PixelFormat}
import S2D.core.*

@main
def main(): Unit =
  InitWindow(800, 600, "S2D Framework Says Hi!")

  ClearBackgroundRGB(25, 25, 112)

  val iconSize = 16
  val iconData = java.nio.ByteBuffer.allocateDirect(iconSize * iconSize * 4)

  for (i <- 0 until iconSize * iconSize) {
    iconData.put((255).toByte)
    iconData.put((0).toByte)
    iconData.put((0).toByte)
    iconData.put((255).toByte)
  }
  iconData.flip()

  val testIcon = Image(iconData, iconSize, iconSize, 1, PixelFormat.UNCOMPRESSED_R8G8B8A8)

  SetWindowIcon(testIcon)

  println(GetMonitorName(0))

  while !WindowShouldClose() do
    BeginDrawing()

    if IsKeyDown(Keys.A) then
      ShowCursor()
      Thread.sleep(300)

    if IsKeyDown(Keys.D) then
      HideCursor()
      Thread.sleep(300)

    EndDrawing()

  CloseWindow()