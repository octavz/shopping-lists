scalacOptions ++= Seq("-feature")

net.virtualvoid.sbt.graph.Plugin.graphSettings

libraryDependencies += specs2 % Test

libraryDependencies += evolutions

