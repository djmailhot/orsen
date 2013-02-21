package orsen.models

/** A Term that represents a phrase in a body of text.
  * A phrase can consist of one or more tokens of text.
  *
  * @constructor create a new Term with a unique name, and a list of tokens
  * @param tokens the list of tokens that make up this Term
  */
class Term(_id: Integer, _name: String, _sentenceId: Integer) {

  /** The unique id of this Term */
  def id: Integer = _id
  /** The name of this Term */
  def name: String = _name
  /** The id of the sentence containing this term */
  def sentenceId: Integer = _sentenceId

  override def toString() = "Term|id: %d, name: %s".format(this.id, this.name)
}
