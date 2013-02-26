package orsen.models

/** A Entity that represents a concept from text
  *
  * @constructor create a new Entity with an id and a name
  * @param id the Entity's unique id
  * @param name the Entity's name
  */
class Entity(entityId: Int, entityName: String) {

  /** The unique id of this Entity */
  def id: Int = entityId
  /** The name of this Entity */
  def name: String = entityName
  /** A description of this Entity */
  var description: String = ""

  override def toString() = "<Entity|id: %d, name: %s>".format(this.id, this.name)

}
