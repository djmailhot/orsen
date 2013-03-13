package orsen.detectors

import orsen.datainterface._
import orsen.detectors._
import orsen.output._
import orsen.models._

import scala.collection.mutable

// TODO: Move differences from NERChainer and RawMatcher into abstract trait
object NERChainer extends Detector {

  override def main(argument: Array[String]) {
    // Dump all entities to output
    // For all sentences
    //    Generate Mentions

    MongoDataInterface.resetDataInterface("orsen")

    dataInterface.getSentences().foreach { (sentence) =>
      val mentions = extractMentions(sentence.tokenIterator)
      val candidateMatches = mentions.foreach {
        (mention) =>
        var candidates = retrieveMatchingEntities(mention)
        outputWriter.writeMentionWithEntitiesWithArray(mention, candidates)
      }
    }
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

    tokenClusters = tokenClusters.filter{(cluster) => cluster.first.nerTag == "PERSON" || cluster.first.nerTag == "LOCATION" || cluster.first.nerTag == "ORGANIZATION"}
  //   tokenClusters.foreach{(cluster) =>
  //   println("sentenceId: " + cluster.first.sentenceId)
  //   println("index: " + cluster.first.index)
  //   println("id: " + cluster.first.sentenceId * 100 + cluster.first.index)
  //   println("---")
  // }
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
      return crossWikiEntries.map{
        (entry) =>
        (MongoDataInterface.getEntityByText(entry.entityText)->entry.score)
      }.toArray
    } catch {
      case nse: NoSuchElementException => return Array()
    }
    return Array()
  }

}
