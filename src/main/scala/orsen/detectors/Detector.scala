package orsen.detector

/** A Detector is a singleton that encapsulates the entity detection process
  *
  * A Detector should assume that all models have been generated and are
  * accessible through a DataInterface object.
  *
  * A Detector should also output information using an object that implements
  * the OutputWriter Interface
  */
trait Detector {

  /** Runs the Detector algorithm **/
  def run()

}
