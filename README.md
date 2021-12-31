# scalaTrie

An immutable [Trie](https://en.wikipedia.org/wiki/Trie) class written in Scala.

## Usage

Add the library to `build.sbt` dependencies:

```scala
libraryDependencies += "io.github.mattjp" %% "scalatrie" % "1.0.0"
```

Add the library to Scala code:

```scala
import trie.Trie

val trie: Trie =  Trie().addMany(Seq("trie", "tree"))

trie.validWord("trie") // true
trie.validWord("tr")   // false
trie.validPrefix("tr") // true
trie.getSuffixes("tr") // ["ie", "ee"]

val updatedTrie: Trie = trie.remove("tree")
```

## Publishing

0. Update `~/.sbt/sonatype_credentials` with proper publishing credentials.
1. `version` in `build.sbt` should not include the `-SNAPSHOT` suffix.
2. Run `sbt publishSigned`.
3. Navigate to the Nexus staging repositories page: https://s01.oss.sonatype.org/#stagingRepositories (login with Sonatype credentials).
4. Close the staging repository through the console.
5. Release the staging repository through the console.
