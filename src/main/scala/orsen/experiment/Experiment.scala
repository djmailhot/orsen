package orsen.experiment

import orsen.models._

/* Runs an experiment over some Detector Output Data
 *
 */
object Experiment {
  def main(arguments: Array[String]) {
    // TODO Read all real mention=>entity matches
    // TODO Read Data, searching for said mentions
    // TODO For each mention, build up the list of candidates
    // TODO How often is the real entity inside of the list of candidates?
  }

  def gatherGoldStandard(): Map[Mention, Entity] = {
    // TODO: Implement
    return Map[Mention, Entity]()
  }

  def findMentions(targetMentions: Map[Mention, Entity], 
                   mentions: Iterator[Mention]): Map[Mention, Array[Entity]] = {
    return Map[Mention, Array[Entity]]()
  }
}
