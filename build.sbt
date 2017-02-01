lazy val projectVersion = "0.1.0"

lazy val scalaTestVersion = "3.0.1"

lazy val buildSettings = Seq(
  organization := "br.eng.andrelfpinto",
  scalaVersion := "2.12.1"
)

lazy val core = crossProject.crossType(CrossType.Pure)
  .settings(projectSettings: _*)
  .settings(moduleName := "interpolationtable")
  .jvmSettings(commonJvmSettings)
  .jsSettings(commonJsSettings)

lazy val coreJVM = core.jvm
lazy val coreJS  = core.js

lazy val projectSettings = buildSettings ++ commonSettings

lazy val commonSettings = Seq(
  version := projectVersion,
  scalacOptions ++= commonScalacOptions,
  libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest" % scalaTestVersion % "test"
  )
)

lazy val commonJvmSettings = scoverageSettings

lazy val commonJsSettings = Seq(
  scalaJSStage in Global := FastOptStage
)

lazy val scoverageSettings = Seq(
  coverageMinimum := 60,
  coverageFailOnMinimum := false,
  coverageEnabled := true
)

lazy val commonScalacOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xfuture",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-value-discard"
)
