name := """reactive-scala-webstore"""
organization := "com.joegitau"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies ++=Seq(
  guice,
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "mysql" % "mysql-connector-java" % "8.0.25",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.joegitau.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.joegitau.binders._"
