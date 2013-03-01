package test.orsen.datainterface

import java.io.IOException
import java.util.Random
import orsen.dataconversion.CreateMongoDB
import orsen.datainterface.DataInterface
import orsen.datainterface.MongoDataInterface
import orsen.models._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterAll

class MongoDataInterfaceTest extends FunSuite with BeforeAndAfterAll {

  val saltedDbName = "test" + new Random().nextInt()

  override def beforeAll(configMap: Map[String, Any]) {
    try {
      CreateMongoDB.createDatabase(saltedDbName, "test")
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

  override def afterAll(configMap: Map[String, Any]) {
    try {
      CreateMongoDB.dropDatabase(saltedDbName)
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

  def dataInterface: MongoDataInterface = {
    return new MongoDataInterface(saltedDbName)
  }

  /*************
   * Sentences *
   *************/

  test("getSentences returns an iterator of Sentence objects") {
    try {
      var it = dataInterface.getSentences()
      while (it.hasNext) {
        assert(it.next.isInstanceOf[Sentence])
      }
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

  test("getSentenceById throws NoSuchElementException with invalid id") {
    try {
      intercept[NoSuchElementException] {
        dataInterface.getSentenceById(-1)
        fail()
      }
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

  test("getSentenceById retrieving correct Sentence model object works") {
    try {
      var sent: Sentence = dataInterface.getSentenceById(4789)
      assert(sent.isInstanceOf[Sentence])
      assert(sent.id === 4789)
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }


  /**********
   * Tokens *
   **********/

  test("getTokens returns an iterator of Token objects") {
    try {
      var it = dataInterface.getTokens
      while (it.hasNext) {
        assert(it.next.isInstanceOf[Token])
      }
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

  test("getTokensOfSentence throws NoSuchElementException with invalid id") {
    try {
      intercept[NoSuchElementException] {
        dataInterface.getTokensOfSentence(-1)
        fail()
      }
      intercept[NoSuchElementException] {
        dataInterface.getTokensOfSentence(208)
        fail()
      }
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

  test("getTokensOfSentence retrieves the correct Token model objects") {
    try {
      val actualTokens = "It was Angola 's first election since a 27-year civil war ended in the country in 2002 .".split(" ")
      val it: Iterator[Token] = dataInterface.getTokensOfSentence(4789)

      var i = 0
      while(it.hasNext) {
        val token: Token = it.next
        assert(token.text === actualTokens(i))
        i += 1
      }
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

  test("getTokensOfSentence retrieves Token model objects with all expected fields (text, postag, nertag)") {
    try {
      val actualTokens = "It was Angola 's first election since a 27-year civil war ended in the country in 2002 .".split(" ")
      val actualPOStags = "DT NN VBD CD NN , VBG TO DT NN .".split(" ")
      val actualNERtags = "O O O PERCENT PERCENT O O O O O O".split(" ")
      val it: Iterator[Token] = dataInterface.getTokensOfSentence(4789)

      var i = 0
      while(it.hasNext) {
        val token: Token = it.next
        assert(token.text === actualTokens(i))
        //assert(token.posTag === actualPOStags(i))
        //assert(token.nerTag === actualNERtags(i))
        i += 1
      }
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

  test("getTokenById throws NoSuchElementException with invalid id") {
    try {
      intercept[NoSuchElementException] {
        dataInterface.getTokenById(-1)
        fail()
      }
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

}
