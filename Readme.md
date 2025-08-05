<br />
<div align="center">
  <a href="https://github.com/FinochioM/S2D">
    <img src="images/S2D-Logo.svg" alt="Logo" width="270" height="270">
  </a>

  <h3 align="center">S2D</h3>

  <p align="center">
    A simple 2D graphics library for Scala-Native, built on top of SDL2 and OpenGL. S2D provides an easy-to-use API for creating 2D games and graphics applications.
    <br />
  </p>
</div>

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

## Examples
**Basic example of S2D**

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
S2D does not have an official documentation at the moment, so I have written some examples based on the `RAYLIB` library. <br>
Inside the folder you will also find a `README.md` file that explains the structure of the folder more in depth.<br>
**You can find these examples in the _[s2d_examples](https://github.com/FinochioM/S2D/tree/master/s2d_examples)_ folder.**

## Installation
S2D has a custom CLI Tool that lets you create a new template project with either SBT or Scala CLI.<br>
Here is a step-by-step guide (You need Coursier installed on your machine):

**The fastest way to set up a new project is running this command in your terminal:**

```
cs launch io.github.finochiom:s2d-cli_3:0.1.0 -- --generate
```

_This command will launch S2D_CLI with the `--generate` command directly. **(Please take into account you will need to manually change the version of the CLI to the latest one)**_

**If you want to install the CLI locally on your machine you can follow these steps:**
1. Run the following command:
```
cs install s2d --contrib
```
_This will install the s2d-cli application in your machine. <br>**Refer to the coursier documentation to see where the applications are installed.**_<br>
2. After that you can run the CLI Tool like this:
```
s2d --<command>
```
_You can run the `--help` command to see the commands available_

_**NOTE 1: The CLI Tool has its own repository where you can find the changelog and latest release (The `install` command automatically installs the latest version)** **https://github.com/FinochioM/S2D_CLI**_
<br>
<br>
_**NOTE 2: Version** `1.0.1` **uses the wrong version scheme. S2D will be using the** `early-semver` **scheme, starting from** `0.1.2-SNAPSHOT`_
<br>
<br>
_**NOTE 3: If you don't want to use the CLI you will need to manually link the libraries, headers and dlls. If you don't do this the library will not work. You can go to the _[CONTRIBUTING](https://github.com/FinochioM/S2D/blob/master/.github/CONTRIBUTING.md)_ file for more information.**_
## License
S2D is licensed under the *zlib* license. Read the [LICENSE](https://github.com/FinochioM/S2D/blob/master/LICENSE) for more information.