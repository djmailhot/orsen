package orsen.experiment

import orsen.models._
import scala.collection.mutable

/* Runs an experiment over some Detector Output Data
 *
 */
object Experiment {
  val DataFilePath = "/tmp/orsen_unorganized_output_writer_output.txt"

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
    var text = scala.io.Source.fromFile(DataFilePath).mkString
    // TODO: This is horrible
    return computeCandidateIterator(text)
  }


  //TODO: This is so inefficient that everybody died
  def computeCandidateIterator(text: String): Iterator[(Mention, (Entity, Double))] = {
    var results =  mutable.ArrayBuffer[(Mention, (Entity, Double))]()
    text.split("\n").map {
      (line) =>
      val pieces = line.split(",")
      val mention = new Mention(pieces(1).toInt, pieces(2))
      val candidateStartIndex =  (4 + pieces(3).toInt)
      val candidateCount = pieces(candidateStartIndex).toInt
      val candidates     = pieces.slice(candidateStartIndex + 1, candidateStartIndex + 1 + candidateCount * 2)

      var entities      = mutable.ArrayBuffer[Entity]()
      var probabilities = mutable.ArrayBuffer[Double]()
      candidates.zipWithIndex.foreach {
        case (entityOrProbability, index) =>
        if (index % 2 == 0) {
          // Entity Id
          entities += new Entity(entityOrProbability.toInt, "", "")

        } else {
          // Entity Probability
          probabilities += entityOrProbability.toDouble
        }
      }

      entities.zip(probabilities).foreach {
        case(entity, probability) =>
          results += ((mention, ((entity, probability))))
      }
    }
    return results.iterator
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
    reverse.zipWithIndex.foreach {
      case ((value, quantity), index) => printf("%d %d %d\n", index, value, quantity)
    }
  }
}
