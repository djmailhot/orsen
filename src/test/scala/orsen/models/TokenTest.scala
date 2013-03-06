package test.orsen.models

import orsen.models._
import org.scalatest.FunSuite

class TokenTest extends FunSuite {
  test("Equals and Hashcode should report equality for different Token of same ID") {
    var token_one = new Token(39, "asdasdf", 5)
    var token_two = new Token(39, "asdasdf", 5)
    assert(token_one.hashCode() === token_two.hashCode())
    assert(token_one === token_two)
  }
}
