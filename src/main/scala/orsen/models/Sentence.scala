package orsen.models

// TODO: Come from arbitrary DataInterface specified in a config file
import orsen.datainterface.stub.StubDataInterface

/** A Sentence that represents one sentence in a body of text. A sentence
  * more formally consists of an ordered list of tokens
  *
  * @param id the Sentence's unique id
  * @param tokenIds an array of tokenized text making up the Sentence.  
  */
class Sentence(sentenceId: Int, _tokenIds: Array[Int]) {

  /** The unique id of this Sentence */
  def id: Int = sentenceId
  /** A Array of all tokens in this Sentence */
  def tokenIds: Array[Int] = _tokenIds
  /** The raw tokenized text of this Sentence */
  // def getText: String = sentenceTokens.mkString(" ")

  def tokenIterator: Iterator[Token] = {
    // TODO: Come from arbitrary DataInterface specified in a config file
    StubDataInterface.getTokensOfSentence(this.id)
  }

  override def toString() = "<Sentence|id: %d>".format(this.id)
}
