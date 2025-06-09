import S2D.*
import S2D.core.*
import S2D.shapes.*
import S2D.types.{Camera2D, Color, Rectangle, Vector2}

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
      val rect1 = Rectangle(100.0f, 100.0f, 200.0f, 150.0f)
      DrawRectangleGradientEx(rect1, Color(255, 0, 0), Color(0, 0, 255), Color(255, 255, 0), Color(0, 255, 255))

    EndMode2D()

    EndDrawing()
  CloseWindow()