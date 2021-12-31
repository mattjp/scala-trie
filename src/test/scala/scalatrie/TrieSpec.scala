package scalatrie

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TrieSpec extends AnyFlatSpec with Matchers {

  trait SampleWords {
    val word: String = "tree"
    val words: Seq[String] = Seq("trie", "treble", "trim", "thin", "have", "hang", "hug")
  }

  trait EmptyTrieBuilder extends SampleWords {
    val trie: Trie = Trie()
  }

  trait TrieBuilder extends SampleWords {
    val trie: Trie = Trie().addMany(words)
  }

  behavior of "An empty Trie"
  it should "add a single word" in new EmptyTrieBuilder {
    val updatedTrie: Trie = trie.add(word)
    updatedTrie.validWord(word) shouldBe true // add is dependent on validWord, and vice versa
  }

  it should "add multiple words" in new EmptyTrieBuilder {
    val updatedTrie: Trie = trie.addMany(words)
    words.foreach { w => updatedTrie.validWord(w) shouldBe true }
  }

  it should "not remove a single word" in new EmptyTrieBuilder {
    val (removed, updatedTrie): (Boolean, Trie) = trie.remove(word)
    removed shouldBe false
    updatedTrie shouldBe trie
  }

  it should "not remove multiple words" in new EmptyTrieBuilder {
    val (removedWords, updatedTrie): (Seq[String], Trie) = trie.removeMany(words)
    removedWords shouldBe words
    updatedTrie shouldBe trie
  }

  it should "return no suffixes" in new EmptyTrieBuilder {
    val suffixes: Seq[String] = trie.getSuffixes()
    suffixes.isEmpty shouldBe true
  }

  behavior of "A non-empty Trie"
  it should "add a single word" in new TrieBuilder {
    val updatedTrie: Trie = trie.add(word)
    updatedTrie.validWord(word) shouldBe true
  }

  it should "not add a word that exists" in new TrieBuilder {
    val existing: String = words.head
    val updatedTrie: Trie = trie.add(existing)
    updatedTrie.validWord(existing) shouldBe true

    val (removed, removedTrie): (Boolean, Trie) = trie.remove(existing)
    removed shouldBe true
    removedTrie.validWord(existing) shouldBe false // only one copy of the word was stored
  }

  it should "add multiple words" in new TrieBuilder {
    val addedWords: Seq[String] = Seq("apple", "apples", "hugs", "hugging")
    val updatedTrie: Trie = trie.addMany(addedWords)
    words.foreach { w => updatedTrie.validWord(w) shouldBe true }
    addedWords.foreach { w => updatedTrie.validWord(w) shouldBe true }
  }

  it should "remove a single valid word" in new TrieBuilder {
    val target: String = words.head
    val (removed, updatedTrie): (Boolean, Trie) = trie.remove(target)
    removed shouldBe true
    updatedTrie.validWord(target) shouldBe false
    words.drop(1).foreach { w => updatedTrie.validWord(w) shouldBe true }
  }

  it should "not remove a single invalid word" in new TrieBuilder {
    val target: String = "tre"
    val (removed, updatedTrie): (Boolean, Trie) = trie.remove(target)
    removed shouldBe false
    updatedTrie shouldBe trie
  }

  it should "remove many valid words" in new TrieBuilder {
    val targets: Seq[String] = words.slice(0, 2)
    val (invalidWords, updatedTrie): (Seq[String], Trie) = trie.removeMany(targets)
    invalidWords.isEmpty shouldBe true
    targets.foreach { t => updatedTrie.validWord(t) shouldBe false }
    words.drop(2).foreach { w => updatedTrie.validWord(w) shouldBe true }
  }

  it should "not remove many invalid words" in new TrieBuilder {
    val targets: Seq[String] = Seq("tre", "tri")
    val (invalidWords, updatedTrie): (Seq[String], Trie) = trie.removeMany(targets)
    invalidWords shouldBe targets
    updatedTrie shouldBe trie
  }

  it should "remove many valid words and not remove many invalid words" in new TrieBuilder {
    val validTargets: Seq[String] = words.slice(0, 2)
    val invalidTargets: Seq[String] = Seq("tre", "tri")
    val (invalidWords, updatedTrie): (Seq[String], Trie) = trie.removeMany(validTargets ++ invalidTargets)
    invalidWords shouldBe invalidTargets
    validTargets.foreach { t => updatedTrie.validWord(t) shouldBe false }
    words.drop(2).foreach { w => updatedTrie.validWord(w) shouldBe true }
  }

  it should "identify a valid prefix" in new TrieBuilder {
    val prefix: String = words.head.substring(0, 2)
    trie.validPrefix(prefix) shouldBe true
  }

  it should "identify an invalid prefix" in new TrieBuilder {
    val prefix: String = "try"
    trie.validPrefix(prefix) shouldBe false
  }

  it should "identify a valid word" in new TrieBuilder {
    val prefix: String = words.head
    trie.validWord(prefix) shouldBe true
  }

  it should "identify an invalid word" in new TrieBuilder {
    val prefix: String = words.head.dropRight(1)
    trie.validWord(prefix) shouldBe false
  }

  it should "return all suffixes for the root node" in new TrieBuilder {
    val suffixes: Seq[String] = trie.getSuffixes()
    words.foreach { w => suffixes.contains(w) shouldBe true }
  }

  it should "return all suffixes for a non-root node" in new TrieBuilder {
    val prefix: String = "t"
    val prefixTrieOpt: Option[Trie] = trie.traverse(prefix)
    prefixTrieOpt should not be None

    prefixTrieOpt.foreach { prefixTrie =>
      val suffixes: Seq[String] = prefixTrie.getSuffixes()
      val expected: Seq[String] = words
        .filter { _.startsWith(prefix) }
        .map { _.drop(1) }

      expected.foreach { e => suffixes.contains(e) shouldBe true }
    }
  }

  it should "traverse a valid prefix" in new TrieBuilder {
    val prefix: String = words.head.substring(0, 3)
    val suffix: String = words.head.substring(3)
    val prefixTrieOpt: Option[Trie] = trie.traverse(prefix)
    prefixTrieOpt should not be None
    prefixTrieOpt.map { prefixTrie =>
      val suffixes: Seq[String] = prefixTrie.getSuffixes()
      suffixes.contains(suffix) shouldBe true
    }
  }

  it should "traverse an invalid prefix" in new TrieBuilder {
    val prefix: String = words.head.substring(1)
    val prefixTrieOpt: Option[Trie] = trie.traverse(prefix)
    prefixTrieOpt shouldBe None
  }

}
