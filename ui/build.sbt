version := "0.09"

name := "ui"

scalaVersion := "2.9.1"

scalacOptions += "-deprecation"

resolvers ++= Seq(
  "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
)

libraryDependencies ++= Seq(
    "net.liftweb" %% "lift-webkit" % "2.4" % "compile->default",
    "junit" % "junit" % "4.5" % "test->default",
    "javax.servlet" % "servlet-api" % "2.5" % "provided->default",
    "ch.qos.logback" % "logback-classic" % "0.9.26" % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test,container",
    "org.scala-tools.testing" %% "specs" % "1.6.9" % "test"
)

seq(webSettings :_*)