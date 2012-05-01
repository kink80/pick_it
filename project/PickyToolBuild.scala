import sbt._
import Keys._

object PickyToolBuild extends Build {
  lazy val root = Project(id = "PickyTool",
    base = file(".")) aggregate(datamodel, service, ui)

  lazy val datamodel = Project(id="datamodel",
    base = file("datamodel"))

  lazy val dbi = Project(id = "dbi",
    base = file("dbi")) dependsOn(datamodel)

  lazy val service = Project(id = "service",
    base = file("service")) dependsOn(dbi)

  lazy val ui = Project(id = "ui",
    base = file("ui")) dependsOn(dbi)

}