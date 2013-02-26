
package orsen.models

/** A Mention represents a token or n-gram of tokens that might reference an Entity
  *
  * @constructor create a new Mention with a unique name, and a list of tokens
  * @param id A unique id for this Mention
  * @param text The text of this token
  */
class Mention(_id: Integer, _text: String) {

  /** The unique id of this Mention */
  def id: Integer = _id
  /** The name of this Mention */
  def text: String = _text
  /** The ids of the tokens that make up this Mention */
  // TODO: 
  var tokenIDs: Array[Integer] = Array()


  override def toString() = "Mention|id: %d, text: %s".format(this.id, this.text)
}
