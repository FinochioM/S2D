ThisBuild / version := "0.1.7-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.6"
ThisBuild / organization := "io.github.finochiom"
ThisBuild / versionScheme := Some("early-semver")

Global / onChangedBuildSource := ReloadOnSourceChanges

// Copied from the Scala Native docs
// https://scala-native.org/en/stable/user/sbt.html
// set to Debug for compilation details (Info is default)
ThisBuild / logLevel := Level.Info

// import to add Scala Native options
import scala.scalanative.build._

// defaults set with common options shown
ThisBuild / nativeConfig ~= { c =>
  c.withLTO(LTO.none) // thin
    .withMode(Mode.debug) // releaseFast
    .withGC(GC.immix) // commix
}
//

lazy val root =
  (project in file("."))
    .enablePlugins(ScalaNativePlugin)
    .settings(
      name := "s2dProject",
      neverPublish
    )
    .aggregate(s2d)

lazy val s2d =
  (project in file("s2d"))
    .enablePlugins(ScalaNativePlugin)
    .settings(
      name := "s2d",
      commonSettings ++ publishSettings
    )

lazy val sandbox =
  (project in file("sandbox"))
    .enablePlugins(ScalaNativePlugin)
    .settings(
      name := "s2d",
      commonSettings ++ neverPublish
    )
    .dependsOn(s2d)

lazy val commonSettings = {
  Seq(
    scalacOptions ++= Seq(
      "-Wconf:msg=indented:silent"
    ),
    Compile / packageDoc / publishArtifact := true,
    Compile / packageSrc / publishArtifact := true,
    Test / publishArtifact := false
  )
}

lazy val publishSettings = {
  Seq(
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
    description := "A simple to use 2D videogames programming library written in Scala",
    publishTo := Some(
      Resolver.file(
        "local-repo",
        file(sys.props("user.home") + "/.m2/repository")
      )
    )
  )
}

lazy val neverPublish = Seq(
  publish / skip := true,
  publishLocal / skip := true
)
