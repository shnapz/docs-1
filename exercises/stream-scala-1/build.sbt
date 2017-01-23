name := "stream-scala-1"

version := "1.0"

scalaVersion := "2.12.1"
//scalaVersion := "2.11.8"

libraryDependencies <+= scalaVersion( "org.scala-lang" % "scala-reflect" % _ )
libraryDependencies ++= Seq(
 "org.scalaz" %% "scalaz-core" % scalazVersion,
 "org.scalaz" %% "scalaz-effect" % scalazVersion,
 "co.fs2" %% "fs2-scalaz" % "0.2.0",
 "co.fs2" %% "fs2-io" % "0.9.2"
)

