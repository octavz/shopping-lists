// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "SBT repository" at "http://repo.typesafe.com/typesafe/simple/maven-releases/"

resolvers += "Third party repository" at "http://repo.typesafe.com/typesafe/simple/third-party/"

resolvers += "Maven mirror" at "http://mirrors.ibiblio.org/pub/mirrors/maven2"

resolvers += "Maven" at "http://repo1.maven.org/maven2/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.6")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
addSbtPlugin("com.iheart" % "sbt-play-swagger" % "0.5.2")
//addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.4.0")

//addSbtPlugin("org.ensime" % "ensime-sbt" % "0.1.5")

//addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.1.5")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")
