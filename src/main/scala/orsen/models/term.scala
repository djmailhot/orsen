package orsen.models

/** A Term that represents a phrase in a body of text.
  * A phrase can consist of one or more tokens of text.
  *
  * @constructor create a new Term with an id, and a list of tokens
  * @param id the Term's unique id
  * @param tokens the list of tokens that make up this Term
  */
class Term(id: Integer, tokens: Array[String]) {
  
  // the global id of this Term
  def id = this.id
  // the raw tokenized text of this Term
  def text = tokens.mkString(" ")
  // a list of all tokens in this Term
  def tokens = tokens.clone()

  override def toString() = text
}
