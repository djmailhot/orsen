package orsen.datainterface.stub

import scala.collection.mutable.ArrayBuffer
import orsen.datainterface.DataInterface
import orsen.models._

object StubDataInterface extends DataInterface {

  val mockedEntities = Array(
      new Entity(1, "Luanda"),
      new Entity(2, "Angola"),
      new Entity(3, "Popular Movement for the Liberation of Angola"),
      new Entity(4, "Angola")
  )

  val mockedSentences = (4787 to 4796 toArray).map((id) => new Sentence(id, Array[Int]()))

  val text = Array(
      Array("LUANDA", ",", "Sept.", "17", "-LRB-", "Xinhua", "-RRB-"),
      Array("Angola", "'s", "ruling", "party", "the", "MPLA", "gained", "81.64", "percent", "of", "the", "votes", "in", "the", "final", "results", "of", "the", "country", "'s", "parliamentary", "elections", ",", "the", "Angolan", "electoral", "commission", "announced", "late", "Tuesday", "."),
      Array("It", "was", "Angola", "'s", "first", "election", "since", "a", "27-year", "civil", "war", "ended", "in", "the", "country", "in", "2002", "."),
      Array("The", "Popular", "Movement", "for", "the", "Liberation", "of", "Angola", "-LRB-", "MPLA", "-RRB-", "won", "191", "seats", "in", "the", "220-member", "parliament", "in", "the", "election", "on", "September", "5", "while", "the", "main", "opposition", "party", "the", "National", "Union", "for", "the", "Total", "Independence", "of", "Angola", "-LRB-", "UNITA", "-RRB-", "garnered", "10.39", "percent", "of", "the", "votes", ",", "having", "16", "deputies", "elected", "into", "the", "national", "assembly", "."),
      Array("The", "turnout", "was", "87", "percent", ",", "according", "to", "the", "commission", "."),
      Array("Over", "8.2", "million", "Angolans", "registered", "for", "the", "elections", "and", "cast", "their", "votes", "at", "12,274", "polling", "stations", "across", "the", "country", "on", "September", "5", "."),
      Array("¡", "¡", "¡", "¡", "¡", "¡"),
      Array("To", "show", "the", "world", "that", "Angola", "pursues", "after", "democracy", ",", "the", "government", "invited", "1,200", "foreign", "observers", "from", "17", "international", "organizations", "and", "10", "countries", "to", "follow", "up", "the", "elections", "."),
      Array("¡", "¡", "Fourteen", "out", "of", "Angola", "'s", "150", "political", "parties", "and", "coalitions", "took", "part", "in", "the", "elections", "to", "vie", "for", "the", "220", "parliamentary", "seats", "."),
      Array("¡", "¡")
  )

  val mockedTokens = ArrayBuffer[Token]()

  var currentTokenId = 0
  var currentSentenceId = 4787
  text.foreach { (sentence) =>
    sentence.foreach { (tokenText) =>
      mockedTokens += new Token(currentTokenId, tokenText, currentSentenceId)
      currentTokenId += 1
    }
    currentSentenceId += 1
  }

  // Sentences

  def getSentences(): Iterator[Sentence] = {
    return mockedSentences.iterator
  }

  def getSentenceById(sentenceId: Int): Sentence = {
    if (sentenceId < 4787 || 4796 < sentenceId) {
      throw new NoSuchElementException()
    }

    return mockedSentences(sentenceId - 4787)

  }

  // Entities

  def getEntities(): Iterator[Entity] = {
    return mockedEntities.iterator
  }

  def getEntityById(entityId: Int): Entity = {
    if (entityId < 1 || entityId > 3) {
      throw new NoSuchElementException()
    }

    return mockedEntities(entityId - 1)
  }

  // Tokens

  def getTokens(): Iterator[Token] = {
    return mockedTokens.iterator
  }

  def getTokensOfSentence(sentenceId: Int): Iterator[Token] = {
    if (sentenceId < 4787 || 4796 < sentenceId) {
      throw new NoSuchElementException()
    }
    return mockedTokens.iterator
  }

  def getTokenById(tokenId: Int): Token = {
    if (tokenId < 0 || mockedTokens.length < tokenId) {
      throw new NoSuchElementException()
    }
    return mockedTokens(tokenId)
  }
}
