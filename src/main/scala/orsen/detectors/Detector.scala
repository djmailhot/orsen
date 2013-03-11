package orsen.detectors

import orsen.datainterface._
import orsen.datainterface.stub._
import orsen.output._

/** A Detector is a singleton that encapsulates the entity detection process
  *
  * A Detector should assume that all models have been generated and are
  * accessible through a DataInterface object.
  *
  * A Detector should also output information using an object that implements
  * the OutputWriter Interface
  */
trait Detector {

  // TODO: These should come from a config file
  // TODO: These probably should be immutable, but leave as var for the sake of testing
  var dataInterface: DataInterface = MongoDataInterface
  var outputWriter: OutputWriter   = EveryoneDiedOutputWriter

  /** Runs the Detector algorithm **/
  def main(argument: Array[String])

  /** Assigns the DataInterface to be used for the Detector */
  def setDataInterface(interface: DataInterface) {
    dataInterface = interface
  }

  /** Assigns the OutputWriter to be used for the Detector */
  def setOutputWriter(writer: OutputWriter) {
    outputWriter = writer
  }

}
