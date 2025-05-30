import S2D.*
import S2D.core.*

@main
def main(): Unit =
  InitWindow(800, 600, "S2D Framework Says Hi!")

  while !WindowShouldClose() do
    BeginDrawing()
    ClearBackground(Color(100, 149, 237))

    EndDrawing()

  CloseWindow()