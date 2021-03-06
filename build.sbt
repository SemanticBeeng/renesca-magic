name := "renesca-magic"

// don't forget to change the version in README.md
version := "1.0.0-SNAPSHOT"
val scalaV = "2.11.8"
val paradiseVersion = "2.1.0"
scalaVersion := scalaV


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases" // specs2

libraryDependencies ++= Seq(
  "com.github.renesca" %% "renesca" % "0.3.2-9" % "test",
  "org.scala-lang" % "scala-reflect" % scalaV,
  "org.specs2" %% "specs2-core" % "3.6.6" % "test", //TODO: higher specs versions seem to produce a memory leak...
  "org.specs2" %% "specs2-mock" % "3.6.6" % "test",
  "org.scala-lang" % "scala-compiler" % scalaV % "test"
)

addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)
scalacOptions ++= scalacOpts

// scalaxy (faster collection operations)
scalacOptions += "-Xplugin-require:scalaxy-streams"
scalacOptions in Test ~= (_ filterNot (_ == "-Xplugin-require:scalaxy-streams"))
scalacOptions in Test += "-Xplugin-disable:scalaxy-streams"
autoCompilerPlugins := true
addCompilerPlugin("com.nativelibs4java" %% "scalaxy-streams" % "0.3.4")

scalacOptions in Test ++= Seq("-Yrangepos") // specs2
parallelExecution in Test := false

// publishing
pgpSecretRing := file("local.secring.gpg")
pgpPublicRing := file("local.pubring.gpg")
organization := "com.github.renesca"

pomExtra := {
  <url>https://github.com/renesca/renesca-magic</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <url>https://github.com/renesca/renesca-magic</url>
      <connection>scm:git:git@github.com:renesca/renesca-magic.git</connection>
    </scm>
    <developers>
      <developer>
        <id>fdietze</id>
        <name>Felix Dietze</name>
        <url>https://github.com/fdietze</url>
      </developer>
      <developer>
        <id>jkaroff</id>
        <name>Johannes Karoff</name>
        <url>https://github.com/cornerman</url>
      </developer>
    </developers>
}

val scalacOpts = (
  "-encoding" :: "UTF-8" ::
  "-unchecked" ::
  "-deprecation" ::
  "-explaintypes" ::
  "-feature" ::
  "-Yinline" :: "-Yinline-warnings" ::
  "-language:_" ::
  // "-Xdisable-assertions" :: "-optimize" ::
  Nil
)

val scalacMacroOpts = (
  "-Ymacro-debug-lite" ::
  "-Yshow-trees-stringified" ::
  Nil
)

fullClasspath in Test := {
  val defaultValue = (fullClasspath in Test).value
  val classpath = defaultValue.files.map(_.getAbsolutePath)
  System.setProperty("sbt.paths.tests.classpath", classpath.mkString(java.io.File.pathSeparatorChar.toString))
  defaultValue
}
