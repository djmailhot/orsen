package orsen.output

import orsen.models._

/** A OutputInterface is a singleton that provides an interface for writing
  * computed Mentions and Entity candidates data.
  */

trait OutputWriter {

  /** Writes the Entity to the OutputInterface's output target
    *
    * @param entity The entity to write
    */
  def writeEntity(entity: Entity)

  /** Writes the Sentence to the OutputInterface's output target
    *
    * @param sentence The sentence to write
    */
  def writeSentence(sentence: Sentence)

  /** Writes the Mention to the OutputInterface's output target
    *
    * @param mention The mention to write. Assumed to only ever be written once
    * @param candidates Candidates for the mention. Keys are Entities and their values the
    * computed probability that the true Sense for the mention.
    */
  def writeMention(mention: Mention, candidates: Map[Int, Double])

  /** Writes the Mention to the OutputInterface's output target
    *
    * @param mention The mention to write. Assumed to only ever be written once
    * @param candidates Candidates for the mention. Keys are Entities and their values the
    * computed probability that the true Sense for the mention.
    */
  def writeMentionWithEntities(mention: Mention, candidates: Map[Entity, Double]) {
    /** A map of candidate(Entity) ids to their probabilities */
    var candidatesReformed = candidates.map{
      (candidate) => (candidate._1.id, candidate._2)
    }
    writeMention(mention, candidatesReformed)
  }
}
