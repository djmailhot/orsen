package orsen.models

/** A Token that represents a phrase in a body of text.
  * A phrase can consist of one or more tokens of text.
  *
  * @constructor create a new Token with a unique name, and a list of tokens
  * @param id A unique id for this Token
  * @param text The text of this token
  */
class Token(_id: Int, _text: String, _sentenceId: Int, _posTag: String, _nerTag: String) {
  def this(_id: Int, _text: String, _sentenceId: Int) {
    this(_id, _text, _sentenceId, Token.UNKNOWN_TOKEN, Token.UNKNOWN_TOKEN)
  }

  /** The unique id of this Token */
  def id: Int = _id
  /** The name of this Token */
  def text: String = _text
  /** The id of the sentence containing this Token */
  def sentenceId: Int = _sentenceId
  /** The Part Of Speech tag for this Token */
  def posTag: String = _posTag
  /** The Named Entity Recognizer tag for this Token */
  def nerTag: String = _nerTag


  def addPOStag(_posTag: String): Token = {
    return new Token(id, text, sentenceId, _posTag, nerTag)
  }

  def addNERtag(_nerTag: String): Token = {
    return new Token(id, text, sentenceId, posTag, _nerTag)
  }


  override def toString() = "Token|id: %d, text: %s".format(this.id, this.text)
}

object Token {
  val UNKNOWN_TOKEN = "<UNKNOWN_TOKEN>"
}
