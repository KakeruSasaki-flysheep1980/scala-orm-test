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
    "net.fwbrasil" %% "activate-jdbc" % "1.3",
    "com.googlecode.mapperdao" % "mapperdao" % "1.0.0.rc22-2.10.1"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += Resolver.file("Local Ivy2 Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)
    // Add your own project settings here
  )

}
