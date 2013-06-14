import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "scala-orm-test"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "pro.savant.circumflex" % "circumflex-orm" % "3.0-RC1",
    "org.sorm-framework" % "sorm" % "0.3.8",
    "net.fwbrasil" %% "activate-play" % "1.3",
    "net.fwbrasil" %% "activate-jdbc" % "1.3"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
