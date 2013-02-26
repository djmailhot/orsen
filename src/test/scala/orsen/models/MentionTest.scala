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

}
