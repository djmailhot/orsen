package orsen.output

import orsen.models._

/** A OutputInterface is a singleton that provides an interface for writing
  * computed Terms and Entity candidates data.
  */

trait OutputWriter {

  /** Writes the Entity to the OutputInterface's output target
    */
  def writeEntity(entity: Entity)

  /** Writes the Sentence to the OutputInterface's output target
    */
  def writeSentence(sentence: Sentence)

  /** Writes the Term to the OutputInterface's output target
    *
    * The term should be in a completed form with all candidates
    * already computed, and it is assumed that the same term will
    * not be written twice.
    */
  def writeTerm(term: Term)

}
