package orsen.models

/** A Sentence that represents one sentence in a body of text. A sentence
  * more formally consists of an ordered list of tokens
  *
  * @param id the Sentence's unique id
  * @param tokens an array of tokenized text making up the Sentence.  
  */
class Sentence(sentenceId: Integer, sentenceTokens: Array[String]) {

  // the global id of this sentence
  def id: Integer = sentenceId
  // the raw tokenized text of this sentence
  def text: String = sentenceTokens.mkString(" ")
  // a list of all tokens in this sentence
  def tokens: Array[String] = sentenceTokens

  override def toString() = text
}
