package test.orsen.detectors

import orsen.detectors._
import orsen.models._
import orsen.output._
import orsen.datainterface._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalamock.scalatest.MockFactory
import org.scalamock.ProxyMockFactory

class RawMatcherTests extends FunSuite with BeforeAndAfter
                                       with MockFactory
                                       with ProxyMockFactory {

  val mockDataInterface = mock[DataInterface]
  val mockOutputWriter  = mock[OutputWriter]

  before {
    RawMatcher.setDataInterface(mockDataInterface)
    RawMatcher.setOutputWriter(mockOutputWriter)
  }

  test("writeEntities can write out a list of entities") {
    fail("Test not yet written")
  }

  test("buildEntityTable can build the common case") {
    fail("Test not yet written")
  }
}
