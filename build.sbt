ThisBuild / version := "0.1.6-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.6"
ThisBuild / organization := "io.github.finochiom"
ThisBuild / versionScheme := Some("early-semver")

lazy val root = (project in file("."))
  .enablePlugins(ScalaNativePlugin)
  .settings(
    name := "s2d",
    Compile / unmanagedSourceDirectories := (Compile / unmanagedSourceDirectories).value.filterNot(_.getName == "sandbox"),

    scalacOptions ++= Seq(
      "-Wconf:msg=indented:silent"
    ),

    publishMavenStyle := true,
    licenses := Seq("zlib" -> url("https://zlib.net/zlib_license.html")),
    homepage := Some(url("https://github.com/FinochioM/S2D")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/FinochioM/S2D"),
        "scm:git@github.com:finochiom/S2D.git"
      )
    ),
    developers := List(
      Developer(
        id = "FinochioM",
        name = "Matias Finochio",
        email = "matias.finochio@davinci.edu.ar",
        url = url("https://github.com/FinochioM")
      )
    ),

    pgpSigningKey := Some("FD3BD1C64C106A9B"),

    description := "The S2D CLI tool for creating and managing S2D projects",
    publishTo := Some(Resolver.file("local-repo", file(sys.props("user.home") + "/.m2/repository"))),

    Compile / packageDoc / publishArtifact := true,
    Compile / packageSrc / publishArtifact := true,
    Test / publishArtifact := false
  )