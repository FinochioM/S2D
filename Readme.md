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
  while Window.isOpen() do
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
S2D does not have an official documentation at the moment, so I wrote some basic examples that can be used as one. \
**You can find these examples in the _[s2d_examples](https://github.com/FinochioM/S2D/tree/master/s2d_examples)_ folder.**

## Installation
S2D has a custom CLI Tool that lets you create a new template project with either SBT or Scala CLI. \
Here is a step-by-step guide (You need Coursier installed on your machine): 

1. Run the following command:
```
cs install s2d --contrib
```
_This will install the s2d-cli application in your machine. \
**Refer to the coursier documentation to see where the applications are installed.**_ \

2. After that you can run the CLI Tool like this:
```
s2d --<command>
```
_You can run the `--help` command to see the commands available_

_**NOTE 1: The CLI Tool has its own repository where you can find the changelog and latest release (The `install` command automatically installs the latest version)** **https://github.com/FinochioM/S2D_CLI**_
\
\
_**NOTE 2: Version** `1.0.1` **uses the wrong version scheme. S2D will be using the** `early-semver` **scheme, starting from** `0.1.2-SNAPSHOT`_


## Links
It would be nice to build a community over time to help each other so here are a few useful links for that: \
_[SUBREDDIT](https://www.reddit.com/r/S2D/)_ \
_[DISCORD]() **not available yet**_ \
_[SUBSTACK](https://substack.com/@matiasfinochio)_

## License
S2D is licensed under the *zlib* license. Read the [LICENSE](https://github.com/FinochioM/S2D/blob/master/LICENSE) for more information.