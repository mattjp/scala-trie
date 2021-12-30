package scalatrie

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TrieSpec extends AnyFlatSpec with Matchers {

  behavior of "An empty Trie"

  val trie: Trie = Trie()

  it should "add a single word" in {
    val word: String = "tree"
    trie.add(word)
    trie.validWord(word) shouldBe true // add is dependent on validWord, and vice versa
  }



  "The Trie object" should "add multiple words to an empty Trie" in {
    val words: Seq[String] = Seq("tree", "treat", "trap", "tooth")
    val trie: Trie = Trie().addMany(words)
    words.map { word => trie.validWord(word) shouldBe true }
  }

}
