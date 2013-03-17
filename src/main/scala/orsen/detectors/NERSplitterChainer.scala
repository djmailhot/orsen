package orsen.detectors

import orsen.datainterface._
import orsen.detectors._
import orsen.output._
import orsen.models._

import java.io.{File, FileWriter}
import scala.collection.mutable

// TODO: Move differences from NERChainer and RawMatcher into abstract trait
object NERSplitterChainer extends Detector {

  var ignoredMentionFile = new FileWriter("/tmp/orsen_ignored_mentions")
  override def main(argument: Array[String]) {
    // Dump all entities to output
    // For all sentences
    //    Generate Mentions

    MongoDataInterface.resetDataInterface("orsen")

    dataInterface.getSentences().foreach { (sentence) =>
      println("sentence: " + sentence.id)
      val mentions = extractMentions(sentence.tokenIterator)
      val candidateMatches = mentions.foreach {
        (mention) =>
        println("\tmention: " + mention.text);
        var candidates = retrieveMatchingEntities(mention)
        candidates ++= retrieveFallbackEntities(mention)
        outputWriter.writeMentionWithEntitiesWithArray(mention, candidates)
      }
    }
    ignoredMentionFile.close()
  }

  def extractMentions(tokens: Iterator[Token]): mutable.ArrayBuffer[Mention] = {
    var tokenClusters = mutable.ArrayBuffer[mutable.ArrayBuffer[Token]]()
    var index = 0
    tokens.foreach {
      (token) =>
      token.index = index
      if (tokenClusters.size > 0 && tokenClusters.last.last.nerTag == token.nerTag) {
        tokenClusters.last += token
      } else {
        var newCluster = mutable.ArrayBuffer[Token](token)
        tokenClusters += newCluster
      }
      index += 1
    }

    tokenClusters.foreach{
      (cluster) =>
      if (!( cluster.first.nerTag == "PERSON"
        || cluster.first.nerTag == "LOCATION"
        || cluster.first.nerTag == "ORGANIZATION"
        || cluster.first.nerTag == "MISC")) {
        ignoredMentionFile.write("ignored " + new Mention(cluster.first.sentenceId * 100 + cluster.first.index,
                  cluster.map((token) => token.text
                 ).mkString(" "), cluster) + ", NERTAG: " + cluster.first.nerTag + "\n")
      }
    }

    tokenClusters = tokenClusters.filter{
      (cluster) =>
       (cluster.first.nerTag == "PERSON"
      || cluster.first.nerTag == "LOCATION"
      || cluster.first.nerTag == "ORGANIZATION"
      || cluster.first.nerTag == "MISC")
    }
    // The ID given to a new Mention is the same as the id of the first token
    var mentions = tokenClusters.map{(cluster) =>
      new Mention(cluster.first.sentenceId * 100 + cluster.first.index,
                  cluster.map((token) => token.text
                 ).mkString(" "), cluster)
    }
    return mentions
  }


  /* Given a mention, find all matching entities and their priors
   */
  def retrieveMatchingEntities(mention: Mention): Array[(Entity, Double)] = {
    try {
      var crossWikiEntries = MongoDataInterface.getCrosswikiEntrysByMention(mention.text)
      var results =  crossWikiEntries.map{
        (entry) =>
        (MongoDataInterface.getEntityByText(entry.entityText)->entry.score)
      }.toArray
      // println("\t" + results.map((x)=>x._1.name).mkString(", "))
      return results
    } catch {
      case nse: NoSuchElementException => return Array()
    }
    return Array()
  }

  def retrieveFallbackEntities(mention: Mention): mutable.ArrayBuffer[(Entity, Double)] = {
    try {
      //TODO generate subtokens
      var results = mutable.ArrayBuffer[(Entity, Double)]()
      var found = mutable.Set[Entity]()
      var subtokens = mention.tokenIterator
      subtokens.foreach {
        (subtoken) => 
        // println(subtoken)
        var entities = MongoDataInterface.getEntitiesBySubtoken(subtoken.text)//.toArray
        // println("\t" + entities.map((x)=>x.name).mkString(", "))
        // TODO: Find Dups
        entities.foreach {
          (entity) =>
          if (!found.contains(entity)) {
            results += entity->0.0
            found += entity
          }
        }
      }
      // println("----")
      // println("\t" + results.map((x)=>x._1.name).mkString(", "))
      return results
    } catch {
      case nse: NoSuchElementException => return mutable.ArrayBuffer()
    }
    return mutable.ArrayBuffer()
  }
}
