package orsen.detectors

import orsen.detector.Detector
import orsen.models._
import orsen.datainterface.stub._
import orsen.output._

import scala.collection.mutable

/** RawMatcher uses raw string matching to generate mention candidates
  * For any mention, its list of candidates are all entities that have
  * the same name. All candidates share a uniform probability
  */
object RawMatcher extends Detector {

  def run() {
    // Dump all Entities to output
    writeEntities(dataInterface.getEntities())
    // Build Hash Table of Entities by name
    // For every sentence
    // For every word of every sentence
    // Build match, write match

  }

  /** Writes all entity information to the OutputWriter */
  def writeEntities(entities: Iterator[Entity]) {

  }

  /** Builds a Map of [Entity Name(String)] to Array[Entity]
    *
    * @return a Map of Entity Name to Array of Entities
    */
  def buildEntityTable(entities: Iterator[Entity]): mutable.Map[String, mutable.ArrayBuffer[Entity]] = {
    var entityTable = mutable.Map[String, mutable.ArrayBuffer[Entity]]()
    entities.foreach { (entity) =>
      var entityArr:mutable.ArrayBuffer[Entity] =
        (entityTable.get(entity.name) getOrElse mutable.ArrayBuffer[Entity]())
      entityArr += entity
      entityTable.put(entity.name, entityArr)
    }
    return entityTable
  }

  /** Returns a Map[Entity, Double]
    *
    */
  def buildCandidates(entityTable: Map[String, Array[Entity]], mention: Mention): Map[Entity, Double] = {
    if (!entityTable.contains(mention.text))
      throw new NoSuchElementException
    var candidates = entityTable(mention.text)
    var candidateTable:Map[Entity, Double] = candidates.map{
      (candidate) => (candidate->(1.0 / candidates.size))
    }.toMap
    return candidateTable
  }
}
