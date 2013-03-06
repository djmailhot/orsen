package test.orsen.models

import orsen.models._
import org.scalatest.FunSuite

class MentionTest extends FunSuite {
  test("The primary constructor should be sane") {
    var mention = new Mention(39, "banana", Array(1, 2, 3))
    assert(mention.id === 39)
    assert(mention.text === "banana")
    assert(mention.tokenIds === Array(1, 2, 3))
  }

  test("The Array[Token] constructor should be sane") {
    var mention = new Mention(39, "banana", Array((new Token(1, "", 1)),
                                                  (new Token(2, "", 1)),
                                                  (new Token(3, "", 1))))
    assert(mention.id === 39)
    assert(mention.text === "banana")
    assert(mention.tokenIds === Array(1, 2, 3))
  }

  test("The Token constructor should be sane") {
    var mention = new Mention(39, "banana", new Token(39, "", 1))
    assert(mention.id === 39)
    assert(mention.text === "banana")
    assert(mention.tokenIds === Array(39))
  }

  test("Equals and Hashcode should report equality for different Mentions of same ID") {
    var mention_one = new Mention(39, "banana", 1)
    var mention_two = new Mention(39, "banana", 1)
    assert(mention_one.hashCode() === mention_two.hashCode())
    assert(mention_one === mention_two)
  }

}
