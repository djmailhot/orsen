package orsen.dataconversion

import scala.io.Source

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient


object CreateMongoDB {

  var databasename = "orsen"

  def extractData(fileName: String, parseFunc: ((String, MongoDBObject) => Unit), collection: MongoCollection) {
    println("extracting from file "+fileName)

    val it: Iterator[String] = Source.fromFile(fileName).getLines
    while (it.hasNext) {
      println(collection.toString())
      val line: String = it.next
      println(line)
      val parse = line.split("\t")
      val id: Int = parse(0).toInt

      val query: MongoDBObject = MongoDBObject("sentenceId" -> id)
      var dbRecord: MongoDBObject = collection.findOne(query) match {
        case Some(record) => record
        case None => null
      }

      if (dbRecord == null) {
        // if there is no object for this sentence id, add a new object to the collection
        dbRecord = MongoDBObject("sentenceId" -> id)
        println("old record" + dbRecord)
        parseFunc(line, dbRecord)
        println("new record" + dbRecord)
        collection += dbRecord
      } else {
        // if there is an object for this sentence id, update it in the collection
        println("old record" + dbRecord)
        parseFunc(line, dbRecord)
        println("new record" + dbRecord)
        collection.update(query, dbRecord)
      }
    }
  }


  def parseText(line: String, record: MongoDBObject) {
    val parse = line.split("\t")
    val text = parse(1)
    record += "text" -> text
  }

  def parseTokens(line: String, record: MongoDBObject) {
    val parse = line.split("\t")
    val tokens: Array[String] = parse(1).split(" ")
    record += "tokens" -> tokens
  }

  def parsePOStags(line: String, record: MongoDBObject) {
    val parse = line.split("\t")
    val posTags: Array[String] = parse(1).split(" ")
    record += "postags" -> posTags
  }


  def parseNERtags(line: String, record: MongoDBObject) {
    val parse = line.split("\t")
    val nerTags: Array[String] = parse(1).split(" ")
    record += "nertags" -> nerTags
  }


  def createDatabase(databasename: String = "orsen", databasepath: String = "prod") {
    val dataPath: String = "data/" + databasepath + "/"

    MongoClient().dropDatabase(databasename)
    val mongoDB: MongoDB = MongoClient()(databasename)

    val sentencesColl: MongoCollection = mongoDB("sentences")
    sentencesColl.ensureIndex( MongoDBObject("sentenceId" -> 1), MongoDBObject("unique" -> true))

    extractData(dataPath + "sentences.text", parseText, sentencesColl)

    val tokensColl: MongoCollection = mongoDB("tokens")
    tokensColl.ensureIndex( MongoDBObject("tokenId" -> 1), MongoDBObject("unique" -> true))
    tokensColl.ensureIndex( MongoDBObject("sentenceId" -> 1) )
    extractData(dataPath + "sentences.tokens", parseTokens, tokensColl)
    extractData(dataPath + "sentences.stanfordner", parseNERtags, tokensColl)
    extractData(dataPath + "sentences.stanfordpos", parsePOStags, tokensColl)
  }

}
