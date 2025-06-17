## Contributing to S2D

**In this file I want to explain how to contribute to this project.**

To clone the repository and have it compiling and running you will need to follow these steps:

1- Download or clone this repository, where I have the libraries and binaries for the project.
    <br>_- https://github.com/FinochioM/S2D_Libraries_<br>
2- Save these libraries in a folder in the system root directory, in Windows it usually is `C:\`, so it should be something like `C:\libs\<files here>`.<br>
3- Now you can clone the main S2D repository, which cointains all the source code and the project files. <br>_**The project was created and it's built and run with `Scala-CLI` but you can use `SBT` if you want to.**_
<br>4- In the project you will have 2 main folders, `src` and `sandbox`.
<br> The `src` folder contains the source code of the library as well as the bindings for STB, SDL2, GLEW and OPENGL.
<br> The `sandbox` folder contains a `main.scala` file and a `build.bat` file, this is basically used as a **TEST PROJECT**.
---
To run the project with the latest version of S2D here are the steps (For Scala-CLI only):<br>
- Open a terminal (CMD, Wezterm, PowerShell, etc.) and navigate to the root directory of the project. (`cd <path-to-S2D>`)<br>
- Publish the project locally with the command:
```shell
scala-cli --power publish local .
```
- After publishing the project locally you should find it in the local `.ivy2` directory, which is usually located in your home directory (`C:\Users\<username>\.ivy2\local\io.github.finochiom\s2d_native0.5_3\0.1.0-SNAPSHOT`).
<br>_**Take into account that this command will use the `project.scala` file found in the S2D folder, so the version `0.1.0-SNAPSHOT` might be different when you run the command**_
- Now navigate to the `sandbox` folder.
```shell
cd <path-to-S2D>\sandbox
```
- Once in the sandbox folder you can run the project by running the build.bat script:
```shell
build.bat
```
_**Again note that in the `main.scala` file inside the sandbox folder you will find this line of code: `//> using dep "io.github.FinochioM::s2d::0.1.0-SNAPSHOT"`. The `0.1.0-SNAPSHOT` must be the exact same one as the one in the `project.scala` file.**_

## Important Notes
**As explained above it is a MUST that you have the libraries stored locally in your computer.**<br>
**It is also important to read the `BUILD AND INSTALLATION` section of the [README.md](https://github.com/FinochioM/S2D/blob/master/README.md) file**<br>

If you followed these steps and the project compiles and runs correctly, then you are ready to contribute with whatever you feel like!