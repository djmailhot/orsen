package orsen.models

/** A Entry that represents a unit of data from the crosswiki data set
  *
  * @constructor create a new CrosswikiEntry with a mention, a corresponding entity and a probability
  * @param mention the mention text of this entry
  * @param entityId the specific id of the entity that the specified mention maps to
  * @param score the probability that this mention maps to this entity
  */
class CrosswikiEntry(_mentionText: String, _entityId: Int, _score: Double) {

  /** The mention of this CrosswikiEntry */
  def mentionText: String = _mentionText
  /** The entityId of this CrosswikiEntry */
  def entityId: Int = _entityId
  /** The probability of this CrosswikiEntry */
  def score: Double = _score

  override def toString() = "<CrosswikiEntry|mentionText: %s, entityId: %d, prob: %f>".format(this.mentionText, this.entityId, this.score)
}
