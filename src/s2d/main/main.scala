import S2D.*
import S2D.core.*
import S2D.shapes.*

@main
def main(): Unit =
  InitWindow(800, 600, "S2D Framework Says Hi!")

  val camera2D = Camera2D(
    offset = Vector2(0.0f, 0.0f),
    target = Vector2(0.0f, 0.0f),
    rotation = 0.0f,
    zoom = 1.0f
  )

  while !WindowShouldClose() do
    BeginDrawing()
    ClearBackground(Color(100, 149, 237))

    BeginMode2D(camera2D)

      DrawRectangle(100, 100, 200, 150, Color(255, 100, 100))


      for i <- 0 until 5 do
        for j <- 0 until 3 do
          val x = 50 + i * 60
          val y = 450 + j * 40
          val red = (i * 51) % 256
          val green =(j * 85) % 256
          DrawRectangle(x, y, 50, 30, Color(red, green, 100))
    EndMode2D()

    EndDrawing()
  CloseWindow()