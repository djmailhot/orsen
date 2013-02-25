package test.orsen.output

import orsen.output.UnorganizedOutputWriter
import orsen.models._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import java.io.File

class UnorganizedOutputWriterTests extends FunSuite with BeforeAndAfter{

  val TestFilePath    = "/tmp/test_output.txt"
  var exampleEntity   = new Entity(42, "entity_name")
  var exampleSentence = new Sentence(39, Array(1, 2, 3))
  var exampleTerm     = new Term(1, "term_name", 39)

  var noRemoveFlag = false
  before {
    exampleEntity.description = "example_description"
    exampleTerm.candidates += (1 -> 0.75,2->0.25)

    // Make sure that TestFilePath cannot collide with an existing file
    var outputFileProbe = new File(TestFilePath)
    if (outputFileProbe.exists) {
      noRemoveFlag = true
      fail(TestFilePath + " exists already, so tests cannot run . Please remove it")
    }

    UnorganizedOutputWriter.assignOutputFile(TestFilePath)
  }

  after {
    var outputFileProbe = new File(TestFilePath)
    if (!noRemoveFlag)
      outputFileProbe.delete()
  }

  test("writeInformation writes correctly") {
    UnorganizedOutputWriter.writeInformation(Array("text",1,2,3))
    assert(scala.io.Source.fromFile(TestFilePath).mkString === "text,1,2,3\n")
  }

  // TODO: In a glorious world, writeInformation would be stubbed out. Kind of tough to pull off
  // with singletons though
  test("writeEntity formatting is right") {
    UnorganizedOutputWriter.writeEntity(exampleEntity)
    assert(scala.io.Source.fromFile(TestFilePath).mkString === "entity,42,entity_name,example_description\n")
  }

  test("writeSentence formatting is right") {
    UnorganizedOutputWriter.writeSentence(exampleSentence)
    assert(scala.io.Source.fromFile(TestFilePath).mkString === "sentence,39,1,2,3\n")
  }

  test("writeTerm formatting is right") {
    UnorganizedOutputWriter.writeTerm(exampleTerm)
    assert(scala.io.Source.fromFile(TestFilePath).mkString === "term,1,term_name,1,0.75,2,0.25\n")
  }

}
