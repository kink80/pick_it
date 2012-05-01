version := "0.09"


libraryDependencies ++= {
    val liftVersion = "2.4"
    Seq(
        "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default",
        "net.liftweb" %% "lift-openid" % liftVersion % "compile->default",
        "net.liftweb" %% "lift-record" % liftVersion,
        "net.liftweb" %% "lift-mongodb" % "2.4",
        "net.liftweb" %% "lift-mongodb-record" % "2.4",
        "org.scala-tools.testing" % "specs_2.8.1" % "1.6.8"
    )
}