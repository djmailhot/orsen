package orsen.datainterface

import orsen.models._

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient


class MongoDataInterface(dbname: String = "orsen") extends DataInterface {

  val mongoDB: MongoDB = MongoClient()(dbname)




  /** Returns an iterator over all sentences in the data corpus.
    * Sentences are represented by Sentence model objects.
    *
    * @return an iterator of Sentence objects
    */
  def getSentences(): Iterator[Sentence] = {
    return new DBIterator[Sentence](mongoDB("sentences").find(), deserializeSentence)
  }

  /** Returns the sentence that is associated with this sentenceId.
    *
    * @return a Sentence object
    * @throws NoSuchElementException if sentenceID does not match any Sentence
    */
  def getSentenceById(sentenceId: Int): Sentence = {
    val dbRecord: MongoDBObject = fetchOne(MongoDBObject("sentenceId" -> sentenceId), "sentences")
    return deserializeSentence(dbRecord)
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
    return new DBIterator[Token](mongoDB("tokens").find(), deserializeToken)
  }


  /** Returns an iterator over all tokens in the given sentence
    * Tokens are represented by Token model objects.
    *
    * @return an iterator of Token objects
    * @throws NoSuchElementException if sentenceId does not match any Sentence
    */
  def getTokensOfSentence(sentenceId: Int): Iterator[Token] = {
    val query: MongoDBObject = MongoDBObject("sentenceId" -> sentenceId)
    val cursor: MongoCursor = mongoDB("tokens").find(query)
    if (cursor.isEmpty) throw new NoSuchElementException()

    return new DBIterator[Token](cursor, deserializeToken)
  }


  /** Returns the Token that is associated with this tokenId
    * Tokens are represented by Token model objects.
    *
    * @return an iterator of Token objects
    * @throws NoSuchElementException if tokenId does not match any Token
    */
  def getTokenById(tokenId: Int): Token = {
    val dbRecord: MongoDBObject = fetchOne(MongoDBObject("tokenId" -> tokenId), "tokens")
    return deserializeToken(dbRecord)
  }


  private def fetchOne(query: MongoDBObject, collectionName: String): MongoDBObject = {
    val collection: MongoCollection = mongoDB(collectionName)
    val dbRecord: MongoDBObject = collection.findOne(query) match {
      case Some(record) => record
      case None => throw new NoSuchElementException()
    }
    return dbRecord
  }


  private def deserializeSentence(dbRecord: MongoDBObject): Sentence = {
    val id = dbRecord.as[Int]("sentenceId")
    return new Sentence(id, null)
  }

  private def deserializeToken(dbRecord: MongoDBObject): Token = {
    val tId = dbRecord.as[Int]("tokenId")
    val sId = dbRecord.as[Int]("sentenceId")
    val text = dbRecord.as[String]("text")
    val posTag = dbRecord.as[String]("postag")
    val nerTag = dbRecord.as[String]("nertag")
    return new Token(tId, text, sId)
  }


  private class DBIterator[R](dbCursor: MongoCursor, deserializeFunc: (MongoDBObject) => R) extends Iterator[R] {
    def hasNext(): Boolean = {
      return dbCursor.hasNext
    }
    def next(): R = {
      return deserializeFunc(dbCursor.next())
    }
  }
}
