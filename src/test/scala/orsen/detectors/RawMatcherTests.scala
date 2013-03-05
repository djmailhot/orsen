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

class RawMatcherTests extends FunSuite with BeforeAndAfter
                                       with MockFactory
                                       with ProxyMockFactory {

  val entities = Array(new Entity(1, "a", "example_description"),
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


  // TODO: This test badly depends on the existence of UnorganizedOutputWriter
  // This is a decision to avoid the complexity of mocking for pace
  val TestFilePath = "/tmp/orsen_raw_matcher_test_output.txt"
  var noRemoveFlag = false

  before {
    RawMatcher.setOutputWriter(UnorganizedOutputWriter)

    // Make sure that TestFilePath cannot collide with an existing file
    var outputFileProbe = new File(TestFilePath)
    if (outputFileProbe.exists) {
      noRemoveFlag = true
      fail(TestFilePath + " exists already, so tests cannot run . Please remove it")
    }
    UnorganizedOutputWriter.setOutputFile(TestFilePath)
  }

  after {
    var outputFileProbe = new File(TestFilePath)
    if (!noRemoveFlag)
      outputFileProbe.delete()
  }

  test("The RawMatcher works end to end") {
    // mockDataInterface.expects.getEntities returning entities.iterator
    fail("Test not yet written and probably will never be written")
  }

  test("writeEntities can work in the common case") {
    RawMatcher.writeEntities(entities.slice(0, 2).iterator)
    assert("entity,1,a,example_description\nentity,2,a,\n"
           === scala.io.Source.fromFile(TestFilePath).mkString)
  }

  test("buildEntityTable can write out a list of entities") {
    val entityTable = RawMatcher.buildEntityTable(entities.iterator)
    val expected = mutable.Map("a"->(mutable.ArrayBuffer() ++ entities.slice(0, 3)),
                               "b b"->(mutable.ArrayBuffer() ++ entities.slice(3, 4)),
                               "b"->(mutable.ArrayBuffer() ++ entities.slice(4, 5)))
    assert(expected === entityTable)
  }

  test("buildCandidates can build the common case") {
    var entityTable = mutable.Map("a"->(mutable.ArrayBuffer() ++ entities.slice(0, 3)),
                                  "b"->(mutable.ArrayBuffer() ++ entities.slice(4, 5)))
    var mention = mentions(0)
    var candidateResults = RawMatcher.buildCandidates(entityTable, mention)
    var expected = mutable.Map(entities(0) -> 1.0 / 3,
                               entities(1) -> 1.0 / 3,
                               entities(2) -> 1.0 / 3)
    assert(expected === candidateResults)
  }

  test("buildCandidates returns an empty Map in the nonexisting case") {
    var entityTable = mutable.Map("a"->(mutable.ArrayBuffer() ++ entities.slice(0, 3)),
                                  "b"->(mutable.ArrayBuffer() ++ entities.slice(4, 5)))
    var mention = new Mention(1, "ansdasd", Array(1))
    val expected = Map[Entity, Double]()
    assert(expected === RawMatcher.buildCandidates(entityTable, mention))
  }

  test("findAndWriteCandidates can handle the common case") {
    var entityTable = mutable.Map("a"->(mutable.ArrayBuffer() ++ entities.slice(0, 3)),
                                  "b"->(mutable.ArrayBuffer() ++ entities.slice(4, 5)))
    RawMatcher.findAndWriteCandidates(entityTable, tokens(0))
    val expected = "mention,1,a,1,1,3,1,0.3333,2,0.3333,3,0.3333\n"
    assert(expected === scala.io.Source.fromFile(TestFilePath).mkString)
  }
}
