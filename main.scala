import S2D.core.Window

@main
def main(): Unit =
  Window.create(800, 600, "S2D Framework - Images Module Test")

  while !Window.shouldCloseWindow() do
    println("Window is open, running main loop...")
  end while

  Window.close()
end main
