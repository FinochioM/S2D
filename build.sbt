ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "3.3.6"

lazy val lwjglVersion = "3.3.3"
lazy val lwjglNatives = "natives-windows"

lazy val root = (project in file("."))
  .settings(
    name := "S2D",
    organization := "com.github.finochiom",
    publishMavenStyle := true,
    libraryDependencies ++= Seq(
      "org.lwjgl" % "lwjgl" % lwjglVersion,
      "org.lwjgl" % "lwjgl-glfw" % lwjglVersion,
      "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,
      "org.lwjgl" % "lwjgl-stb" % lwjglVersion,

      "org.lwjgl" % "lwjgl" % lwjglVersion classifier lwjglNatives,
      "org.lwjgl" % "lwjgl-glfw" % lwjglVersion classifier lwjglNatives,
      "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier lwjglNatives,
      "org.lwjgl" % "lwjgl-stb" % lwjglVersion classifier lwjglNatives,
    )
  )
