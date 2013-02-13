package orsen.models

/** A Sentence that represents one sentence in a body of text. A sentence
  * more formally consists of an ordered list of tokens
  *
  * @param id the Sentence's unique id
  * @param text the raw tokenized text contents of the Sentence.  
  * @param (optional) delim the delimitor character that the sentence is
  *        tokenized by.  Defaults to a single space (" ") character.
  */
class Sentence(id: Integer, text: String, delim: String = " ") {

  // the global id of this sentence
  def id = this.id
  // the raw tokenized text of this sentence
  def text = this.text
  // a list of all tokens in this sentence
  def tokens = this.text.split(delim)

  override def toString() = text

}
