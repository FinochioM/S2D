import S2D.*
import S2D.core.*
import S2D.shapes.*

@main
def main(): Unit =
  InitWindow(800, 600, "S2D Framework Says Hi!")

  val camera2D = Camera2D(
    offset = Vector2(100.0f, 100.0f),
    target = Vector2(0.0f, 0.0f),
    rotation = 0.0f,
    zoom = 20.0f
  )



  while !WindowShouldClose() do
    BeginDrawing()
    ClearBackground(Color(100, 149, 237))
    BeginMode2D(camera2D)

      //DrawCircle(10, 10, 2, Color(255, 255, 255))
      DrawCircleSector(Vector2(10, 10), 2, 1.0f, 100.0f, 4, Color(255, 255, 255))

    EndMode2D()
    EndDrawing()
  CloseWindow()