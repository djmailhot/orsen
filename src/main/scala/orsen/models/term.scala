package orsen.models

/** A Term that represents a phrase in a body of text.
  * A phrase can consist of one or more tokens of text.
  *
  * @constructor create a new Term with a unique name, and a list of tokens
  * @param tokens the list of tokens that make up this Term
  */
class Term(termId: Integer, termName: String) {
  /** The unique id of this Term **/
  def id: Integer = termId
  /** The name of this Term **/
  def name: String = termName

  override def toString() = name
}
