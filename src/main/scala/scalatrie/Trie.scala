package scalatrie

case class Trie(
  valid: Boolean = false,
  children: Map[String, Trie] = Map()
) {

  /**
   * Add a word to a Trie.
   * @param word - The word to add to the Trie.
   * @param trie - The Trie where the word is being added.
   * @return A Trie with the additional word added.
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
   * Add multiple words to a given Trie.
   * @param words - Sequence of words to add to the Trie.
   * @param trie - The Trie where the words will be added.
   * @return A Trie with the additional words added.
   */
  def addMany(words: Seq[String], trie: Trie = this): Trie = {
    words.foldLeft(trie) { (t, s) => t.add(s) }
  }


  /**
   * Remove a given word from a Trie. If the word does not exist in the Trie, return the
   * original Trie.
   * @param word - The word to be removed from the Trie.
   * @param trie - The Trie from which the word will be deleted.
   * @return Boolean and Trie pair. If the boolean is false, the word does not exist in the Trie.
   *         If the boolean is true, the word was removed from the Trie.
   */
  def remove(word: String, trie: Trie = this): (Boolean, Trie) = {

    def go(word: String, trie: Trie = this): Trie = {
      if (word.isEmpty) trie.copy(valid = false)
      else {
        val (h, t) = word.splitAt(1)
        val child: Trie = go(t, trie.children(h))

        // Remove child nodes that have no children
        val updatedChildren =
          if (child.children.isEmpty) trie.children - h
          else trie.children + (h -> child)

        trie.copy(children = updatedChildren)
      }
    }

    // Only remove valid words
    if (trie.validWord(word)) (true, go(word, trie))
    else (false, trie)
  }


  /**
   * Remove multiple words from a Trie. Return the updated Trie with words removed, along
   * with a list of words that did not exist in the Trie.
   * @param words - List of words to be removed from the Trie.
   * @param trie - The Trie where words will be removed.
   * @return List of words that were not removed from the Trie, and the updated Trie.
   */
  def removeMany(words: Seq[String], trie: Trie = this): (Seq[String], Trie) = {
    words.foldLeft((Seq[String](), trie)) {
      (z, word) => {
        val (missingWords, t) = z
        val (removed, updatedTrie) = t.remove(word)
        if (removed) (missingWords, updatedTrie)
        else (missingWords :+ word, updatedTrie)
      }
    }
  }


  /**
   * Traverse a string, returning the Trie node of the last character in the string,
   * if it exists in the Trie.
   * @param prefix - The string to traverse.
   * @param trie - The current node to search.
   * @return The Trie node of the last character in the string, if it exists.
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
   * DFS from a given Trie node to create a list of all suffixes that form valid words.
   * Running this function from the root node of a Trie will yield all valid words in that Trie.
   * @param trie - The current node to find all suffixes.
   * @return List of all suffixes that form valid words for a given Trie.
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
   * Traverse a string, returning false if the string does not exist in the Trie.
   * @param prefix - The string to traverse.
   * @param trie - The current node to begin the prefix search.
   * @return True if the prefix exists in the Trie, false otherwise.
   */
  def validPrefix(prefix: String, trie: Trie = this): Boolean = {
    traverse(prefix, trie) match {
      case Some(_) => true
      case None    => false
    }
  }


  /**
   * Traverse a string, returning false if the string is not a valid word in the Trie.
   * @param prefix - The string to traverse.
   * @param trie - The current node to begin the search.
   * @return True if the prefix is a valid word, false otherwise.
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
  val trie: Trie = Trie().addMany(words)
//
//  println(trie)
//
//  println(trie.validPrefix("hel")) // true
//  println(trie.validPrefix("h")) // true
//  println(trie.validPrefix("hola")) // true
//  println(trie.validPrefix("holb")) // false
//
//  println(trie.getSuffixes(trie.traverse("h").get))


  val (b, t) = trie.removeMany(Seq("henlo", "hi", "hols"))
  println(b)
  println(t.validWord("henlo"))
  println(t.validPrefix("henlo"))
  println(t.validWord("hi"))
  println(t.validPrefix("hi"))
  println(t.validWord("hola"))
  println(t.getSuffixes())

//  val t: Trie = Trie()
//  val u: Trie = t.add("hello")
//  val v: Trie = u.add("henlo")

}
