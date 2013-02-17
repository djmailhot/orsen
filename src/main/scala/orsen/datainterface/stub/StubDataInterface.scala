package orsen.datainterface.stub

import orsen.datainterface.DataInterface
import orsen.models._

object StubDataInterface extends DataInterface {
  var mockedSentences = Array(
      new Sentence(4787, Array("LUANDA", ",", "Sept.", "17", "-LRB-", "Xinhua", "-RRB-")),
      new Sentence(4788, Array("Angola", "'s", "ruling", "party", "the", "MPLA", "gained", "81.64", "percent", "of", "the", "votes", "in", "the", "final", "results", "of", "the", "country", "'s", "parliamentary", "elections", ",", "the", "Angolan", "electoral", "commission", "announced", "late", "Tuesday", ".")),
      new Sentence(4789, Array("It", "was", "Angola", "'s", "first", "election", "since", "a", "27-year", "civil", "war", "ended", "in", "the", "country", "in", "2002", ".")),
      new Sentence(4790, Array("The", "Popular", "Movement", "for", "the", "Liberation", "of", "Angola", "-LRB-", "MPLA", "-RRB-", "won", "191", "seats", "in", "the", "220-member", "parliament", "in", "the", "election", "on", "September", "5", "while", "the", "main", "opposition", "party", "the", "National", "Union", "for", "the", "Total", "Independence", "of", "Angola", "-LRB-", "UNITA", "-RRB-", "garnered", "10.39", "percent", "of", "the", "votes", ",", "having", "16", "deputies", "elected", "into", "the", "national", "assembly", ".")),
      new Sentence(4791, Array("The", "turnout", "was", "87", "percent", ",", "according", "to", "the", "commission", ".")),
      new Sentence(4792, Array("Over", "8.2", "million", "Angolans", "registered", "for", "the", "elections", "and", "cast", "their", "votes", "at", "12,274", "polling", "stations", "across", "the", "country", "on", "September", "5", ".")),
      new Sentence(4793, Array("¡", "¡", "¡", "¡", "¡", "¡")),
      new Sentence(4794, Array("To", "show", "the", "world", "that", "Angola", "pursues", "after", "democracy", ",", "the", "government", "invited", "1,200", "foreign", "observers", "from", "17", "international", "organizations", "and", "10", "countries", "to", "follow", "up", "the", "elections", ".")),
      new Sentence(4795, Array("¡", "¡", "Fourteen", "out", "of", "Angola", "'s", "150", "political", "parties", "and", "coalitions", "took", "part", "in", "the", "elections", "to", "vie", "for", "the", "220", "parliamentary", "seats", ".")),
      new Sentence(4796, Array("¡", "¡"))
  )

  var mockedEntities = Array(
      new Entity(1, "Luanda"),
      new Entity(2, "Angola"),
      new Entity(3, "Popular Movement for the Liberation of Angola")
  )

  def getSentences(): Iterator[Sentence] = {
    return mockedSentences.iterator
  }

  def getSentenceById(sentenceId: Integer): Sentence = {
    if (sentenceId < 4787 || 4796 < sentenceId) {
      throw new NoSuchElementException()
    }

    return mockedSentences(sentenceId - 4787)

  }

  def getEntities(): Iterator[Entity] = {
    return mockedEntities.iterator
  }

  def getEntityById(entityId: Integer): Entity = {
    if (entityId < 1 || entityId > 3) {
      throw new NoSuchElementException()
    }

    return mockedEntities(entityId - 1)
  }

  def getTerms(): Iterator[Entity] = null

  def getTermById(termId: Integer): Entity = null
}
