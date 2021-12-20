package scalatrie

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TrieSpec extends AnyFlatSpec with Matchers {

//  "The Trie object" when
    "empty" should "add a single word" in {
      val word: String = "tree"
      val trie: Trie = Trie().add(word)
      trie.validWord(word) shouldBe true
    }




  "The Trie object" should "add multiple words to an empty Trie" in {
    val words: Seq[String] = Seq("tree", "treat", "trap", "tooth")
    val trie: Trie = Trie().addMany(words)
    words.map { word => trie.validWord(word) shouldBe true }
  }

}
