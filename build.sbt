name := "scala-otp"

version := "1.0"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "RoundEights" at "http://maven.spikemark.net/roundeights",
  "jitpack" at "https://jitpack.io"
)

libraryDependencies ++= Seq(
  "com.roundeights" %% "hasher" % "1.2.0",
  "commons-codec" % "commons-codec" % "1.9",
  "com.github.kenglxn" % "QRGen" % "2.1.0",
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.0",
  "org.scalatest"         %     "scalatest_2.11"    % "2.2.4"     % "test"
)
