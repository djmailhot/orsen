package test.orsen.detectors

import java.io.IOException
import java.util.Random
import orsen.dataconversion.CreateMongoDB
import orsen.detectors._
import orsen.models._
import orsen.output._
import orsen.datainterface._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfterEach
import org.scalamock.scalatest.MockFactory
import org.scalamock.ProxyMockFactory

import scala.collection.mutable

import java.io.File

class DetectorTests extends FunSuite with BeforeAndAfterAll
                                     with BeforeAndAfterEach
                                     with MockFactory
                                     with ProxyMockFactory {

  val salt = new Random().nextInt()
  val mongoDBName = "test" + salt
  val dataInterface =  MongoDataInterface

// TODO all
  override def beforeAll(configMap: Map[String, Any]) {
    MongoDataInterface.resetDataInterface(mongoDBName)
    try {
      CreateMongoDB.createDatabase(mongoDBName, "test")
      CreateMongoDB.createCrosswikiDatabase(mongoDBName, "test")
      //CreateMongoDB.addMultikeyIndexToEntities(mongoDBName)
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

// TODO all
  override def afterAll(configMap: Map[String, Any]) {
    try {
      CreateMongoDB.dropDatabase(mongoDBName)
    } catch {
      case ioe: IOException => fail(ioe.toString())
    }
  }

  override def beforeEach(configMap: Map[String, Any]) {
    MongoDataInterface.resetDataInterface(mongoDBName)
  }

  test("NERChainer.extractMentions should work in the easy case") {
    val testSentence = dataInterface.getSentenceById(4788)
    var mentions = NERChainer.extractMentions(testSentence.tokenIterator)
    var expected = mutable.ArrayBuffer(
        new Mention(478800, "Angola", Array(478800)),
        new Mention(478805, "MPLA", Array(478805)),
        new Mention(478824, "Angolan", Array(478824)))
    assert(expected === mentions)
  }

  test("NERChainer.extractMentions should work in the challenging case") {
    val testSentence = dataInterface.getSentenceById(4790)
    var mentions = NERChainer.extractMentions(testSentence.tokenIterator)
    var expected = mutable.ArrayBuffer(
        new Mention(479001, "Popular Movement for the Liberation of Angola", Array(479001)),
        new Mention(479009, "MPLA", Array(479009)),
        new Mention(479030, "National Union", Array(479030)),
        new Mention(479037, "Angola", Array(479037)),
        new Mention(479039, "UNITA", Array(479039)))
    assert(expected === mentions)
  }

  test("NERSplitterChainer.extractMentions should work in the easy case") {
    val testSentence = dataInterface.getSentenceById(4788)
    var mentions = NERSplitterChainer.extractMentions(testSentence.tokenIterator)
    var expected = mutable.ArrayBuffer(
        new Mention(478800, "Angola", Array(478800)),
        new Mention(478805, "MPLA", Array(478805)),
        new Mention(478824, "Angolan", Array(478824)))
    assert(expected === mentions)
  }

  test("NERSplitterChainer.extractMentions should work in the challenging case") {
    val testSentence = dataInterface.getSentenceById(4790)
    var mentions = NERSplitterChainer.extractMentions(testSentence.tokenIterator)
    var expected = mutable.ArrayBuffer(
        new Mention(479001, "Popular Movement for the Liberation of Angola", Array(479001)),
        new Mention(479009, "MPLA", Array(479009)),
        new Mention(479030, "National Union", Array(479030)),
        new Mention(479037, "Angola", Array(479037)),
        new Mention(479039, "UNITA", Array(479039)))
    assert(expected === mentions)
  }

  test("NERPOSSplitterChainer.extractMentions should work in the easy case") {
    val testSentence = dataInterface.getSentenceById(4788)
    var mentions = NERPOSSplitterChainer.extractMentions(testSentence)
    var expected = mutable.ArrayBuffer(
        new Mention(478800, "Angola 's ruling party", Array(478800)),
        new Mention(478804, "the MPLA", Array(478804)),
        new Mention(478823, "the Angolan electoral commission", Array(478823)))
    assert(expected === mentions)
  }

  test("NERPOSSplitterChainer.extractMentions should work in the challenging case") {
    val testSentence = dataInterface.getSentenceById(4790)
    var mentions = NERPOSSplitterChainer.extractMentions(testSentence)
    var expected = mutable.ArrayBuffer(
        new Mention(479000, "The Popular Movement for the Liberation of Angola -LRB- MPLA -RRB-", Array(479000)),
        new Mention(479029, "the National Union for the Total Independence of Angola -LRB- UNITA -RRB-", Array(479029)))
    assert(expected === mentions)
  }
}
