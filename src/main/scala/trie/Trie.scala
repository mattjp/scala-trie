package trie

object Trie extends Greeting with App {
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "I am trie."
}
