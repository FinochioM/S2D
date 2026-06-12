import xerial.sbt.Sonatype.GitHubHosting

ThisBuild / version := "0.2.2"
ThisBuild / scalaVersion := "3.8.3"
ThisBuild / organization := "io.github.finochiom"
ThisBuild / versionScheme := Some("early-semver")

val publishTarget = sys.props.getOrElse("publishTarget", "sonatype")

lazy val root = (project in file("."))
  .enablePlugins(ScalaNativePlugin)
  .settings(
    name := "s2d",
    Compile / unmanagedSourceDirectories := (Compile / unmanagedSourceDirectories).value
      .filterNot(_.getName == "sandbox")
      .filterNot(_.getName == "examples"),
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
    pgpSigningKey := Some("E566FB27021A557C"),
    description := "A simple to use 2D videogames programming library written in Scala",

    publishTo := {
      if (publishTarget == "local") {
        Some(
          Resolver.file(
            "local-repo",
            file(sys.props("user.home") + "/.m2/repository")
          )
        )
      } else {
        sonatypePublishToBundle.value
      }
    },

    sonatypeCredentialHost := "central.sonatype.com",

    Compile / packageDoc / publishArtifact := true,
    Compile / packageSrc / publishArtifact := true,
    Test / publishArtifact := false
  )