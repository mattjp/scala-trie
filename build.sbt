import Dependencies._

Global / lintUnusedKeysOnLoad := false

ThisBuild / scalaVersion         := "2.13.7"
ThisBuild / version              := "0.2.0-SNAPSHOT"
ThisBuild / versionScheme        := Some("early-semver")
ThisBuild / organization         := "io.github.mattjp"
ThisBuild / organizationName     := "mattjp"
ThisBuild / organizationHomepage := Some(url("http://github.com/mattjp/"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/mattjp/scala-trie"),
    "scm:git@github.com:mattjp/scala-trie.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id    = "mattperetick",
    name  = "Matt Pereick",
    email = "mjperetick@gmail.com",
    url   = url("http://mattjp.co")
  )
)

ThisBuild / description := "Trie data structure for Scala."
ThisBuild / licenses    := List("GNU GPL 3" -> new URL("https://www.gnu.org/licenses/gpl-3.0.en.html"))
ThisBuild / homepage    := Some(url("https://github.com/mattjp/scala-trie"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishMavenStyle := true
ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

lazy val root = (project in file("."))
  .settings(
    name := "scalaTrie",
    libraryDependencies += scalaTest % Test,
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")
  )
