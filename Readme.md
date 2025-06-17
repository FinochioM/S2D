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

_**Go to the _[CONTRIBUTING](https://github.com/FinochioM/S2D/blob/master/.github/CONTRIBUTING)_ file to see what are the requirements of building and running the project.**_

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

## Installation
You can install S2D directly from Maven Central with the following link:
</br> https://central.sonatype.com/artifact/io.github.finochiom/s2d_native0.5_3
<br>
<br>_**Publishing to Maven Central is currently on hold because of an issue with Scala-CLI.**_
<br>_**The last version available on Maven is the 1.0.1, which is an outdated version. If you want to use the latest version you can check the last release on this repository and download it through Jitpack.**_

If you use SBT:<br>
`build.sbt` & `plugins.sbt`

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
            "-I<path-to-SDL2_include>", // Replace with your SDL2 include path
            "-I<path-to-STB_include>",        // Replace with your STb include path
            "-I<path-to-GLEW_include>",       // Replace with your GLEW include path
            "-I/usr/local/include",
            "-I/usr/include"
          )
        )
        .withLinkingOptions(
          Seq(
            "-lSDL2",
            "-L<path-to-SDL2_lib>", // Replace with your SDL2 library path
            "-L<path-to-STB_lib>",                 // Replace with your STB library path
            "-lstb_image",                     // This is the name of the STB library (std_image.lib)
            "-L<path-to-GLEWx64_lib>", // Replace with your GLEW library path
            "-lglew32",                        // This is the name of the GLEW library (glew32.lib)
            "-lopengl32",
            "-lglu32"
          )
        )
    }
  )
```
```scala
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.5.8")
```
If you use Scala-CLI:<br>
`project.scala`
```scala
//> using scala 3.3.6
//> using platform scala-native
//> using nativeVersion 0.5.8
//> using dep io.github.finochiom::s2d_native:version
```
Or run this command in a terminal:
```scala
scala-cli run . --dependency io.github.finochiom::s2d_native:version
```

_**To be able to Build and Run the project you will need to manually link the libraries and their headers. The project currently uses SDL2, STB, GLEW and OpenGL. You can refer to the _[CONTRIBUTING](https://github.com/FinochioM/S2D/blob/master/.github/CONTRIBUTING)_ file to see how to do it.**_
</br>
</br>_**NOTE 1: You will need to place the `glew32.dll` and `sdl2.dll` files in the root folder of your project. Again you can refer to the _[CONTRIBUTING](https://github.com/FinochioM/S2D/blob/master/.github/CONTRIBUTING)_ file to download them.**_
</br>
</br>
_**NOTE 2: Version** `1.0.1` **uses the wrong version scheme. S2D will be using the** `early-semver` **scheme, starting from** `0.1.2-SNAPSHOT`_

## License
S2D is licensed under the *zlib* license. Read the [LICENSE](https://github.com/FinochioM/S2D/blob/master/LICENSE) for more information.