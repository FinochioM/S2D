# S2D

A simple 2D graphics library for Scala-Native, built on top of SDL2 and OpenGL. S2D provides an easy-to-use API for creating 2D games and graphics applications.

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

_NOTE: Take into account that the project is built with Scala-CLI and you will need to manually link SDL2 and OpenGL. Go to the `build and installation` section to see the command._

## Basic Example

```scala
import s2d.core.{Drawing, Window}
import s2d.shapes.Basics
import s2d.types.*

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
You can install S2D directly from Maven Central with the following link:
</br> https://central.sonatype.com/artifact/io.github.finochiom/s2d_native0.5_3

_Another way of using S2D is through jitpack._
</br> _Just go to https://jitpack.io/#FinochioM/S2D and follow their instructions._

Here is an example of the `build.sbt` and `plugins.sbt` file with the Maven configuration.

```scala
import scala.scalanative.build._

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .enablePlugins(ScalaNativePlugin)
  .settings(
    name := "TEST_S2D_Native",
    libraryDependencies += "io.github.finochiom" % "s2d_native0.5_3" % "1.0.1",
    nativeConfig ~= { c =>
      c.withMode(Mode.debug)
        .withGC(GC.commix)
        .withCompileOptions(
          Seq(
            "-IC:\\libs\\SDL2\\include",
            "-I/usr/local/include",
            "-I/usr/include"
          )
        )
        .withLinkingOptions(
          Seq(
            "-lSDL2",
            "-LC:\\libs\\SDL2\\lib",
            "-lopengl32"
          )
        )
    }
  )
```
```scala
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.5.8")
```

_NOTE 1: To run the project I recommend using Scala-CLI. You should be able to use this command `scala-cli run . --native-compile -I<path-to-include> --native-linking -lSDL2 --native-linking -L<path-to-lib> --native-linking -lopengl32`_
</br>
</br>_NOTE 2: If you are planning to use SBT anyway I recommend creating an SBT task with the `run` command. The libraries will be linked through the `build.sbt` file as showed above._
</br>
</br>
_NOTE 3: Version 1.0.0 of S2D uses LWJGL as the backend, so it will NOT work with Scala Native, from 1.0.1 on it uses SDL2_

## License
S2D is licensed under the *zlib* license. Read the [LICENSE](https://github.com/FinochioM/S2D/blob/master/LICENSE) for more information.