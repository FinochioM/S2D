import S2D.core.Window
import S2D.core.Drawing
import S2D.shapes.Basics
import S2D.textures.{Images, Textures}
import S2D.types.*

import java.nio.file.{Files, Paths}

@main
def main(): Unit =
  Window.create(800, 600, "S2D Framework - Images Module Test")

  val camera2D = Camera2D(
    offset = Vector2(0.0f, 0.0f),
    target = Vector2(0.0f, 0.0f),
    rotation = 0.0f,
    zoom = 1.0f
  )
  
  val imageOpt = Images.load("assets/player_still.png")

  imageOpt match
    case Some(image) =>
      val textureOpt = Textures.loadFromImage(image)

      textureOpt match
        case Some(texture) =>
          val extractedImageOpt = Images.loadFromTexture(texture)

          extractedImageOpt match
            case Some(extractedImage) =>
              if Images.exportImage(extractedImage, "output_test.png") then
                println("Image exported as PNG")
              else
                println("Failed to export as PNG")

              if Images.exportImage(extractedImage, "output_test.jpg") then
                println("Image exported as JPG")
              else
                println("Failed to export as JPG")
              if Images.exportImage(extractedImage, "output_test.bmp") then
                println("Image exported as BMP")
              else
                println("Failed to export as BMP")

              Images.exportToMemory(extractedImage, "png") match
                case Some((data, size)) =>
                  Images.loadFromMemory("png", data, size) match
                    case Some(memoryImage) =>
                      Images.unloadImage(memoryImage)
                    case None =>
                      println("Failed to load image from memory")
                case None =>
                  println("Failed to export to memory")

              if Images.exportAsCode(extractedImage, "image_data.scala") then
                try
                  val codeContent = Files.readString(Paths.get("image_data.scala"))
                  codeContent.split("\n").take(5).foreach(line => println(s"    $line"))
                catch
                  case _: Exception => println("Could not read generated file")
              else
                println("Failed to export as code")

              Images.unloadImage(extractedImage)
            case None =>
              println("Failed to extract image from texture")

          var screenshotTaken = false
          var frameCount = 0

          while !Window.shouldCloseWindow() do
            Drawing.beginFrame()
            Drawing.clear(Color.fromHex("#2C3E50").getOrElse(Color.Blue))

            Drawing.beginCamera(camera2D)
            val rect1 = Rectangle(100.0f, 100.0f, 150.0f, 100.0f)
            val rect2 = Rectangle(300.0f, 150.0f, 200.0f, 150.0f)

            val time = System.currentTimeMillis() / 1000.0
            val animatedColor = Color.Red.lerp(Color.Yellow, (math.sin(time) + 1.0).toFloat / 2.0f)

            Drawing.beginBlend(BlendMode.Alpha)
            Basics.rectangle(rect1, animatedColor)
            Basics.rectangleRounded(rect2, 0.3f, 8, Color.Green)

            Textures.draw(texture, 50, 300, Color.White)

            Drawing.endBlend()

            Drawing.endCamera()

            Drawing.endFrame()

            frameCount += 1

          Textures.unload(texture)
        case None =>
          println("Failed to create texture from image")

      Images.unloadImage(image)
    case None =>
      var screenshotTaken = false
      var frameCount = 0

      while !Window.shouldCloseWindow() do
        Drawing.beginFrame()
        Drawing.clear(Color.fromHex("#3498DB").getOrElse(Color.Blue))

        Drawing.beginCamera(camera2D)
        val rect = Rectangle(200.0f, 200.0f, 200.0f, 100.0f)
        Basics.rectangle(rect, Color.Red)
        Basics.rectangleOutline(Rectangle(180.0f, 180.0f, 240.0f, 140.0f), Color.White)
        Drawing.endCamera()

        Drawing.endFrame()

        frameCount += 1

  Window.close()