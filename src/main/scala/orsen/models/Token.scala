package orsen.models

/** A Token that represents a phrase in a body of text.
  * A phrase can consist of one or more tokens of text.
  *
  * @constructor create a new Token with a unique name, and a list of tokens
  * @param id A unique id for this Token
  * @param text The text of this token
  */
class Token(_id: Int, _text: String, _sentenceId: Int) {

  /** The unique id of this Token */
  def id: Int = _id
  /** The name of this Token */
  def text: String = _text
  /** The id of the sentence containing this Token */
  def sentenceId: Int = _sentenceId

  override def toString() = "Token|id: %d, text: %s".format(this.id, this.text)
}
