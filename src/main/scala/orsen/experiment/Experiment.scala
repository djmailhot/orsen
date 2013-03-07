package orsen.experiment

import orsen.models._
import scala.collection.mutable

/* Runs an experiment over some Detector Output Data
 *
 */
object Experiment {
  def main(arguments: Array[String]) {
    // TODO Read all real mention=>entity matches
    var goldStandard = gatherGoldStandard()
    var matches = findMentions(goldStandard, getCandidateIterator())
    // TODO Read Data, searching for said mentions
    // TODO For each mention, build up the list of candidates
    // TODO How often is the real entity inside of the list of candidates?
  }

  def gatherGoldStandard(): Map[Mention, Entity] = {
    // TODO: Implement
    return Map[Mention, Entity]()
  }
  
  def getCandidateIterator(): Iterator[(Mention, (Entity, Double))] = {
    // TODO: Implement
    return Array[(Mention, (Entity, Double))]().iterator
  }

  def createRatings(goldStandard: Map[Mention, Entity],
                    computedLinks: mutable.Map[Mention, mutable.ArrayBuffer[Entity]]): Map[Mention, Int] = {
    return Map[Mention, Int]()
  }

  /* Runs through mention/(candidate/probability) pairs, looking for any that have one of the target mentions
   */
  def findMentions(goldStandard: Map[Mention, Entity],
                   candidateIterator: Iterator[(Mention, (Entity, Double))]
                   ): mutable.Map[Mention, mutable.ArrayBuffer[(Entity, Double)]] = {
    var results = mutable.Map[Mention, mutable.ArrayBuffer[(Entity, Double)]]()
    candidateIterator.foreach {
      (pair) => 
      val mention = pair._1
      val entity  = pair._2._1
      val probability = pair._2._2
      if (goldStandard.contains(mention)) {
        var entityArr:mutable.ArrayBuffer[(Entity, Double)] =
          (results.get(mention) getOrElse mutable.ArrayBuffer[(Entity, Double)]())
        entityArr += ((entity, probability))
        println(entityArr)
        results.put(mention, entityArr)
      }
    }
// sort by 2nd element
// Sorting.quickSort(pairs)(Ordering.by[(String, Int, Int), Int](_._2)

    results.foreach {
      (candidateList) =>
      var mention = candidateList._1
      var entityArr = candidateList._2
      
      results.put(mention, entityArr.sortWith(_._2>_._2))
    }
    return results
  }
}
