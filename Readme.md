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

_**Go to the _[CONTRIBUTING](https://github.com/FinochioM/S2D/blob/master/.github/CONTRIBUTING.md)_ file to see what are the requirements of building and running the project.**_

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
S2D has a custom CLI Tool that lets you create a new template project with either SBT or Scala CLI.<br>
Here is a step-by-step guide (You need Coursier installed on your machine):

**The fastest way to set up a new project is running this command in your terminal:**

```
cs launch io.github.finochiom:s2d-cli_3:0.1.0 -- --generate
```

_This command will launch the S2D_CLI with the `--generate` command directly._

**If you want to install the CLI locally on your machine you can follow these steps:**
1. Go into the directory where you want to install the CLI Tool
2. Run the following command:
```
cs bootstrap io.github.finochiom:s2d-cli_3:0.1.0 --main main -o s2d-cli
```
_This will generate 2 files: `s2d_cli` & `s2d_cli.bat`_
3. After that you can run the CLI Tool like this:
```
s2d-cli --<command>
```


_**NOTE 1: The CLI Tool has its own repository where you can find the latest release. (https://github.com/FinochioM/S2D_CLI)**_
<br>
<br>
_**NOTE 2: Version** `1.0.1` **uses the wrong version scheme. S2D will be using the** `early-semver` **scheme, starting from** `0.1.2-SNAPSHOT`_
<br>
<br>
_**NOTE 3: If you don't want to use the CLI you will need to manually link the libraries, headers and dlls. If you don't do this the library will not work. You can go to the _[CONTRIBUTING](https://github.com/FinochioM/S2D/blob/master/.github/CONTRIBUTING.md)_ file for more information.**_
## License
S2D is licensed under the *zlib* license. Read the [LICENSE](https://github.com/FinochioM/S2D/blob/master/LICENSE) for more information.