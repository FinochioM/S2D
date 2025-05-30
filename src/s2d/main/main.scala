import S2D.*
import S2D.core.*

@main
def main(): Unit =
  InitWindow(800, 600, "S2D Framework Says Hi!")


  val camera = Camera2D(
    offset = Vector2(400, 300),
    target = Vector2(0, 0),
    rotation = 0.0f,
    zoom = 1.0f
  )

  while !WindowShouldClose() do
    BeginDrawing()
    ClearBackground(Color(100, 149, 237))

    BeginMode2D(camera)

    EndMode2D()

    EndDrawing()

  CloseWindow()