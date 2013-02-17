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

  /** Returns the sentence that is associated with this global sentence id.
    *
    * @return a Sentence object
    * @throws NoSuchElementException if entityId is invalid
    */
  def getSentenceById(sentenceId: Integer): Sentence
  /** Returns an iterator over all existing entities in the entity database.
    * Entities are represented by Entity model objects.
    *
    * @return an iterator of Entity objects
    */
  def getEntities(): Iterator[Entity]

  /** Returns the Entity that is associated with this global entity id.
    *
    * @return an Entity object
    * @throws NoSuchElementException if entityId is invalid
    */
  def getEntityById(entityId: Integer): Entity

  /** Returns an iterator over all terms in the data corpus.
    * Terms are represented by Term model objects.
    *
    * @return an iterator of Term objects
    */
  // def getTerms(): Iterator[Term]

  /** Returns an iterator over all terms in the data corpus.
    * Terms are represented by Term model objects.
    *
    * @return an iterator of Term objects
    */
  // def getTermById(termId: Integer): Iterator[Term]
}
