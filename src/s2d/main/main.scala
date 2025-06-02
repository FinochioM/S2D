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

      DrawCircle(200, 200, 50, Color(255, 100, 100))
      DrawCircleLines(200, 200, 60, Color(0, 0, 0))

    EndMode2D()

    EndDrawing()
  CloseWindow()