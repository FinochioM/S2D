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

      DrawEllipse(200, 200, 80, 40, Color(255, 100, 100))
      DrawEllipse(400, 200, 30, 80, Color(100, 255, 100))
      DrawEllipse(300, 400, 60, 60, Color(100, 100, 255))

    EndMode2D()

    EndDrawing()
  CloseWindow()