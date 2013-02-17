package orsen.dataconversion

import scala.io.Source

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient


object CreateMongoDB {


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
      collection.findOne(query) match {
        case Some(dbRecord) => {
          // if there is an object for this sentence id, update it in the collection
          println("old record" + dbRecord)
          parseFunc(line, dbRecord)
          println("new record" + dbRecord)
          collection.update(query, dbRecord)
        }
        case None => {
          // if there is no object for this sentence id, add a new object to the collection
          val dbRecord = MongoDBObject("_id" -> id)

          println("old record" + dbRecord)
          parseFunc(line, dbRecord)
          println("new record" + dbRecord)
          collection += dbRecord
        }
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
    val tokens = parse(1)
    sentenceRecord += "tokens" -> tokens
  }



  def main(arguments: Array[String]) {
    val dataPath: String = "data/"

    val mongoDB: MongoDB = MongoClient()("orsen")

    val sentencesColl: MongoCollection = mongoDB("sentences")

    extractData(dataPath + "sentences.text", parseText, sentencesColl)
  }

}
