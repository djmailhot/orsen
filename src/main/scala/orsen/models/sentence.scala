package orsen.models

/** A Sentence that represents one sentence in a body of text. A sentence
  * more formally consists of an ordered list of tokens
  *
  * @param id the Sentence's unique id
  * @param tokens an array of tokenized text making up the Sentence.  
  */
class Sentence(sentenceId: Integer, sentenceTokens: Array[String]) {

  /** The unique id of this Sentence */
  def id: Integer = sentenceId
  /** The raw tokenized text of this Sentence */
  def text: String = sentenceTokens.mkString(" ")
  /** A Array of all tokens in this Sentence */
  def tokens: Array[String] = sentenceTokens

  override def toString() = text
}
