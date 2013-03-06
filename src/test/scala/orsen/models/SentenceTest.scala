package test.orsen.models

import orsen.models._
import org.scalatest.FunSuite

class SentenceTest extends FunSuite {
  test("Equals and Hashcode should report equality for different Sentences of same ID") {
    var sentence_one = new Sentence(39, Array(5))
    var sentence_two = new Sentence(39, Array(5))
    assert(sentence_one.hashCode() === sentence_two.hashCode())
    assert(sentence_one === sentence_two)
  }

}
