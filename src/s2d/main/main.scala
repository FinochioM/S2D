import S2D.core.*

@main
def main(): Unit =
  InitWindow(800, 600, "S2D Framework Says Hi!")

  ClearBackgroundRGB(25, 25, 112)

  while !WindowShouldClose() do
    BeginDrawing()

    if IsKeyEscape() then
      println("PRESSED KEY ESCAPE")

    EndDrawing()

  CloseWindow()