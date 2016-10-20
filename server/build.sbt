scalacOptions ++= Seq("-feature")

//net.virtualvoid.sbt.graph.Plugin.graphSettings

scalaVersion := "2.11.8"

val appName = "shopping-list"

val appVersion = "0.1"

val appResolvers = Seq(
  "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases",
  "java-net" at "http://download.java.net/maven/2",
  "Sedis Repo" at "http://pk11-scratch.googlecode.com/svn/trunk",
  "Clojars " at "http://clojars.org/repo",
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies ++= Seq(
  specs2 % Test,
  cache,
  json,
  filters,
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "com.nulab-inc" %% "play2-oauth2-provider" % "0.15.0",
  //    "org.mockito" % "mockito-all" % "2.0.2-beta",
  //"com.wix" %% "accord-core" % "0.4-SNAPSHOT",
  "com.github.nscala-time" %% "nscala-time" % "2.0.0",
  "com.livestream" %% "scredis" % "2.0.6",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.2",
  "org.webjars" % "swagger-ui" % "2.2.2"
)

lazy val main = (project in file(".")).enablePlugins(PlayScala, SwaggerPlugin)

swaggerDomainNameSpaces := Seq("org.shopping.dto")


