package orsen.experiment

import orsen.models._
import scala.collection.mutable

/* Runs an experiment over some Detector Output Data
 *
 */
object Experiment {
  def main(arguments: Array[String]) {
    var goldStandard  = gatherGoldStandard()
    var computedLinks = findMentions(goldStandard, getCandidateIterator())
    var ratings = createRatings(goldStandard, computedLinks)
    var reverse = reverseRatings(ratings)
    writeReverseRatings(reverse)
  }

  /* return a Map[Mention, Entity] of mention/entity pairs that are known to be genuine */
  def gatherGoldStandard(): Map[Mention, Entity] = {
    // TODO: Implement
    return Map[Mention, Entity]()
  }

  // return an iterator with which to loop through all mentions and their candidates 
  def getCandidateIterator(): Iterator[(Mention, (Entity, Double))] = {
    // TODO: Implement
    return Array[(Mention, (Entity, Double))]().iterator
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

    // Sort
    results.foreach {
      (candidateList) =>
      var mention = candidateList._1
      var entityArr = candidateList._2
      // The lack of something like ConcurrentModificationException is Intredasting.......
      results.put(mention, entityArr.sortWith(_._2>_._2))
    }
    return results
  }
  def createRatings(goldStandard: Map[Mention, Entity],
                    computedLinks: mutable.Map[Mention, mutable.ArrayBuffer[(Entity, Double)]]):
                      mutable.Map[Mention, Int] = {
    var ratings = mutable.Map[Mention, Int]()
    goldStandard.foreach {
      (standard) =>
      val mention = standard._1
      val trueEntity = standard._2
      val candidates = computedLinks.get(mention) getOrElse mutable.ArrayBuffer()
      candidates.zipWithIndex.foreach {
        case(((candidate, _)), index) =>
        if (candidate == trueEntity) {
          ratings.put(mention, index)
        }
      }
      // Doesn't exist
      if (!ratings.contains(mention))
        ratings.put(mention, -1)
    }
    return ratings
  }

  def reverseRatings(ratings: mutable.Map[Mention, Int]): mutable.Map[Int, Int] = {
    var reverse = mutable.Map[Int, Int]()
    ratings.foreach {
      case (mention, rank) => reverse.put(rank, (reverse.get(rank) getOrElse 0) + 1)
    }
    return reverse
  }

  def writeReverseRatings(reverse: mutable.Map[Int, Int]) {
    reverse.foreach {
      case (value, quantity) => printf("%d %d\n", (value, quantity))
    }
  }
}
