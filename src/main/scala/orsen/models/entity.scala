package orsen.models

/** A Entity that represents a concept from text
  *
  * @constructor create a new Entity with an id and a name
  * @param id the Entity's unique id
  * @param name the Entity's name
  */
class Entity(entityId: Integer, entityName: String) {

  // the global id of this entity
  def id: Integer = entityId
  // the raw tokenized text of this entity
  def name: String = entityName

}
