package orsen.datainterface

import orsen.models.Sentence
import orsen.models.Entity

trait DataInterface {

  var mockedRawData = Array(
      "4787    LUANDA , Sept. 17 -LRB- Xinhua -RRB-",
      "4788    Angola 's ruling party the MPLA gained 81.64 percent of the votes in the final results of the country 's parliamentary elections , the Angolan electoral commission announced late Tuesday .",
      "4789    It was Angola 's first election since a 27-year civil war ended in the country in 2002 .",
      "4790    The Popular Movement for the Liberation of Angola -LRB- MPLA -RRB- won 191 seats in the 220-member parliament in the election on September 5 while the main opposition party the National Union for the Total Independence of Angola -LRB- UNITA -RRB- garnered 10.39 percent of the votes , having 16 deputies elected into the national assembly .",
      "4791    The turnout was 87 percent , according to the commission .",
      "4792    Over 8.2 million Angolans registered for the elections and cast their votes at 12,274 polling stations across the country on September 5 .",
      "4793    ¡ ¡ ¡ ¡ ¡ ¡",
      "4794    To show the world that Angola pursues after democracy , the government invited 1,200 foreign observers from 17 international organizations and 10 countries to follow up the elections .",
      "4795    ¡ ¡ Fourteen out of Angola 's 150 political parties and coalitions took part in the elections to vie for the 220 parliamentary seats .",
      "4796    ¡ ¡")

  var mockedRawEntities = Array(
      "Luanda",
      "Angola",
      "Popular Movement for the Liberation of Angola")

  /** Returns an iterator over all sentences in the data corpus.
    * Sentences are represented by Sentence model objects.
    *
    * @returns an iterator of Sentence objects
    */
  def getSentences(): Iterator[Sentence] = {
    def mockedRawDataParser(s: String): Sentence = {
      var split = s.split("    ")
      return new Sentence(split(0).toInt, split(1).split(" "))
    }

    return mockedRawData.map(s => mockedRawDataParser(s)).iterator
  }


  /** Returns the sentence that is associated with this global sentence id.
    *
    * @returns a Sentence object
    */
  def getSentenceById(sentenceId: Integer): Sentence {
    // STUB
  }



  /** Returns an iterator over all existing entities in the entity database.
    * Entities are represented by Entity model objects.
    *
    * @returns an iterator of Entity objects
    */
  def getEntities(): Iterator[Entity] {
    // STUB
  }



  /** Returns the Entity that is associated with this global entity id.
    *
    * @returns an Entity object
    */
  def getEntityById(entityId: Integer): Entity {
    // STUB
  }


  /** Quick sanity test
    */
  def main(arguments: Array[String]) {
    println("DataInterface sanity check")
    var it = this.getSentences()
    while (it.hasNext) {
      println(it.next.tokens)
    }
  }
}
