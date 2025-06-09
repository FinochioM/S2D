import S2D.core.Window.*
import S2D.core.Drawing.*
import S2D.shapes.Basics.*
import S2D.types.*

@main
def main(): Unit =
  InitWindow(800, 600, "S2D Framework Says Hi!")

  val camera2D = Camera2D(
    offset = Vector2(0.0f, 0.0f),
    target = Vector2(0.0f, 0.0f),
    rotation = 0.0f,
    zoom = 1.0f
  )

  val rectanglePos = Vector2(100.0, 100.0f)
  val rectangleSize = Vector2(200.0f, 150.0f)

  while !WindowShouldClose() do
    BeginDrawing()
    ClearBackground(Color.fromHex("#6495ED").getOrElse(Color.Blue))

    BeginMode2D(camera2D)
      val rect = Rectangle.fromCenter(rectanglePos + rectangleSize / 2, rectangleSize)

      val baseColor = Color.Red
      val animatedColor = baseColor.lerp(Color.Yellow, (math.sin(System.currentTimeMillis() / 1000.0) + 1.0).toFloat / 2.0f)

      BeginBlendMode(BlendMode.Alpha)
      DrawRectangleRec(rect, animatedColor)
      EndBlendMode()

    EndMode2D()

    EndDrawing()
  CloseWindow()