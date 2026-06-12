## Contributing to S2D

This file explains how to set up the project locally so you can compile it, run it, and contribute to S2D.

## Project Setup

To clone the repository and get the project compiling and running, follow these steps:

1. Download or clone the repository that contains the required libraries and binaries:

   https://github.com/FinochioM/S2D_Libraries

2. Save these libraries in a folder in the system root directory.

   On Windows, this is usually `C:\`, so the folder should look something like this:

   ```text
   C:\libs\<files here>
   ```

3. Clone the main S2D repository, which contains the source code and project files.

   The project was created, built, and run using `Scala CLI`, but you can also use `sbt` if you prefer.

4. Inside the project, you will find two main folders:

   * `src`
   * `sandbox`

   The `src` folder contains the source code of the library, as well as the bindings for STB, SDL2, GLEW, and OpenGL.

   The `sandbox` folder contains a `main.scala` file. This folder is used as a test project.

---

## Running the Project with Scala CLI

To run the project with the latest local version of S2D, follow these steps.

> These instructions are for Scala CLI only.

1. Open a terminal such as CMD, WezTerm, or PowerShell.

2. Navigate to the root directory of the project:

   ```shell
   cd <path-to-S2D>
   ```

3. Publish the project locally:

   ```shell
   scala-cli --power publish local .
   ```

4. After publishing the project locally, you should find it in your local `.ivy2` directory.

   On Windows, this is usually located in your user directory:

   ```text
   C:\Users\<username>\.ivy2\local\io.github.finochiom\s2d_native0.5_3\0.1.0-SNAPSHOT
   ```

   Keep in mind that this command uses the `project.scala` file found in the S2D folder, so the version `0.1.0-SNAPSHOT` might be different when you run the command.

5. Navigate to the `sandbox` folder:

   ```shell
   cd <path-to-S2D>\sandbox
   ```

6. Run the project using the `build.bat` script:

   ```shell
   build.bat
   ```

   In the `main.scala` file inside the `sandbox` folder, you will find a dependency line similar to this:

   ```scala
   //> using dep "io.github.FinochioM::s2d::0.1.0-SNAPSHOT"
   ```

   The version in this line must match the version defined in the `project.scala` file.

## Important Notes

As explained above, you must have the required libraries stored locally on your computer.

It is also important to read the `INSTALLATION` section of the [README.md](https://github.com/FinochioM/S2D/blob/master/README.md) file.

If you followed these steps and the project compiles and runs correctly, then you are ready to contribute.
