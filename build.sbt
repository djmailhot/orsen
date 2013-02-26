name := "orsen"

version := "1.0"

scalaVersion := "2.9.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "org.mongodb" %% "casbah" % "2.5.0"

resolvers += "Sonatype OSS Releases" at 
  "http://oss.sonatype.org/content/repositories/releases/"

libraryDependencies +=
  "org.scalamock" %% "scalamock-scalatest-support" % "latest.integration"
