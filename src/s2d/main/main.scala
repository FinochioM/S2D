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

      val circlePos1 = Vector2(300.0f, 200.0f)
      val circlePos2 = Vector2(500.0f, 400.0f)

      DrawCircleV(circlePos1, 50.0f, Color(255, 100, 100))
      DrawCircleV(circlePos2, 75.0f, Color(100, 255, 100))

      val time = System.currentTimeMillis() / 1000.0f
      val animatedPos = Vector2(400.0f + math.sin(time).toFloat * 100.0f, 300.0f)
      DrawCircleV(animatedPos, 30.0f, Color(100, 100, 255))

    EndMode2D()

    EndDrawing()
  CloseWindow()