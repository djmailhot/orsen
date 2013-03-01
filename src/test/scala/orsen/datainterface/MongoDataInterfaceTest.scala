package test.orsen.datainterface

import java.io.IOException
import orsen.dataconversion.CreateMongoDB
import orsen.datainterface.DataInterface
import orsen.datainterface.MongoDataInterface
import orsen.models._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MongoDataInterfaceTest extends FunSuite with BeforeAndAfter {

  CreateMongoDB.createDatabase("test", "test")

  val dataInterface = new MongoDataInterface("test")


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

  //  4787	LUANDA, Sept. 17 (Xinhua)
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
      val actualTokenStrings = "4787	LUANDA, Sept. 17 (Xinhua)".split("\t")
      val it: Iterator[Token] = dataInterface.getTokensOfSentence(4789)

      var i = 0
      while(it.hasNext) {
        val token: Token = it.next
        assert(token.text === actualTokenStrings(i))
        i += 1
      }
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

}
