# S2D

A simple 2D graphics library for Scala 3, built on top of LWJGL and OpenGL. S2D provides an easy-to-use API for creating 2D games and graphics applications.

## Changelog & TO-DO
You can take a look at what packages/functions I will add in the future in the _[LIBRARY_EXPANSION.TXT](https://github.com/FinochioM/S2D/blob/master/LIBRARY_EXPANSION.txt)_ file.
</br> You can also take a look at the _[Changelog.TXT](https://github.com/FinochioM/S2D/blob/master/Changelog.txt)_ file, where I will keep track of all the changes made to the different versions of the library.

## Contributing
Contributions are welcome and really appreciated! The project will always remain open source for anyone that wants to either add new things to the library or just take a look at the source code.

If you have a suggestion that would make the library better, please fork the repo and create a pull request.
Don't forget to give the repo a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Basic Example

```scala
import S2D.core.{Drawing, Window}
import S2D.shapes.Basics
import S2D.types.*

@main
def main(): Unit =
  // Create a window
  Window.create(800, 600, "My S2D App")

  // Set up a camera
  val camera = Camera2D(
    offset = Vector2(0.0f, 0.0f),
    target = Vector2(0.0f, 0.0f),
    rotation = 0.0f,
    zoom = 1.0f
  )

  // Main game loop
  while !Window.shouldCloseWindow() do
    Drawing.beginFrame()
    Drawing.clear(Color.Blue)

    Drawing.beginCamera(camera)
      // Draw a red rectangle
      val rect = Rectangle(200.0f, 200.0f, 200.0f, 100.0f)
      Basics.rectangle(rect, Color.Red)
      
      // Draw a white circle
      Basics.circle(Vector2(400.0f, 300.0f), 50.0f, Color.White)
    Drawing.endCamera()

    Drawing.endFrame()

  Window.close()
```

## Build and Installation
You can install S2D directly from Maven Central. This is the link to the library:
</br> https://central.sonatype.com/artifact/io.github.finochiom/s2d_3

_Another way of using S2D is through jitpack._
</br> _Just go to https://jitpack.io/#FinochioM/S2D and follow their instructions._

Here is an example of the ```build.sbt``` file with the Maven configuration.

```scala
ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .settings(
    name := "TestIntallation",

    libraryDependencies += "io.github.finochiom" %% "s2d" % "1.0.0"
  )
```

**Requirements:**
- JDK 21 or higher
- Scala 3.3.6
- sbt 1.10.0

## License
S2D is licensed under the *zlib* license. Read the [LICENSE](https://github.com/FinochioM/S2D/blob/master/LICENSE) for more information.