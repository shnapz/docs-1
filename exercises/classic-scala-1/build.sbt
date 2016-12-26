name := "day-exersize"

version := "1.0"

//scalaVersion := "2.12.1"
scalaVersion := "2.11.8"

libraryDependencies <+= scalaVersion( "org.scala-lang" % "scala-reflect" % _ )

