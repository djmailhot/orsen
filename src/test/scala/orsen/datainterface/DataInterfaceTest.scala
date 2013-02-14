package test.orsen.datainterface

import org.scalatest.FunSuite

import orsen.datainterface.DataInterface
import orsen.models.Sentence

class DataInterfaceTest extends FunSuite {
  test("the sentences iterator returns Sentence objects") {
    var dataInterface = new DataInterface()
    var it = dataInterface.getSentences()
    while (it.hasNext) {
      assert(it.next.isInstanceOf[Sentence])
    }
  }

}


