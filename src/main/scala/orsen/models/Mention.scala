
package orsen.models

/** A Mention represents a token or n-gram of tokens that might reference an Entity
  *
  * @constructor create a new Mention with a unique name, and a list of tokens
  * @param id A unique id for this Mention
  * @param text The text of this token
  */
class Mention(_id: Int, _text: String, _tokenIds: Array[Int]) {
  def this(_id: Int, _text: String, _tokens: Array[Token]) {
    this(_id, _text, _tokens.map((t) => t.id))
  }

  /** The unique id of this Mention */
  def id: Int = _id
  /** The name of this Mention */
  def text: String = _text
  /** The ids of the tokens that make up this Mention */
  def tokenIds: Array[Int] = _tokenIds

  override def toString() = "Mention|id: %d, text: %s".format(this.id, this.text)
}
