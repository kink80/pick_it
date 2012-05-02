version := "0.09"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "org.apache.httpcomponents" % "httpclient" % "4.0.3" % "compile",
    "com.googlecode.json-simple" % "json-simple" % "1.1.1" % "compile",
    "com.typesafe.akka" % "akka-actor" % "2.0"
)