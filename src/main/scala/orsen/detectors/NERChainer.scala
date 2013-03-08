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
    dataInterface.getSentences().foreach { (sentence) =>
      val mentions = extractMentions(sentence.tokenIterator)
      val candidateMatches = mentions.foreach {
        (mention) =>
        var candidates = retrieveMatchingEntities(mention)
        outputWriter.writeMentionWithEntities(mention, candidates)
      }
    }
  }

  def extractMentions(tokens: Iterator[Token]): mutable.ArrayBuffer[Mention] = {
    var tokenClusters = mutable.ArrayBuffer[mutable.ArrayBuffer[Token]]()
    tokens.foreach {
      (token) =>
      if (tokenClusters.size > 0 && tokenClusters.last.last.nerTag == token.nerTag) {
        tokenClusters.last += token
      } else {
        var newCluster = mutable.ArrayBuffer[Token](token)
        tokenClusters += newCluster
      }
    }
    // The ID given to a new Mention is the same as the id of the first token
    var mentions = tokenClusters.map((cluster) => new Mention(cluster.first.id,
      cluster.map((token) => token.text).mkString(" "),
      cluster))
    return mentions
  }

  /* Given a mention, find all matching entities and their priors
   */
  def retrieveMatchingEntities(mention: Mention): Map[Entity, Double] = {
    // TODO:
    return Map[Entity, Double]()
  }
}
