package orsen.datainterface

import orsen.models._

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient


class MongoDataInterface(val dbname: String = "orsen") extends DataInterface {

  val mongoDB: MongoDB = MongoClient()(dbname)

  val sentencesColl: MongoCollection = mongoDB("sentences")



  /** Returns an iterator over all sentences in the data corpus.
    * Sentences are represented by Sentence model objects.
    *
    * @return an iterator of Sentence objects
    */
  def getSentences(): Iterator[Sentence] = {
    return new SentenceIterator(sentencesColl.find())
  }

  /** Returns the sentence that is associated with this sentenceId.
    *
    * @return a Sentence object
    * @throws NoSuchElementException if sentenceID does not match any Sentence
    */
  def getSentenceById(sentenceId: Int): Sentence = {
    val dbRecord: MongoDBObject = fetchById(sentenceId)
    return deserializeSentence(dbRecord)
  }

  /** Returns the text tokens that are associated with this sentenceId.
    *
    * @return a list of Token objects
    * @throws NoSuchElementException if sentenceID does not match any Sentence
    */
  def getTokensBySentenceId(sentenceId: Int): Array[Token] = {
    val dbRecord: MongoDBObject = fetchById(sentenceId)
    return dbRecord.as[Array[Token]]("tokens")
  }

  /** Returns the Part Of Speech tag for the text tokens of this sentenceId.
    *
    * @return a list of Strings that are Part Of Speech tags
    * @throws NoSuchElementException if sentenceID does not match any Sentence
    */
  def getPOStagsById(sentenceId: Int): Array[String] = {
    val dbRecord: MongoDBObject = fetchById(sentenceId)
    return dbRecord.as[Array[String]]("postags")
  }

  /** Returns an iterator over all existing entities in the entity database.
    * Entities are represented by Entity model objects.
    *
    * @return an iterator of Entity objects
    */
  def getEntities(): Iterator[Entity] = {
    return null
  }

  /** Returns the Entity that is associated with this entityId.
    *
    * @return an Entity object
    * @throws NoSuchElementException if entityId does not match any Entity
    */
  def getEntityById(entityId: Int): Entity = {
    return null
  }


  /** Returns an iterator over all tokens in the data corpus.
    * Tokens are represented by Token model objects.
    *
    * @return an iterator of Token objects
    */
  def getTokens(): Iterator[Token] = {
    return null
  }


  /** Returns an iterator over all tokens in the given sentence
    * Tokens are represented by Token model objects.
    *
    * @return an iterator of Token objects
    * @throws NoSuchElementException if sentenceId does not match any Sentence
    */
  def getTokensOfSentence(sentenceId: Int): Iterator[Token] = {
    return null
  }


  /** Returns the Token that is associated with this tokenId
    * Tokens are represented by Token model objects.
    *
    * @return an iterator of Token objects
    * @throws NoSuchElementException if tokenId does not match any Token
    */
  def getTokenById(tokenId: Int): Token = {
    return null
  }




  private def fetchById(sentenceId: Int): MongoDBObject = {
    val query = MongoDBObject("_id" -> sentenceId)
    val dbRecord: MongoDBObject = sentencesColl.findOne(query) match {
      case Some(record) => record
      case None => throw new NoSuchElementException()
    }
    return dbRecord
  }


  private def deserializeSentence(dbRecord: MongoDBObject): Sentence = {
    val id = dbRecord.as[Int]("_id")
    return new Sentence(id, null)
  }


  private class SentenceIterator(dbCursor: MongoCursor) extends Iterator[Sentence] {
    def hasNext(): Boolean = {
      return dbCursor.hasNext
    }
    def next(): Sentence = {
      return deserializeSentence(dbCursor.next())
    }
  }
}
