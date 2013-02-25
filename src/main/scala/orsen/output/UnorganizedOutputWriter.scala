package orsen.output

import java.io.{File, FileWriter}
import orsen.models._
import org.scalamock.annotation.mock

/** An UnorganizedOutputWriter writes all output to a single file.
  * All lines match one of the three formats:
  *   entity,id,name,description
  *   sentence,id,term_1_id,term_2_id,...
  *   term,id,name,candidate_1_id,candidate_1_probability,candidate_2_id,...
  *
  */
object UnorganizedOutputWriter extends OutputWriter {

  // TODO: This should come from a config file
  /** The filename of the output file */
  val targetFile = "/tmp/orsen_unorganized_output_writer_output.txt"

  /** The FileWriter object that the UnorganizedOutputWriter writes to */
  var outputFile = new FileWriter(targetFile)

  /** Reassigns the output file and creates a new output stream for the new
   *  file.
   *
   *  @param filename The name of the file for the new output stream
   */
  def setOutputFile(filename: String) {
    outputFile = new FileWriter(filename)
  }

  /** Writes the given information to the output file as a line with values
    * separated by commas.
    *
    * @param information An array of anything to write to the output file
    */
  def writeInformation(information: Array[Any]) {
    outputFile.write(information.mkString(","))
    outputFile.write("\n")
    outputFile.flush()
  }

  /** Closes the UnorganizedOutputWriter */
  def close() {
    outputFile.close()
  }


  def writeEntity(entity: Entity) {
    writeInformation(Array[Any]("entity", entity.id, entity.name, entity.description))
  }

  def writeSentence(sentence: Sentence) {
    writeInformation(Array[Any]("sentence", sentence.id) ++ sentence.termIds)
  }

  def writeTerm(term: Term, candidates: Map[Entity, Double]) {
    /** A map of candidate(Entity) ids to their probabilities */
    val candidatesList = candidates.foldLeft(Array[Any]()){
      (acc, candidate) => acc :+ candidate._1.id :+ candidate._2
    }
    val information = Array[Any]("term",term.id,term.name) ++ candidatesList
    writeInformation(information)
  }
}
