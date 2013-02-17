package orsen.datainterface

import orsen.models.Sentence
import orsen.models.Entity

trait DataInterface {
  /** Returns an iterator over all sentences in the data corpus.
    * Sentences are represented by Sentence model objects.
    *
    * @returns an iterator of Sentence objects
    */
  def getSentences(): Iterator[Sentence]

  /** Returns the sentence that is associated with this global sentence id.
    *
    * @returns a Sentence object
    * @throws NoSuchElementException if entityId is invalid
    */
  def getSentenceById(sentenceId: Integer): Sentence
  /** Returns an iterator over all existing entities in the entity database.
    * Entities are represented by Entity model objects.
    *
    * @returns an iterator of Entity objects
    */
  def getEntities(): Iterator[Entity]

  /** Returns the Entity that is associated with this global entity id.
    *
    * @returns an Entity object
    * @throws NoSuchElementException if entityId is invalid
    */
  def getEntityById(entityId: Integer): Entity

}
