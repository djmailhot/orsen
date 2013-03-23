package orsen.detectors

import orsen.datainterface._
import orsen.detectors._
import orsen.output._
import orsen.models._
import edu.berkeley.nlp.ling.Tree

import java.io.{File, FileWriter}
import scala.collection.mutable
import scala.collection.JavaConversions._

// TODO: Move differences from NERChainer and RawMatcher into abstract trait
object NERPOSSplitterChainer extends Detector {

  var ignoredMentionFile = new FileWriter("/tmp/orsen_ignored_mentions")
  override def main(argument: Array[String]) {
    // Dump all entities to output
    // For all sentences
    //    Generate Mentions

    var sourceCollection = "sentences"
    val index = argument.indexOf("-parser")
    if (index != -1) {
      val parser = argument(index + 1)
      if (parser == "cj") {
        sourceCollection = "sentences"
      } else if (parser == "cky") {
        sourceCollection = "postrees"
      }
    }

    MongoDataInterface.resetDataInterface("orsen")

    dataInterface.getSentences().foreach { (sentence) =>
      println("sentence: " + sentence.id)
      val mentions = extractMentions(sentence, sourceCollection)
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

  def extractMentions(sentence: Sentence, sourceCollection: String = "sentences"): mutable.ArrayBuffer[Mention] = {
    val tokens: Iterator[Token] = sentence.tokenIterator

    // build up lists of consecutive NER tagged tokens
    // INSTEAD, traverse the POS tree, looking for consecutive tokens marked with interesting NER tags
    // Expand a mention's tokens based on a shared parent POS tag of NP or NNP
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

    // filter only our interested tags
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

    // putting original mentions in Array
    var mentions = mutable.ArrayBuffer[Mention]()
    tokenClusters.foreach{(cluster) =>
      val mentionText = sentence.text.slice(cluster.first.spanStart, cluster.last.spanEnd)
      mentions.add(new Mention(cluster.first.sentenceId * 10000 + cluster.first.index, mentionText, cluster))
    }

    print("exploring POS trees")
    // work with POS tag trees
    val postree: Tree[String] = MongoDataInterface.getPOSTreeBySentence(sentence.id, sourceCollection)
    val treeToParentMap: mutable.Map[Tree[String],Tree[String]] = mutable.HashMap[Tree[String],Tree[String]]()
    recordParentLinks(postree, treeToParentMap)

    val treeToLevelMap: mutable.Map[Tree[String],Int] = mutable.HashMap[Tree[String],Int]()
    recordLevels(postree, treeToLevelMap, 0)

    val tokenToTreeMap: mutable.Map[Token,Tree[String]] = mutable.HashMap[Token,Tree[String]]()
    recordTokenLinks(sentence.tokenIterator, postree, tokenToTreeMap)

    val treeToTokenMap: mutable.Map[Tree[String],Token] = mutable.HashMap[Tree[String],Token]()
    recordTreeLinks(sentence.tokenIterator, postree, treeToTokenMap)

    // The ID given to a new Mention is the same as the id of the first token
    tokenClusters.foreach{(cluster) =>
      print(".")
      
      // extract trees
      val trees: mutable.ArrayBuffer[Tree[String]] = cluster.map{(token) => 
        tokenToTreeMap.getOrElse(token, new Tree(Token.UNKNOWN_TOKEN)) 
      }
      // extract levels
      val levels: mutable.ArrayBuffer[Int] = trees.map{(tree) =>
        treeToLevelMap.getOrElse(tree, -1) 
      }

      // find the common Tree root that covers all tokens detected to have interesting NER tags
      var sameRoot = trees.foldLeft(true){(same, ele) => same && trees.last == ele}
      while (!sameRoot) {
        val min = levels.min
        val max = levels.max
        for(i <- 0 until levels.length) {
          if (levels(i) > min || (min == max && !sameRoot)) {
            levels(i) -= 1
            trees(i) = treeToParentMap.getOrElse(trees(i), new Tree(Token.UNKNOWN_TOKEN))
          }
        }
        sameRoot = trees.foldLeft(true){(same, ele) => same && trees.last == ele}
      }

      // iterate up the parsed Tree as long as the common root is labeled as a {NP, NNP}
      val acceptedLabels = Array[String]("NN", "NP", "NNP")
      var root = trees.last
      var parent = treeToParentMap.getOrElse(root, new Tree(Token.UNKNOWN_TOKEN))
      while(acceptedLabels.contains(parent.getLabel())) {
        root = parent
        parent = treeToParentMap.getOrElse(root, new Tree(Token.UNKNOWN_TOKEN))
      }

      val terminalList = root.getPreOrderTraversal().filter{ (tree) => tree.isLeaf() }
      val tokenList = terminalList.map{ (terminal) => 
        treeToTokenMap.getOrElse(terminal, Token.UNKNOWN_TOKEN).asInstanceOf[Token]
      }
      val mentionTokens = tokenList.toArray

      val mentionText = sentence.text.slice(tokenList.first.spanStart, tokenList.last.spanEnd)
      mentions.add(new Mention((tokenList.first.sentenceId * 100 + tokenList.first.index) * 100, mentionText, mentionTokens))
    }
    println(sentence.id)
    return mentions
  }


  private def recordParentLinks(root: Tree[String], treeToParentMap: mutable.Map[Tree[String],Tree[String]]) {
    root.getChildren().foreach {
      case (child) =>
          treeToParentMap.put(child, root)
          recordParentLinks(child, treeToParentMap)
    }
  }

  private def recordLevels(root: Tree[String], treeToLevelMap: mutable.Map[Tree[String],Int], level: Int) {
    treeToLevelMap.put(root, level)
    root.getChildren().foreach {
      case (child) =>
          recordLevels(child, treeToLevelMap, level + 1)
    }
  }

  private def recordTokenLinks(it: Iterator[Token], root: Tree[String], tokenToTreeMap: mutable.Map[Token,Tree[String]]) {
    val treeList = root.getPreOrderTraversal().filter{ (tree) => tree.isLeaf() }
    var index = 0
    while(it.hasNext) {
      val token = it.next
      val tree = treeList(index)
      if (!(token.text == tree.getLabel())) {
        throw new IllegalStateException()
      }
      tokenToTreeMap.put(token, tree)
      index += 1
    }
  }

  private def recordTreeLinks(it: Iterator[Token], root: Tree[String], treeToTokenMap: mutable.Map[Tree[String],Token]) {
    val treeList = root.getPreOrderTraversal().filter{ (tree) => tree.isLeaf() }
    var index = 0
    while(it.hasNext) {
      val token = it.next
      val tree = treeList(index)
      if (!(token.text == tree.getLabel())) {
        throw new IllegalStateException()
      }
      treeToTokenMap.put(tree, token)
      index += 1
    }
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
