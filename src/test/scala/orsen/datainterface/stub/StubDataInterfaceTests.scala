package test.orsen.datainterface.stub

import orsen.datainterface.stub.StubDataInterface
import orsen.models._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class StubDataInterfaceTests extends FunSuite with BeforeAndAfter {

  /*************
   * Sentences *
   *************/

  test("getSentences returns an iterator of Sentence objects") {
    var it = StubDataInterface.getSentences()
    while (it.hasNext) {
      assert(it.next.isInstanceOf[Sentence])
    }
  }

  test("getSentenceById throws NoSuchElementException with invalid id") {
    intercept[NoSuchElementException] {
      StubDataInterface.getSentenceById(-1)
      fail()
    }
  }

  test("getSentenceById retrieving lowest id works") {
    var sent = StubDataInterface.getSentenceById(4787)
    assert(sent.isInstanceOf[Sentence])
    assert(sent.id === 4787)
  }

  test("getSentenceById retrieving highest id works") {
    var sent = StubDataInterface.getSentenceById(4796)
    assert(sent.isInstanceOf[Sentence])
    assert(sent.id === 4796)
  }

  /************
   * Entities *
   ************/

  test("getEntities returns an iterator of Entity objects") {
    var it = StubDataInterface.getEntities()
    while (it.hasNext) {
      assert(it.next.isInstanceOf[Entity])
    }
  }

  test("getEntityById throws NoSuchElementException with invalid id") {
    intercept[NoSuchElementException] {
      StubDataInterface.getEntityById(0)
      fail()
    }
  }

  test("getEntityById retrieving the lowest id entity works") {
    var ent = StubDataInterface.getEntityById(1)
    assert(ent.isInstanceOf[Entity])
    assert(ent.id === 1)
    assert(ent.name === "Luanda")
  }

  test("getEntityById retrieving the highest id entity works") {
    var ent = StubDataInterface.getEntityById(3)
    assert(ent.isInstanceOf[Entity])
    assert(ent.id === 3)
    assert(ent.name === "Popular Movement for the Liberation of Angola")
  }


  /**********
   * Tokens *
   **********/

  test("getTokens returns an iterator of Token objects") {
    var it = StubDataInterface.getTokens
    while (it.hasNext) {
      assert(it.next.isInstanceOf[Token])
    }
  }

  test("getTokensOfSentence throws NoSuchElementException with invalid id") {
    intercept[NoSuchElementException] {
       StubDataInterface.getTokensOfSentence(-1)
    }
    intercept[NoSuchElementException] {
       StubDataInterface.getTokensOfSentence(208)
    }
  }

  test("getTokenById throws NoSuchElementException with invalid id") {
    intercept[NoSuchElementException] {
       StubDataInterface.getTokenById(-1)
    }
  }

  test("getTokenById retrieving lowest id works") {
    var token = StubDataInterface.getTokenById(0)
    assert(token.id === 0)
    assert(token.text === "LUANDA")
    assert(token.sentenceId === 4787)
  }

  test("getTokenById retrieving highest id works") {
    var token = StubDataInterface.getTokenById(207)
    assert(token.id === 207)
    assert(token.text === "¡")
    assert(token.sentenceId === 4796)
  }

}
