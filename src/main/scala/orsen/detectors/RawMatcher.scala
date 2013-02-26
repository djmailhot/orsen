package orsen.detectors

import orsen.detector.Detector
import orsen.models._
import orsen.datainterface.stub._
import orsen.output._

/** RawMatcher uses raw string matching to generate mention candidates
  * For any mention, its list of candidates are all entities that have
  * the same name. All candidates share a uniform probability
  */
object RawMatcher extends Detector {

  def run() {
    // Dump all Entities to output
    // Build Hash Table of Entities by name
    // For every sentence
    // For every word of every sentence
    // Build match, write match
  }

  /** Writes all entity information to the OutputWriter */
  def writeEntities(entities: Iterator[Entity]) {

  }

  /** Builds a Map of [Entity Name(String)] to Array[Entity Id(Integer)]
    *
    * @return a Map of Entity Name to Array of Entity Ids
    */
  def buildEntityTable(entities: Iterator[Entity]): Map[String, Array[Integer]] = {
    var entityTable = Map[String, Array[Integer]]()
    return entityTable
  }

  /** Returns a Map[Entity, Double]
    *
    */
  def buildCandidates(entityTable: Map[String, Array[Integer]], mention: Mention) {

  }
}
