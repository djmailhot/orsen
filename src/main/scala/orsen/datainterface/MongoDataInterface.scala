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
  def getSentenceById(sentenceId: Integer): Sentence = {
    val dbRecord: MongoDBObject = fetchById(sentenceId)
    return deserializeSentence(dbRecord)
  }

  /** Returns the text tokens that are associated with this sentenceId.
    *
    * @return a list of Token objects
    * @throws NoSuchElementException if sentenceID does not match any Sentence
    */
  def getTokensById(sentenceId: Integer): Array[String] = {
    val dbRecord: MongoDBObject = fetchById(sentenceId)
    return dbRecord.as[Array[String]]("tokens")
  }

  /** Returns the Part Of Speech tag for the text tokens of this sentenceId.
    *
    * @return a list of Strings that are Part Of Speech tags
    * @throws NoSuchElementException if sentenceID does not match any Sentence
    */
  def getPOStagsById(sentenceId: Integer): Array[String] = {
    val dbRecord: MongoDBObject = fetchById(sentenceId)
    return dbRecord.as[Array[String]]("postags")
  }


  private def fetchById(sentenceId: Integer): MongoDBObject = {
    val query = MongoDBObject("_id" -> sentenceId)
    val dbRecord: MongoDBObject = sentencesColl.findOne(query) match {
      case Some(record) => record
      case None => throw new NoSuchElementException()
    }
    return dbRecord
  }


  private def deserializeSentence(dbRecord: MongoDBObject): Sentence = {
    val id = dbRecord.as[Integer]("_id")
    return new Sentence(id)
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
