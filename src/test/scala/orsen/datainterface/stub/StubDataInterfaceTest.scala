package test.orsen.datainterface.stub

import orsen.datainterface.stub.StubDataInterface
import orsen.models._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class StubDatInterfaceTest extends FunSuite with BeforeAndAfter {

  /*************
   * Sentences *
   *************/

  test("The sentences iterator returns Sentence objects") {
    var it = StubDataInterface.getSentences()
    while (it.hasNext) {
      assert(it.next.isInstanceOf[Sentence])
    }
  }

  test("Retrieving a sentence by invalid ID throws NoSuchElementException") {
    intercept[NoSuchElementException] {
      StubDataInterface.getSentenceById(-1)
      fail()
    }
  }

  test("Retrieve the lowest id sentence works") {
    var sent = StubDataInterface.getSentenceById(4787)
    assert(sent.isInstanceOf[Sentence])
    assert(sent.id == 4787)
  }

  test("Retrieve the highest id sentence works") {
    var sent = StubDataInterface.getSentenceById(4796)
    assert(sent.isInstanceOf[Sentence])
    assert(sent.id == 4796)
  }

  /************
   * Entities *
   ************/

   test("The entities iterator returns Entity objects") {
      var it = StubDataInterface.getEntities()
      while (it.hasNext) {
        assert(it.next.isInstanceOf[Entity])
      }
   }

  test("Retrieving a entity by invalid ID throws NoSuchElementException") {
    intercept[NoSuchElementException] {
      StubDataInterface.getEntityById(0)
      fail()
    }
  }

  test("Retrieving the lowest id entity works") {
    var ent = StubDataInterface.getEntityById(1)
    assert(ent.isInstanceOf[Entity])
    assert(ent.id == 1)
    assert(ent.name == "Luanda")
  }

  test("Retrieving the highest id entity works") {
    var ent = StubDataInterface.getEntityById(3)
    assert(ent.isInstanceOf[Entity])
    assert(ent.id == 3)
    assert(ent.name == "Popular Movement for the Liberation of Angola")
  }


  /*********
   * Terms *
   *********/

   test("The terms iterator returns Term objects") {
      var it = StubDataInterface.getTerms
      while (it.hasNext) {
        assert(it.next.isInstanceOf[Term])
      }
   }

}
