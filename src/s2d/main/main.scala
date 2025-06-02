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

      val rect1 = Rectangle(200.0f, 150.0f, 100.0f, 60.0f)
      DrawRectanglePro(rect1, Vector2(0.0f, 0.0f), 30.0f, Color(255, 100, 100))

      val rect2 = Rectangle(400.0f, 200.0f, 120.0f, 80.0f)
      val centerOrigin = Vector2(rect2.width / 2.0f, rect2.height / 2.0f)
      DrawRectanglePro(rect2, centerOrigin, 0.0f, Color(100, 255, 100))

    val rect3 = Rectangle(400.0f, 200.0f, 120.0f, 80.0f)
    val centerOrigin2 = Vector2(0.0f, 0.0f)
    DrawRectanglePro(rect3, centerOrigin2, 45.0f, Color(255, 100, 100))

    EndMode2D()

    EndDrawing()
  CloseWindow()