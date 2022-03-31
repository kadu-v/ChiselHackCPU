// See README.md for license details.

ThisBuild / scalaVersion := "2.12.13"
ThisBuild / version := "0.1.0"
ThisBuild / organization := "com.github.kaduv"

// lazy val root = (project in file("."))
//   .settings(
//     name := "ChiselHackCPU",
//     libraryDependencies ++= Seq(
//       "edu.berkeley.cs" %% "chisel3" % "3.5.0",
//       "edu.berkeley.cs" %% "chiseltest" % "0.3.4" % "test"
//     ),
//     scalacOptions ++= Seq(
//       "-Xsource:2.11",
//       "-language:reflectiveCalls",
//       "-deprecation",
//       "-feature",
//       "-Xcheckinit",
//       // Enables autoclonetype2 in 3.4.x (on by default in 3.5)
//       "-P:chiselplugin:useBundlePlugin"
//     ),
//     addCompilerPlugin(
//       "edu.berkeley.cs" % "chisel3-plugin" % "3.4.3" cross CrossVersion.full
//     ),
//     addCompilerPlugin(
//       "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full
//     )
//   )

val chiselVersion = "3.5.1"

lazy val root = (project in file("."))
  .settings(
    name := "%NAME%",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % chiselVersion,
      "edu.berkeley.cs" %% "chiseltest" % "0.5.1" % "test"
    ),
    scalacOptions ++= Seq(
      "-language:reflectiveCalls",
      "-deprecation",
      "-feature",
      "-Xcheckinit",
      "-P:chiselplugin:genBundleElements"
    ),
    addCompilerPlugin(
      "edu.berkeley.cs" % "chisel3-plugin" % chiselVersion cross CrossVersion.full
    )
  )
