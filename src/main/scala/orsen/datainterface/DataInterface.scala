package orsen.datainterface

import orsen.models._

/** A DataInterface is a singleton that provides an ORM interface to the data corpus
  *
  */
trait DataInterface {
  /** Returns an iterator over all sentences in the data corpus.
    * Sentences are represented by Sentence model objects.
    *
    * @return an iterator of Sentence objects
    */
  def getSentences(): Iterator[Sentence]

  /** Returns the sentence that is associated with this sentenceId.
    *
    * @return a Sentence object
    * @throws NoSuchElementException if sentenceID does not match any Sentence
    */
  def getSentenceById(sentenceId: Integer): Sentence

  /** Returns an iterator over all existing entities in the entity database.
    * Entities are represented by Entity model objects.
    *
    * @return an iterator of Entity objects
    */
  def getEntities(): Iterator[Entity]

  /** Returns the Entity that is associated with this entityId.
    *
    * @return an Entity object
    * @throws NoSuchElementException if entityId does not match any Entity
    */
  def getEntityById(entityId: Integer): Entity

  /** Returns an iterator over all tokens in the data corpus.
    * Tokens are represented by Token model objects.
    *
    * @return an iterator of Token objects
    */
  def getTokens(): Iterator[Token]

  /** Returns an iterator over all tokens in the given sentence
    * Tokens are represented by Token model objects.
    *
    * @return an iterator of Token objects
    * @throws NoSuchElementException if sentenceId does not match any Sentence
    */
  def getTokensOfSentence(sentenceId: Integer): Iterator[Token]

  /** Returns the Token that is associated with this tokenId
    * Tokens are represented by Token model objects.
    *
    * @return an iterator of Token objects
    * @throws NoSuchElementException if tokenId does not match any Token
    */
  def getTokenById(tokenId: Integer): Token
}
