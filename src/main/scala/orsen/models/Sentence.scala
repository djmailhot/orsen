package orsen.models

// TODO: Come from arbitrary DataInterface specified in a config file
import orsen.datainterface.stub.StubDataInterface
import orsen.detectors._

/** A Sentence that represents one sentence in a body of text. A sentence
  * more formally consists of an ordered list of tokens
  *
  * @param id the Sentence's unique id
  * @param tokenIds an array of tokenized text making up the Sentence.  
  */
class Sentence(sentenceId: Int, _tokenIds: Array[Int], _text: String) {
  def this(_sentenceId:Int) {
    this(_sentenceId, Array[Int](), "")
  }
  def this(_sentenceId:Int, _text: String) {
    this(_sentenceId, Array[Int](), _text)
  }
  def this(_sentenceId: Int, _tokens: Array[Int]) {
    this(_sentenceId, _tokens, "")
  }
  def this(_sentenceId: Int, _tokens: Array[Token]) {
    this(_sentenceId, _tokens.map((token) => token.id), "")
  }

  /** The unique id of this Sentence */
  def id: Int = sentenceId
  /** A Array of all tokens in this Sentence */
  def tokenIds: Array[Int] = _tokenIds
  /** The raw tokenized text of this Sentence */
  def text: String = _text

  def tokenIterator: Iterator[Token] = {
    // TODO: Come from arbitrary DataInterface specified in a config file
    RawMatcher.dataInterface.getTokensOfSentence(this.id)
  }

  override def toString() = "<Sentence|id: %d>".format(this.id)

  override def hashCode() = id.hashCode()

  override def equals(other: Any) = other match {
    case that: Sentence => this.id == that.id
    case _ => false
  }
}
