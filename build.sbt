ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "Snake3D project",
    libraryDependencies += "org.scalafx" % "scalafx_3" % "19.0.0-R30"
  )

