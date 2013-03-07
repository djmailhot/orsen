package orsen.dataconversion

import scala.io.Source

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient


object CreateCrosswikiDB {

  def extractData(fileName: String, parseFunc: ((String, MongoCollection) => Unit), collection: MongoCollection) {
    printf("extracting from file %s\n", fileName)

    var line = ""
    var count = 0
    val it: Iterator[String] = Source.fromFile(fileName).getLines
    while (it.hasNext) {
      try {
        count += 1
        line = it.next
        parseFunc(line, collection)

        if (count % 5000 == 0) {
          printf(".")
        }
      } catch {
        case e: Exception => printf("exception after %d lines, on line %s\n%s\n", count, line, e.getStackTraceString)
      }
    }
    printf("complete\n")
  }


  def extractDictionary(line: String, collection: MongoCollection) {
    val parse = line.split("\t")
    val mention = parse(0)//.strip()
    val scoreParse = parse(1).split(" ")
    val score = scoreParse(0)//.strip()
    val entity = scoreParse(1)//.strip()

    val record = MongoDBObject("mention" -> mention, "score" -> score, "entity" -> entity)
    collection += record
  }


  def createDatabase(databasename: String = "crosswiki", databasepath: String = "data/prod/") {
    val dataPath: String = databasepath match {
      case "prod" => "data/prod/"
      case "test" => "data/test/"
      case _ => databasepath
    }

    val mongoDB: MongoDB = MongoClient()(databasename)

    val dictionaryColl = mongoDB("dictionary")
    extractData(dataPath + "dictionary.txt", extractDictionary, dictionaryColl)
    dictionaryColl.ensureIndex( MongoDBObject("mention" -> 1) )
  }

  def dropDatabase(databasename: String = "crosswiki") {
    MongoClient().dropDatabase(databasename)
  }

}
