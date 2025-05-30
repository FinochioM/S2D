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

  val points = Array(
    Vector2(10.0f, 10.0f),
    Vector2(20.0f, 15.0f),
    Vector2(30.0f, 10.0f),
    Vector2(40.0f, 20.0f)
  )

  while !WindowShouldClose() do
    BeginDrawing()
    ClearBackground(Color(100, 149, 237))
    BeginMode2D(camera2D)

      DrawLineStrip(points, Color(255, 255, 255))

    EndMode2D()
    EndDrawing()
  CloseWindow()