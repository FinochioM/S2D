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

      DrawCircleGradient(400, 300, 100.0f, Color(255, 255, 255), Color(255, 0, 0))
      DrawCircleGradient(200, 200, 80.0f, Color(0, 100, 255), Color(0, 100, 255, 0))

    EndMode2D()

    EndDrawing()
  CloseWindow()