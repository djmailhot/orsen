package orsen.experiment

import orsen.models._
import orsen.datainterface._
import scala.collection.mutable

/* Runs an experiment over some Detector Output Data
 *
 */
object BadExperiment {
  val DataFilePath = "/tmp/orsen_unorganized_output_writer_output.txt"
  val GoldFilePath = "/tmp/orsen_gold_standard.txt"

  def main(arguments: Array[String]) {
    MongoDataInterface.resetDataInterface("experiment")
    var goldStandard  = gatherGoldStandard()
    var computedLinks = findMentions(goldStandard, getCandidateIterator())
    var ratings = createRatings(goldStandard, computedLinks)
    var reverse = reverseRatings(ratings)
    writeReverseRatings(reverse)
  }

  /* return a Map[Mention, Entity] of mention/entity pairs that are known to be genuine */
  def gatherGoldStandard(): Map[String, Entity] = {
    scala.io.Source.fromFile(GoldFilePath).mkString.split("\n").map{
      (line) =>
      var pieces = line.split("   ")
      var entity = MongoDataInterface.getEntityByText(pieces(1))
      pieces(1)->entity
    }.toMap
  }

  // return an iterator with which to loop through all mentions and their candidates 
  def getCandidateIterator(): Iterator[(String, (Entity, Double))] = {
    var text = scala.io.Source.fromFile(DataFilePath).mkString
    // TODO: This is horrible
    return computeCandidateIterator(text)
  }


  //TODO: This is so inefficient that everybody died
  def computeCandidateIterator(text: String): Iterator[(String, (Entity, Double))] = {
    var results =  mutable.ArrayBuffer[(String, (Entity, Double))]()
    text.split("\n").map {
      (line) =>
      val pieces = line.split("|M461CD3L1M373RL0L|")
      val mention = pieces(2)
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

  /* Runs through mention/(candidate/probability) pairs, looking for any that have one of the gold standard mentions
   */
  def findMentions(goldStandard: Map[String, Entity],
                   candidateIterator: Iterator[(String, (Entity, Double))]
                   ): mutable.Map[String, mutable.ArrayBuffer[(Entity, Double)]] = {
    var results = mutable.Map[String, mutable.ArrayBuffer[(Entity, Double)]]()
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
  def createRatings(goldStandard: Map[String, Entity],
                    computedLinks: mutable.Map[String, mutable.ArrayBuffer[(Entity, Double)]]):
                      mutable.Map[String, Int] = {
    var ratings = mutable.Map[String, Int]()
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

  def reverseRatings(ratings: mutable.Map[String, Int]): mutable.Map[Int, Int] = {
    var reverse = mutable.Map[Int, Int]()
    ratings.foreach {
      case (mention, rank) => reverse.put(rank, (reverse.get(rank) getOrElse 0) + 1)
    }
    return reverse
  }

  def writeReverseRatings(reverse: mutable.Map[Int, Int]) {
    printf("# GraphIndex Rank Quantity")
    reverse.toArray.zipWithIndex.foreach {
      case ((-1, quantity), index) => printf("%d %s %d\n", index, "Missing", quantity)
      case ((rank, quantity), index) => printf("%d %d %d\n", index, rank, quantity)
    }
  }
}
