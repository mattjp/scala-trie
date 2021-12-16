package scalatrie

case class Trie(
  valid: Boolean = false,
  children: Map[String, Trie] = Map()
) {

  /**
   *
   * @param word -
   * @param trie -
   * @return
   */
  def add(word: String, trie: Trie = this): Trie = {
    if (word.isEmpty) trie.copy(valid = true)
    else {
      val (h, t) = word.splitAt(1)
      val updatedChildren: Map[String, Trie] =
        if (trie.children.contains(h)) trie.children
        else trie.children + (h -> Trie())

      val updatedTrie: Trie = add(t, updatedChildren(h))
      trie.copy(children = updatedChildren + (h -> updatedTrie))
    }
  }

  /**
   *
   * @param prefix -
   * @param trie -
   * @return
   */
  def traverse(prefix: String, trie: Trie = this): Option[Trie] = {
    if (prefix.isEmpty) Some(trie)
    else {
      val (h, t) = prefix.splitAt(1)
      if (trie.children.contains(h)) traverse(t, trie.children(h))
      else None
    }
  }

  /**
   *
   * @param trie -
   * @return
   */
  def getSuffixes(trie: Trie = this): Seq[String] = {

    def go(trie: Trie, suffix: String = "", words: Seq[String] = Seq()): Seq[String] = {
      if (trie.valid) words :+ suffix
      else {
        trie.children.foldLeft(words) { (updatedWords, child) =>
          val (char, childTrie) = child
          go(childTrie, suffix + char, updatedWords)
        }
      }
    }

    // Run and return helper function
    go(trie)
  }

  /**
   *
   * @param prefix -
   * @param trie -
   * @return
   */
  def validPrefix(prefix: String, trie: Trie = this): Boolean = {
    traverse(prefix, trie) match {
      case Some(_) => true
      case None    => false
    }
  }

  /**
   *
   * @param prefix -
   * @param trie -
   * @return
   */
  def validWord(prefix: String, trie: Trie = this): Boolean = {
    traverse(prefix, trie) match {
      case Some(t) => t.valid
      case None    => false
    }
  }

}


trait Greeting {
  lazy val greeting: String = "I am trie."
}

object Trie2 extends Greeting with App {
  println(greeting)

  val words: Seq[String] = Seq("hello", "henlo", "hola", "hi")
  val trie: Trie = words.foldLeft(Trie()) { (t, s) => t.add(s) }

  println(trie)

  println(trie.validPrefix("hel")) // true
  println(trie.validPrefix("h")) // true
  println(trie.validPrefix("hola")) // true
  println(trie.validPrefix("holb")) // false

  println(trie.getSuffixes(trie.traverse("hej").get))

//  val t: Trie = Trie()
//  val u: Trie = t.add("hello")
//  val v: Trie = u.add("henlo")

}
