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
      val id: Integer = parse(0).toInt

      val query: MongoDBObject = MongoDBObject("_id" -> id)
      var dbRecord: MongoDBObject = collection.findOne(query) match {
        case Some(record) => record
        case None => null
      }

      if (dbRecord == null) {
        // if there is no object for this sentence id, add a new object to the collection
        dbRecord = MongoDBObject("_id" -> id)
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


  def parseText(line: String, sentenceRecord: MongoDBObject) {
    val parse = line.split("\t")
    val text = parse(1)
    sentenceRecord += "text" -> text
  }

  def parseTokens(line: String, sentenceRecord: MongoDBObject) {
    val parse = line.split("\t")
    val tokens: Array[String] = parse(1).split(" ")
    sentenceRecord += "tokens" -> tokens
  }

  def parsePOStags(line: String, sentenceRecord: MongoDBObject) {
    val parse = line.split("\t")
    val posTags: Array[String] = parse(1).split(" ")
    sentenceRecord += "postags" -> posTags
  }


  def parseNERtags(line: String, sentenceRecord: MongoDBObject) {
    val parse = line.split("\t")
    val nerTags: Array[String] = parse(1).split(" ")
    sentenceRecord += "nertags" -> nerTags
  }


  def createDatabase(databasename: String = "orsen", databasepath: String = "prod") {
    val dataPath: String = "data/" + databasepath + "/"

    val mongoDB: MongoDB = MongoClient()(databasename)

    val sentencesColl: MongoCollection = mongoDB("sentences")
    sentencesColl.ensureIndex("_id")

    extractData(dataPath + "sentences.text", parseText, sentencesColl)
    extractData(dataPath + "sentences.tokens", parseTokens, sentencesColl)
    extractData(dataPath + "sentences.stanfordpos", parsePOStags, sentencesColl)
    extractData(dataPath + "sentences.stanfordner", parseNERtags, sentencesColl)
  }

}
