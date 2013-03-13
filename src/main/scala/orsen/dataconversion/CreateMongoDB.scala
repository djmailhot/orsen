package orsen.dataconversion

import scala.io.Source

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient


object CreateMongoDB {

  def extractData(fileName: String, parseFunc: ((String, MongoCollection, Int) => Int), collection: MongoCollection, startId: Int = 0): Int = {
    printf("extracting from file %s\n", fileName)

    var line = ""
    var count = 0
    var currId = startId
    val it: Iterator[String] = Source.fromFile(fileName).getLines
    while (it.hasNext) {
      try {
        count += 1
        line = it.next
        currId = parseFunc(line, collection, currId)

        if (count % 5000 == 0) {
          printf(".")
        }
      } catch {
        case e: Exception => printf("exception after %d lines, on line %s\n%s\n", count, line, e.getStackTraceString)
      }
    }
    printf("complete\n")
    return currId
  }

  def extractCrosswiki(fileName: String, parseFunc: ((String, MongoCollection, MongoCollection, Int) => Int), collection1: MongoCollection, collection2: MongoCollection, startId: Int = 0): Int = {
    printf("extracting from file %s\n", fileName)

    var line = ""
    var count = 0
    var currId = startId
    val it: Iterator[String] = Source.fromFile(fileName).getLines
    while (it.hasNext) {
      try {
        count += 1
        line = it.next
        currId = parseFunc(line, collection1, collection2, currId)

        if (count % 5000 == 0) {
          printf(".")
        }
      } catch {
        case e: Exception => printf("exception after %d lines, on line %s\n%s\n", count, line, e.getStackTraceString)
      }
    }
    printf("complete\n")
    return currId
  }

  def parseText(line: String, collection: MongoCollection, lastId: Int): Int = {
    val parse = line.split("\t")
    val id: Int = parse(0).toInt
    val text = parse(1)


    val query: MongoDBObject = MongoDBObject("sentenceId" -> id)
    var dbRecord: MongoDBObject = collection.findOne(query) match {
      case Some(record) => record
      case None => null
    }

    if (dbRecord == null) {
      // if there is no object for this sentence id, add a new object to the collection
      dbRecord = MongoDBObject("sentenceId" -> id)
      dbRecord += "text" -> text
      collection += dbRecord
    } else {
      // if there is an object for this sentence id, update it in the collection
      dbRecord += "text" -> text
      collection.update(query, dbRecord)
    }
    return id
  }

  
  def parseTokenizedData(line: String, collection: MongoCollection, lastId: Int, fieldName: String): Int = {
    var tokenId = lastId
    val parse = line.split("\t")
    val id: Int = parse(0).toInt
    val tokens: Array[String] = parse(1).split(" ")

    for( index <- 0 to (tokens.length - 1) ) {
      val token = tokens(index)
      
      val query: MongoDBObject = MongoDBObject("sentenceId" -> id, "index" -> index)
      var dbRecord: MongoDBObject = collection.findOne(query) match {
        case Some(record) => record
        case None => null
      }

      if (dbRecord == null) {
        // if there is no object for this sentence id, add a new object to the collection
        tokenId += 1
        dbRecord = MongoDBObject("sentenceId" -> id, "index" -> index, "tokenId" -> tokenId)
        dbRecord += fieldName -> token
        collection += dbRecord
      } else {
        // if there is an object for this sentence id, update it in the collection
        dbRecord += fieldName -> token
        collection.update(query, dbRecord)
      }
    }
    return tokenId
  }

  def parseDictionary(line: String, dictionaryColl: MongoCollection, entityColl: MongoCollection, lastId: Int): Int = {
    var entityId = lastId
    val parse = line.split("\t")
    val mention = parse(0).trim()
    val scoreParse = parse(1).split(" ")
    val score = scoreParse(0).trim()
    val entity = scoreParse(1).trim()

    val query: MongoDBObject = MongoDBObject("entity" -> entity)
    var dbRecord: MongoDBObject = entityColl.findOne(query) match {
      case Some(record) => record
      case None => null
    }

    if (dbRecord == null) {
      // if there is no object for this entity, add a new object to the collection
      entityId += 1
      dbRecord = MongoDBObject("entityId" -> entityId, "entity" -> entity)
      entityColl += dbRecord
    }
    // if there is an object for this entity, do nothing

    val dictRecord = MongoDBObject("mentionText" -> mention, "score" -> score, "entityText" -> entity)
    dictionaryColl += dictRecord

    return entityId
  }


  def parseTokens(line: String, collection: MongoCollection, lastId: Int): Int = {
    parseTokenizedData(line, collection, lastId, "text")
  }

  def parsePOStags(line: String, collection: MongoCollection, lastId: Int): Int = {
    parseTokenizedData(line, collection, lastId, "postag")
  }

  def parseNERtags(line: String, collection: MongoCollection, lastId: Int): Int = {
    parseTokenizedData(line, collection, lastId, "nertag")
  }


  def getIdCount(counters: MongoCollection, counterName: String): Int = {
    val query: MongoDBObject = MongoDBObject("_id" -> counterName)
    var dbRecord: MongoDBObject = counters.findOne(query) match {
      case Some(record) => record
      case None => null
    }

    return (if (dbRecord != null) dbRecord.as[Int]("seq") else 0)
  }

  def updateIdCount(counters: MongoCollection, counterName: String, count: Int) {
    val query: MongoDBObject = MongoDBObject("_id" -> counterName)
    var dbRecord: MongoDBObject = counters.findOne(query) match {
      case Some(record) => record
      case None => null
    }

    if (dbRecord == null) {
      dbRecord = MongoDBObject("_id" -> counterName, "seq" -> count)
      counters += dbRecord
    } else {
      dbRecord += "seq" -> count.asInstanceOf[Integer]
      counters.update(query, dbRecord)
    }
  }


  def addMultikeyIndexToEntities(databasename: String = "orsen") {
    val mongoDB: MongoDB = MongoClient()(databasename)

    val entityColl: MongoCollection = mongoDB("entities")
    val cursor: MongoCursor = entityColl.find()

    println("Updating "+cursor.size+" entity records")
    var count = 0
    while (cursor.hasNext) {
      count += 1
      val record: MongoDBObject = cursor.next
      val query: MongoDBObject = record.clone()
      val entity = record.getAs[String]("entity") getOrElse ""
      val subtokens = entity.split("_")
      record += "subtokens" -> subtokens
      entityColl.update(query, record)

      if (count % 5000 == 0) {
        printf(".")
      }
    }
    println("complete")
    entityColl.ensureIndex( MongoDBObject("subtokens" -> 1) )
  }


  def createDatabase(databasename: String = "orsen", databasepath: String = "prod") {
    val dataPath: String = databasepath match {
      case "prod" => "data/prod/"
      case "test" => "data/test/"
      case _ => databasepath
    }

    val mongoDB: MongoDB = MongoClient()(databasename)

    val countersColl: MongoCollection = mongoDB("counters")

    
    var currSentenceId = getIdCount(countersColl, "sentenceId")
    val sentencesColl: MongoCollection = mongoDB("sentences")
    sentencesColl.ensureIndex( MongoDBObject("sentenceId" -> 1), MongoDBObject("unique" -> true))
    currSentenceId = extractData(dataPath + "sentences.text", parseText, sentencesColl, currSentenceId)
    updateIdCount(countersColl, "sentenceId", currSentenceId)

    
    var currTokenId = getIdCount(countersColl, "tokenId")
    val tokensColl: MongoCollection = mongoDB("tokens")
    tokensColl.ensureIndex( MongoDBObject("sentenceId" -> 1, "tokenId" -> 1), MongoDBObject("unique" -> true))
    currTokenId = extractData(dataPath + "sentences.tokens", parseTokens, tokensColl, currTokenId)
    currTokenId = extractData(dataPath + "sentences.stanfordpos", parsePOStags, tokensColl, currTokenId)
    currTokenId = extractData(dataPath + "sentences.stanfordner", parseNERtags, tokensColl, currTokenId)

    updateIdCount(countersColl, "tokenId", currTokenId)
  }


  def createCrosswikiDatabase(databasename: String = "orsen", databasepath: String = "prod") {
    val dataPath: String = databasepath match {
      case "prod" => "data/prod/"
      case "test" => "data/test/"
      case _ => databasepath
    }

    val mongoDB: MongoDB = MongoClient()(databasename)

    val countersColl: MongoCollection = mongoDB("counters")

    // cross wiki data
    var currEntityId = getIdCount(countersColl, "tokenId")
    val dictionaryColl: MongoCollection = mongoDB("dictionary")
    val entityColl: MongoCollection = mongoDB("entities")
    entityColl.ensureIndex( MongoDBObject("entity" -> 1), MongoDBObject("unique" -> true))
    currEntityId = extractCrosswiki(dataPath + "dictionary.txt", parseDictionary, dictionaryColl, entityColl, currEntityId)
    entityColl.ensureIndex( MongoDBObject("entityId" -> 1), MongoDBObject("unique" -> true))
    dictionaryColl.ensureIndex( MongoDBObject("mentionText" -> 1) )

    updateIdCount(countersColl, "entityId", currEntityId)
  }

  def dropDatabase(databasename: String = "orsen") {
    MongoClient().dropDatabase(databasename)
  }

  def main(arguments: Array[String]) {
    createDatabase()
  }
}
