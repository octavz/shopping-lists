import sbt._

//import play.Project._

import sbt.Keys._

//import play.Play.autoImport._


object AppBuild extends Build {
  val appName = "PlannerService"

  val appVersion = "0.2"

  val appResolvers = Seq(
    "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases",
    "java-net" at "http://download.java.net/maven/2",
    "Sedis Repo" at "http://pk11-scratch.googlecode.com/svn/trunk",
    "Clojars " at "http://clojars.org/repo",
    "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

  val appDependencies = Seq(
    "com.typesafe.slick" %% "slick" % "3.0.0",
    "com.typesafe.play" %% "play-cache" % "2.4.1",
    "com.typesafe.play" %% "play-jdbc" % "2.4.1",
    "com.typesafe.play" %% "play-json" % "2.4.1",
    "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
    "com.typesafe.play" %% "play-slick" % "1.0.0",
    "com.nulab-inc" %% "play2-oauth2-provider" % "0.15.0",
    //    "org.mockito" % "mockito-all" % "2.0.2-beta",
    //"com.wix" %% "accord-core" % "0.4-SNAPSHOT",
    //    "com.wordnik" %% "swagger-play2" % "1.3.12",
    "pl.matisoft" %% "swagger-play24" % "1.4",
    "com.wordnik" %% "swagger-core" % "1.3.12",
    "com.wordnik" %% "swagger-jaxrs" % "1.3.12",
    "net.sourceforge.htmlunit" % "htmlunit" % "2.17" % "test",
    "com.github.nscala-time" %% "nscala-time" % "2.0.0",
    "com.livestream" %% "scredis" % "2.0.6",
    "com.h2database" % "h2" % "1.4.187",
    //    "com.github.kstyrc" % "embedded-redis" % "0.6",
    //"com.fasterxml.jackson.core" % "jackson-core" % "2.5.4",
    //"com.fasterxml.jackson.core" % "jackson-annotations" % "2.5.4",
    //"com.fasterxml.jackson.core" % "jackson-databind" % "2.5.4",
    //"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.0-rc2",
    "com.typesafe.slick" %% "slick-codegen" % "3.0.0"
  )

  lazy val main = Project(appName, file(".")).dependsOn(gen).enablePlugins(play.sbt.Play).settings(
    scalaVersion := "2.11.7",
    libraryDependencies ++= appDependencies,
    resolvers ++= appResolvers,
//    Keys.fork in (Test) := true,
//    javaOptions in (Test) += "-Xdebug",
//    javaOptions in (Test) += "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9998",
    slick <<= slickCodeGenTask // register manual sbt command
  )

  //  lazy val slick = TaskKey[Seq[File]]("gen-tables")
  //  lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  //    val outputDir = (dir / "slick").getPath // place generated files in sbt's managed sources folder
  //  //  val url = "jdbc:h2:mem:test;INIT=runscript from 'src/main/sql/create.sql'" // connection info for a pre-populated throw-away, in-memory db for this demo, which is freshly initialized on every run
  //  val url = "jdbc:postgresql://localhost:5432/planner"
  //    val jdbcDriver = "org.postgresql.Driver"
  //    val slickDriver = "scala.slick.driver.PostegresqlDriver"
  //    val pkg = "demo"
  //    toError(r.run("scala.slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg), s.log))
  //    val fname = outputDir + "/demo/Tables.scala"
  //    Seq(file(fname))
  //  }
  lazy val gen = Project(
    id = "codegen",
    base = file("codegen")
  ).settings(
      resolvers ++= appResolvers,
      scalaVersion := "2.11.7",
      libraryDependencies ++= Seq(
        "com.typesafe.slick" %% "slick" % "3.0.0",
        "com.typesafe" % "config" % "1.3.0",
        "com.typesafe.slick" %% "slick-codegen" % "3.0.0",
        "io.backchat.inflector" %% "scala-inflector" % "1.3.5"
      )
    )
  //
  //  //play.Project.playScalaSettings
  // code generation task that calls the customized code generator
  lazy val slick = TaskKey[Seq[File]]("gen-tables")

  lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir / "slick").getPath
    toError(r.run("SlickGenerator", cp.files, Array(outputDir), s.log))
    Seq(file(""))
  }
}

