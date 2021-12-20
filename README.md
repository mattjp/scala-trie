# scala-trie

An immutable [Trie](https://en.wikipedia.org/wiki/Trie) class written in Scala.

## Usage

Add the library to `build.sbt` dependencies:

```scala
libraryDependencies += "io.github.mattjp" %% "scalatrie" % "0.1.0"
```

Add the library to Scala code:

```scala
import trie.Trie

val trie: Trie =  Trie().addMany(Seq("trie", "tree"))
trie.validWord("trie") // true
trie.validPrefix("tr") // true
trie.validWord("tr")   // false
trie.getSuffixes("tr") // ["ie", "ee"]
```
