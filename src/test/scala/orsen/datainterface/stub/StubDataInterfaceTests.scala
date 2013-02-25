package test.orsen.datainterface.stub

import orsen.datainterface.stub.StubDataInterface
import orsen.models._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class StubDataInterfaceTests extends FunSuite with BeforeAndAfter {

  /*************
   * Sentences *
   *************/

  test("getSentences returns an iterator of Term objects") {
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

  test("getEntities returns an iterator of Term objects") {
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


  /*********
   * Terms *
   *********/

  test("getTerms returns an iterator of Term objects") {
    var it = StubDataInterface.getTerms
    while (it.hasNext) {
      assert(it.next.isInstanceOf[Term])
    }
  }

  test("getTermsOfSentence throws NoSuchElementException with invalid id") {
    intercept[NoSuchElementException] {
       StubDataInterface.getTermsOfSentence(-1)
    }
    intercept[NoSuchElementException] {
       StubDataInterface.getTermsOfSentence(208)
    }
  }

  test("getTermById throws NoSuchElementException with invalid id") {
    intercept[NoSuchElementException] {
       StubDataInterface.getTermById(-1)
    }
  }

  test("getTermById retrieving lowest id works") {
    var term = StubDataInterface.getTermById(0)
    assert(term.id === 0)
    assert(term.name === "LUANDA")
    assert(term.sentenceId === 4787)
  }

  test("getTermById retrieving highest id works") {
    var term = StubDataInterface.getTermById(207)
    assert(term.id === 207)
    assert(term.name === "¡")
    assert(term.sentenceId === 4796)
  }

}
