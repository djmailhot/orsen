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

  /** Returns the text tokens that are associated with this sentenceId.
    *
    * @return a list of Token objects
    * @throws NoSuchElementException if sentenceID does not match any Sentence
    */
  def getTokensById(sentenceId: Integer): Array[String]

  /** Returns the Part Of Speech tag for the text tokens of this sentenceId.
    *
    * @return a list of Strings that are Part Of Speech tags
    * @throws NoSuchElementException if sentenceID does not match any Sentence
    */
  def getPOStagsById(sentenceId: Integer): Array[String]

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

  /** Returns an iterator over all terms in the data corpus.
    * Terms are represented by Term model objects.
    *
    * @return an iterator of Term objects
    */
  def getTerms(): Iterator[Term]

  /** Returns an iterator over all terms in the given sentence
    * Terms are represented by Term model objects.
    *
    * @return an iterator of Term objects
    * @throws NoSuchElementException if sentenceId does not match any Sentence
    */
  def getTermsOfSentence(sentenceId: Integer): Iterator[Term]

  /** Returns the Term that is associated with this termId
    * Terms are represented by Term model objects.
    *
    * @return an iterator of Term objects
    * @throws NoSuchElementException if termId does not match any Term
    */
  def getTermById(termId: Integer): Term
}
