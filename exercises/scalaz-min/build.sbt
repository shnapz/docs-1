name := "functor-min"

version := "1.0"

scalaVersion := "2.12.1"

val scalazVersion = "7.2.8"
    
libraryDependencies ++= Seq(
 "org.scalaz" %% "scalaz-core" % scalazVersion,
 "org.scalaz" %% "scalaz-effect" % scalazVersion,
 "co.fs2" %% "fs2-scalaz" % "0.2.0"
 //"org.scalaz.stream" %% "scalaz-stream" % "0.8.5"
)
