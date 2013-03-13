package orsen.datainterface

import orsen.models._

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient


object MongoDataInterface extends DataInterface {

  var mongoDBName: String = "orsen"
  var mongoDB: MongoDB = MongoClient()(mongoDBName)

  /** Resets the MongoDataInterface the new _mongoDBName
    */
  def resetDataInterface(_mongoDBName: String = "orsen") {
    mongoDBName = _mongoDBName
    mongoDB = MongoClient()(mongoDBName)
  }

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
    return new DBIterator[Entity](mongoDB("entities").find(), deserializeEntity)
  }

  /** Returns the Entity that is associated with this entityId.
    *
    * @return an Entity object
    * @throws NoSuchElementException if entityId does not match any Entity
    */
  def getEntityById(entityId: Int): Entity = {
    val dbRecord: MongoDBObject = fetchOne(MongoDBObject("entityId" -> entityId), "entities")
    return deserializeEntity(dbRecord)
  }

  /** Returns the Entity that is associated with this entity name.
    *
    * @return an Entity object
    * @throws NoSuchElementException if the name does not match any Entity
    */
  def getEntityByText(entityText: String): Entity = {
    val dbRecord: MongoDBObject = fetchOne(MongoDBObject("entity" -> entityText), "entities")
    return deserializeEntity(dbRecord)
  }

  /** Returns all Entities who name contain this subtoken.
    *
    * @return an iterator of Entity object
    */
  def getEntitiesBySubtoken(subtoken: String): Iterator[Entity] = {
    return new DBIterator[Entity](mongoDB("entities").find(MongoDBObject("subtokens" -> subtoken)), deserializeEntity)
  }

  /** Returns an iterator over all tokens in the data corpus.
    * Tokens are represented by Token model objects.
    *
    * @return an iterator of Token objects
    * @throws NoSuchElementException if no matches found
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

  /** Returns an iterator over all CrosswikiEntrys that match this mention text
    * CrosswikiEntrys are represented by CrosswikiEntry model objects.
    *
    * @return an iterator of CrosswikiEntry objects
    * @throws NoSuchElementException if no matches found
    */
  def getCrosswikiEntrysByMention(mention: String): Iterator[CrosswikiEntry] = {
    // add a leading space because of stupid input data
    val query: MongoDBObject = MongoDBObject("mentionText" -> (mention))
    val cursor: MongoCursor = mongoDB("dictionary").find(query)
    if (cursor.isEmpty) throw new NoSuchElementException()

    return new DBIterator[CrosswikiEntry](cursor, deserializeCrosswikiEntry)
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
    return new Sentence(id)
  }

  private def deserializeToken(dbRecord: MongoDBObject): Token = {
    val tId = dbRecord.as[Int]("tokenId")
    val sId = dbRecord.as[Int]("sentenceId")
    val text = dbRecord.as[String]("text")
    val posTag = dbRecord.as[String]("postag")
    val nerTag = dbRecord.as[String]("nertag")
    val token = new Token(tId, text, sId).addPOStag(posTag).addNERtag(nerTag)
    return token
  }

  private def deserializeEntity(dbRecord: MongoDBObject): Entity = {
    val id = dbRecord.as[Int]("entityId")
    val entity = dbRecord.as[String]("entity")
    return new Entity(id, entity)
  }

  private def deserializeCrosswikiEntry(dbRecord: MongoDBObject): CrosswikiEntry = {
    val mention = dbRecord.as[String]("mentionText")
    val entity = dbRecord.as[String]("entityText")
    val score = dbRecord.as[String]("score").toDouble
    val entry = new CrosswikiEntry(mention, entity, score)
    return entry
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
