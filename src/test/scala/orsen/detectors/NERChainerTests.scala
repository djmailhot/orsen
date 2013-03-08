package test.orsen.detectors

import orsen.detectors._
import orsen.models._
import orsen.output._
import orsen.datainterface._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalamock.scalatest.MockFactory
import org.scalamock.ProxyMockFactory

import scala.collection.mutable

import java.io.File

class NERChainerTests extends FunSuite with BeforeAndAfter
                                       with MockFactory
                                       with ProxyMockFactory {

  test("extractMentions should work in the common case") {
    var tokens = Array(new Token(1, "a", 1, Token.UNKNOWN_TOKEN, "1"),
                       new Token(2, "b", 1, Token.UNKNOWN_TOKEN, "1"),
                       new Token(3, "c", 1, Token.UNKNOWN_TOKEN, "1"),
                       new Token(4, "d", 1, Token.UNKNOWN_TOKEN, "2"),
                       new Token(5, "e", 1, Token.UNKNOWN_TOKEN, "3"),
                       new Token(6, "f", 1, Token.UNKNOWN_TOKEN, "3"))

    var mentions = NERChainer.extractMentions(tokens.iterator)
    var expected = mutable.ArrayBuffer(new Mention(1, "a b c", Array(1, 2, 3)),
                         new Mention(4, "d",     Array(4)),
                         new Mention(5, "e f",   Array(5, 6)))
    assert(expected === mentions)
  }
}
