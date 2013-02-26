package test.orsen.datainterface

import orsen.dataconversion.CreateMongoDB
import orsen.models._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MongoConverterTest extends FunSuite with BeforeAndAfter {

  CreateMongoDB.createDatabase("test", "test")

}
