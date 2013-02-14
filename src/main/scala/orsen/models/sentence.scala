package orsen.models

/** A Sentence that represents one sentence in a body of text. A sentence
  * more formally consists of an ordered list of tokens
  *
  * @param id the Sentence's unique id
  * @param tokens an array of tokenized text making up the Sentence.  
  */
class Sentence(id: Integer, tokens: Array[String]) {

  // the global id of this sentence
  def id = this.id
  // the raw tokenized text of this sentence
  def text = this.tokens.mkString(" ")
  // a list of all tokens in this sentence
  def tokens = this.tokens

  override def toString() = text
}
