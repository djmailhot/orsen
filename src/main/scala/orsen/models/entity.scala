package orsen.models

/** A Entity that represents a concept from text
  *
  * @constructor create a new Entity with an id and a name
  * @param id the Entity's unique id
  * @param name the Entity's name
  */
class Entity(entityId: Integer, entityName: String) {

  /** The unique id of this Entity */
  def id: Integer = entityId
  /** The name of this Entity */
  def name: String = entityName

}
