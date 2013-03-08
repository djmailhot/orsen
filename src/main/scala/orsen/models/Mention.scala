package orsen.models

import scala.collection.mutable

/** A Mention represents a token or n-gram of tokens that might reference an Entity
  *
  * It is assumed that in a given runtime, there will never exist more than one Mention
  * of the same id. Thus, all Mention objects of the same id are equivalent.
  *
  * @constructor create a new Mention with a unique name, and a list of tokens
  * @param id A unique id for this Mention
  * @param text The text of this token
  */
class Mention(_id: Int, _text: String, _tokenIds: Array[Int]) {
  def this(_id: Int, _text: String, _tokens: Array[Token]) {
    this(_id, _text, _tokens.map((t) => t.id))
  }
  def this(_id: Int, _text: String, _tokens: mutable.ArrayBuffer[Token]) {
    this(_id, _text, _tokens.toArray)
  }
  def this(_id: Int, _text: String, _token: Token) {
    this(_id, _text, Array(_token))
  }
  def this(_id: Int, _text: String, _tokenId: Int) {
    this(_id, _text, Array(_tokenId))
  }

  /** The unique id of this Mention */
  def id: Int = _id
  /** The name of this Mention */
  def text: String = _text
  /** The ids of the tokens that make up this Mention */
  def tokenIds: Array[Int] = _tokenIds

  override def toString() = "Mention|id: %d, text: %s".format(this.id, this.text)

  override def hashCode() = id.hashCode()

  override def equals(other: Any) = other match {
    case that: Mention => this.id == that.id
    case _ => false
  }

}
