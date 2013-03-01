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

class RawMatcherTests extends FunSuite with BeforeAndAfter
                                       with MockFactory
                                       with ProxyMockFactory {

  val mockDataInterface = mock[DataInterface]
  val mockOutputWriter  = mock[OutputWriter]

  val entities = Array(new Entity(1, "a"),
                       new Entity(2, "a"),
                       new Entity(3, "a"),
                       new Entity(4, "b b"),
                       new Entity(5, "b"))

  val mentions = Array(new Mention(1, "a", Array(1)),
                       new Mention(2, "b b", Array(2, 3)),
                       new Mention(3, "c", Array(4)))

  val tokens = Array(new Token(1, "a", 1),
                     new Token(2, "b", 1),
                     new Token(3, "b", 1),
                     new Token(4, "c", 2))

  val sentences = Array(new Sentence(1, Array(1, 2, 3)),
                        new Sentence(2, Array(4)))

  before {
    RawMatcher.setDataInterface(mockDataInterface)
    RawMatcher.setOutputWriter(mockOutputWriter)
  }

  test("The RawMatcher works end to end") {
    // mockDataInterface.expects.getEntities returning entities.iterator
    fail("Test not yet written")
  }

  test("writeEntities can write out a list of entities") {
    val entityTable = RawMatcher.buildEntityTable(entities.iterator)
    val expected = mutable.Map("a"->(mutable.ArrayBuffer() ++ entities.slice(0, 3)),
                               "b b"->(mutable.ArrayBuffer() ++ entities.slice(3, 4)),
                               "b"->(mutable.ArrayBuffer() ++ entities.slice(4, 5)))
    assert(expected === entityTable)
  }

  test("buildEntityTable can build the common case") {
    var entityTable = Map("a"->entities.slice(0, 3),
                          "b"->entities.slice(3, 5))
    var mention = mentions(0)
    var candidateResults = RawMatcher.buildCandidates(entityTable, mention)
    var expected = Map(entities(0) -> 1.0 / 3,
                       entities(1) -> 1.0 / 3,
                       entities(2) -> 1.0 / 3)
    assert(expected === candidateResults)
  }

  test("buildEntityTable throws NoSuchElementException in the nonexisting case") {
    intercept[NoSuchElementException] {
      var entityTable = Map("a"->entities.slice(0, 3),
                            "b"->entities.slice(4, 6))
      var mention = new Mention(1, "aasdasd", Array(1))
      RawMatcher.buildCandidates(entityTable, mention)
    }
  }
}
