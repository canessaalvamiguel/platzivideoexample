name := """platzi-video"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, DockerPlugin)

scalaVersion := "2.13.1"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.28.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
import com.typesafe.sbt.packager.docker
import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

dockerBaseImage := "openjdk:8-jre-alpine"
dockerExposedPorts ++= Seq(9000)

daemonUserUid in Docker  := None
daemonUser in Docker := "daemon"

dockerCommands += Cmd("user", "root")
dockerCommands += ExecCmd("RUN", "/bin/sh", "-c", "apk add --no-cache bash")
