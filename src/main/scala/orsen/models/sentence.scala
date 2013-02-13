package orsen.models

/** A Sentence that represents one sentence in a body of text. A sentence
  * more formally consists of an ordered list of tokens
  *
  * @param id the Sentence's unique id
  * @param text the raw tokenized text contents of the Sentence.  
  * @param (optional) delim the delimitor character that the sentence is
  *        tokenized by.  Defaults to a single space (" ") character.
  */
class Sentence(sId: Integer, sText: String, delim: String = " ") {

  // the global id of this sentence
  def id = sId
  // the raw tokenized text of this sentence
  def text = sText
  // a list of all tokens in this sentence
  def tokens = sText.split(delim)

  override def toString() = sText

}
