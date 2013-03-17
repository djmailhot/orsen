package orsen.experiment

import orsen.models._
import orsen.datainterface._
import scala.collection.mutable
import java.io.{File, FileWriter}

/* Runs an experiment over some Detector Output Data
 *
 */
object BadExperiment {
  val DataFilePath = "/tmp/orsen_everyone_died_output_writer_output.txt"
  val GoldFilePath = "/tmp/orsen_gold_standard.txt"

  def main(arguments: Array[String]) {
    MongoDataInterface.resetDataInterface("orsen")

    val candidateIterator = getCandidateIterator()
    var goldStandard  = gatherGoldStandard()
    var computedLinks = findMentions(goldStandard, candidateIterator)
    var ratings = createRatings(goldStandard, computedLinks)
    var reverse = reverseRatings(ratings)
    writeReverseRatings(reverse)
    // writeCandidateLists(candidateIterator, goldStandard)
    writeHumanReadableCandidateLists(scala.io.Source.fromFile(DataFilePath).mkString)
  }

  /* return a Map[Mention, Entity] of mention/entity pairs that are known to be genuine */
  def gatherGoldStandard(): Map[String, Entity] = {
    scala.io.Source.fromFile(GoldFilePath, "ISO-8859-1").mkString.split("\n").map{
      (line) =>
      var pieces = line.split("   ")
      try {
        var entity = MongoDataInterface.getEntityByText(pieces(1))
        pieces(0)->entity
      } catch {
        case nse: NoSuchElementException => ""->new Entity(-1, "")
      }
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
      val pieces = line.split("""\|M461CD3L1M373RL0L\|""")
      val mention = pieces(2)
      println("working on mention " + pieces(1))
      if (pieces(1) == "818") {
        ((mention, ((new Entity(9001, "kill me", ""), 9000.1))))
      } else {
        val candidateStartIndex =  (4 + pieces(3).toInt)
        val candidateCount = pieces(candidateStartIndex).toInt
        val candidates     = pieces.slice(candidateStartIndex + 1, candidateStartIndex + 1 + candidateCount * 2)

        var entities      = mutable.ArrayBuffer[Entity]()
        var probabilities = mutable.ArrayBuffer[Double]()
        candidates.zipWithIndex.foreach {
          case (entityOrProbability, index) =>
          if (index % 2 == 0) {
            // Entity Id
            val id = entityOrProbability.toInt
            // entities += new Entity(id, id.toString, "")
              entities += MongoDataInterface.getEntityById(id)
          } else {
            // Entity Probability
            probabilities += entityOrProbability.toDouble
          }
        }

        var computed = entities.zip(probabilities).foreach {
          case(entity, probability) =>
            results += ((mention, ((entity, probability))))
        }
        computed
      }
    }
    return results.iterator
  }

 def writeHumanReadableCandidateLists(text: String) {
    var outputFile = new FileWriter("/tmp/orsen_candidate_list_output.txt")
    outputFile.write("Mention\t gold =>\tCandidate 1, Candidate 2, Candidate 3, ...\n")
    var goldStandard  = gatherGoldStandard().withDefaultValue(new Entity(10, "<Entity Not Found>", ""))
    var results =  mutable.ArrayBuffer[(String, (Entity, Double))]()
    text.split("\n").foreach {
      (line) =>
      val pieces = line.split("""\|M461CD3L1M373RL0L\|""")
      val mention = pieces(2)
      println("working on mention " + pieces(1))
      if (pieces(1) != "818") {
        val candidateStartIndex =  (4 + pieces(3).toInt)
        val candidateCount = pieces(candidateStartIndex).toInt
        val candidates     = pieces.slice(candidateStartIndex + 1, candidateStartIndex + 1 + candidateCount * 2)

        var entities      = mutable.ArrayBuffer[Entity]()
        var probabilities = mutable.ArrayBuffer[Double]()

        outputFile.write("MENTION: " + mention + "\t" + goldStandard(mention) + ":")
        candidates.zipWithIndex.foreach {
          case (entityOrProbability, index) =>
          if (index % 2 == 0) {
            // Entity Id
            val id = entityOrProbability.toInt
            // entities += new Entity(id, id.toString, "")
              outputFile.write("\n\t\t" + MongoDataInterface.getEntityById(id))
          }
        }
      }
      outputFile.write("\n")
    }
    outputFile.close()
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
    var outputFile = new FileWriter("/tmp/orsen_gold_rankings.txt")
    goldStandard.foreach {
      (standard) =>
      val mention = standard._1
      val trueEntity = standard._2
      if (!computedLinks.contains(mention)) {
        // There is no mention that matches the gold standard's 'mention'
        ratings.put(mention, -2)
        outputFile.write("%s %d\n".format(mention, -2))
      } else {
        val candidates = computedLinks.get(mention) getOrElse mutable.ArrayBuffer()
        var index = findMatchingCandidate(candidates, trueEntity)
        if (index != -11) { // Not Found
          ratings.put(mention, index)
          outputFile.write("%s %d\n".format(mention, index))
        }
        // Doesn't exist
        if (!ratings.contains(mention)) {
          ratings.put(mention, -1)
          outputFile.write("%s %d\n".format(mention, -1))
        }
      }
    }
    outputFile.close()
    return ratings
  }

  def findMatchingCandidate(candidates: mutable.ArrayBuffer[(Entity, Double)], trueEntity: Entity): Int = {
    candidates.zipWithIndex.foreach {
      case(((candidate, _)), index) =>
      if (candidate == trueEntity) {
        return index
      }
    }
    return -11
  }

  def reverseRatings(ratings: mutable.Map[String, Int]): mutable.Map[Int, Int] = {
    var reverse = mutable.Map[Int, Int]()
    ratings.foreach {
      case (mention, rank) => reverse.put(rank, (reverse.get(rank) getOrElse 0) + 1)
    }
    return reverse
  }

  def writeReverseRatings(reverse: mutable.Map[Int, Int]) {
    var outputFile = new FileWriter("/tmp/orsen_bad_experiment_table.txt")
    outputFile.write("# GraphIndex Rank Quantity\n")
    reverse.toArray.sortWith(_._1<_._1).zipWithIndex.foreach {
      case ((-1, quantity), index) => outputFile.write("%d %s %d\n".format(index, "No Matching Candidate", quantity))
      case ((-2, quantity), index) => outputFile.write("%d %s %d\n".format(index, "No Matching Mention", quantity))
      case ((rank, quantity), index) => outputFile.write("%d %d %d\n".format(index, rank, quantity))
    }
    outputFile.close()
  }

  def writeCandidateLists(candidates: Iterator[(String, (Entity, Double))], gold: Map[String, Entity]) = {
    var golden = gold.withDefaultValue(new Entity(10, "<Entity Not Found>", ""))
    var outputFile = new FileWriter("/tmp/orsen_candidate_list_output.txt")
    outputFile.write("Mention\t gold =>\tCandidate 1, Candidate 2, Candidate 3, ...\n")
    var prev:String = "stub@#$@#$"
    var candidateNames:mutable.ArrayBuffer[String] = mutable.ArrayBuffer[String]()
    candidates.foreach {
      case (mention, ((entity, probability))) => 
      if (prev != mention) {
        outputFile.write("%s\t %s =>\t%s\n".format(prev, golden(prev).name,  candidateNames.mkString("\t")))
        candidateNames = mutable.ArrayBuffer[String]()
        candidateNames += entity.name
      } else {
        candidateNames += entity.name
      }
      prev = mention
    }
    outputFile.write("%s\t %s =>\t%s\n".format(prev, golden(prev).name,  candidateNames.mkString("\t")))
    outputFile.close()
  }
}

