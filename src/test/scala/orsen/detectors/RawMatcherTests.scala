package test.orsen.detectors

import orsen.detectors._
import orsen.models._
import orsen.output._
import orsen.datainterface._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalamock.scalatest.MockFactory
import org.scalamock.ProxyMockFactory

class RawMatcherTests extends FunSuite with BeforeAndAfter
                                       with MockFactory
                                       with ProxyMockFactory {

  val mockDataInterface = mock[DataInterface]
  val mockOutputWriter  = mock[OutputWriter]

  val entities = Array(new Entity(1, "a"),
                       new Entity(2, "a"),
                       new Entity(3, "a"),
                       new Entity(4, "b"),
                       new Entity(5, "b"))

  before {
    RawMatcher.setDataInterface(mockDataInterface)
    RawMatcher.setOutputWriter(mockOutputWriter)
  }

  test("writeEntities can write out a list of entities") {
    fail("Test not yet written")
  }

  test("buildEntityTable can build the common case") {
    var entityTable = Map("a"->entities.slice(0, 3),
                          "b"->entities.slice(3, 5))
    var mention = new Mention(1, "a", Array(1))
    var candidateResults = RawMatcher.buildCandidates(entityTable, mention)
    var expected = Map(entities(0) -> 1.0 / 3,
                       entities(1) -> 1.0 / 3,
                       entities(2) -> 1.0 / 3)
    assert(candidateResults === expected)
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
