import Dependencies._

ThisBuild / scalaVersion     := "2.13.7"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "trie"

githubOwner       := "mattjp"
githubRepository  := "scala-trie"
githubTokenSource := TokenSource.Or(
    TokenSource.Environment("GITHUB_TOKEN"), // Injected during a github workflow for publishing
    TokenSource.GitConfig("github.token")    // Local token set in ~/.gitconfig
)

lazy val root = (project in file("."))
  .settings(
    name := "scala-trie",
    libraryDependencies += scalaTest % Test
  )
