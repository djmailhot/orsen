package test.orsen.models

import orsen.models._
import org.scalatest.FunSuite

class EntityTest extends FunSuite {
  test("The primary constructor should be sane") {
    var entity = new Entity(39, "banana", "turtle")
    assert(entity.id === 39)
    assert(entity.name === "banana")
    assert(entity.description === "turtle")
  }

  test("The description lacking constructor should be sane") {
    var entity = new Entity(39, "banana")
    assert(entity.id === 39)
    assert(entity.name === "banana")
    assert(entity.description === "")
  }

}
