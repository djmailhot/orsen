package orsen.models

/** A Entry that represents a unit of data from the crosswiki data set
  *
  * @constructor create a new CrosswikiEntry with a mention, a corresponding entity and a probability
  * @param mention the mention text of this entry
  * @param entity the specific entity that the specified mention maps to
  * @param score the probability that this mention maps to this entity
  */
class CrosswikiEntry(_mention: String, _entity: String, _score: Double) {

  /** The mention of this CrosswikiEntry */
  def mention: String = _mention
  /** The entity of this CrosswikiEntry */
  def entity: String = _entity
  /** The probability of this CrosswikiEntry */
  def score: Double = _score

  override def toString() = "<CrosswikiEntry|mention: %s, entity: %s, prob: %d>".format(this.mention, this.entity, this.score)
}
