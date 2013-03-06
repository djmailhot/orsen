package orsen.models

/** A Entity that represents a concept from text
  *
  * @constructor create a new Entity with an id and a name
  * @param id the Entity's unique id
  * @param name the Entity's name
  */
class Entity(_id: Int, _name: String, _description: String) {
  def this(_id: Int, _name: String) {
    this(_id, _name, "")
  }

  /** The unique id of this Entity */
  def id: Int = _id
  /** The name of this Entity */
  def name: String = _name
  /** A description of this Entity */
  var description: String = _description

  override def toString() = "<Entity|id: %d, name: %s>".format(this.id, this.name)

  override def hashCode() = id.hashCode()

  override def equals(other: Any) = other match {
    case that: Entity => this.id == that.id
    case _ => false
  }
}
