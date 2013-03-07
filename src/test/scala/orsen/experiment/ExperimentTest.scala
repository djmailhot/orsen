package test.orsen.experiment

import org.scalatest.FunSuite
import orsen.experiment._
import orsen.models._

import scala.collection.mutable

class ExperimentTests extends FunSuite {

  test("gatherGoldStandard should retrieve true entities") {
    fail("This probably won't ever get tested! Ad Hoc it!")
  }

  val mentions = Array(new Mention(1, "asd", 5),
                       new Mention(2, "bsd", 5),
                       new Mention(3, "tsd", 5))

  val entities = Array(new Entity(101, "asd"),
                       new Entity(102, "bsd"),
                       new Entity(103, "tsd"),
                       new Entity(104, "tsd"))


  val goldStandard = Map[Mention, Entity](
                        mentions(0)->entities(0),
                        mentions(1)->entities(1),
                        mentions(2)->entities(3))

  val candidates  = Array(((entities(1), 100.0)),
                          ((entities(2), 75.0)),
                          ((entities(3), 25.0)))

  val computedLinks = mutable.Map[Mention, mutable.ArrayBuffer[(Entity, Double)]](
                        mentions(1)->mutable.ArrayBuffer((entities(1),100.0)),
                        mentions(2)->mutable.ArrayBuffer((entities(2),75.0),
                                                         (entities(3),25.0))
                      )

  val ratings = mutable.Map[Mention, Int](
                  mentions(0)->(-1), // Not in list
                  mentions(1)->0, // 0th rank
                  mentions(2)->1 // 1th rank
                )


  test("findMentions works in the common cases") {

    var linkedCandidates = Array(mentions(1)->candidates(0), // One candidate
                        new Mention(3, "tsd", 5)->candidates(1), // Multiple candidates
                        new Mention(3, "tsd", 5)->candidates(2),// Multiple candidates
                        new Mention(4, "led", 6)->candidates(0) // Not a target
                         )
    // Handle both same reference and different (equivalent) instance case
    // Handle both single entity and multiple entity cases
    var expected = computedLinks

    // From one ordering of entities
    var results  = Experiment.findMentions(goldStandard, linkedCandidates.iterator)
    assert(expected.size === results.size)
    results.foreach {
      (keyValue) =>
      assert(expected(keyValue._1) === results(keyValue._1))
    }
  }

  test("findMentions works if you switch up input order ") {

    // Handle both same reference and different (equivalent) instance case
    // Handle both single entity and multiple entity cases
    var linkedCandidates = Array(mentions(1)->candidates(0), // One candidate
                        new Mention(3, "tsd", 5)->candidates(2),// Multiple candidates
                        new Mention(3, "tsd", 5)->candidates(1), // Multiple candidates
                        new Mention(4, "led", 6)->candidates(0) // Not a target
                         )
    var expected = computedLinks

    // From one ordering of entities
    var results  = Experiment.findMentions(goldStandard, linkedCandidates.iterator)
    assert(expected.size === results.size)
    results.foreach {
      (keyValue) =>
      assert(expected(keyValue._1) === results(keyValue._1))
    }
  }

  test("createRatings works in the common cases") {
    var expected = ratings
    var results = Experiment.createRatings(goldStandard, computedLinks)
    assert(expected === results)
  }

  test("reverseRatings works in the common cases") {
    val tempRatings = mutable.Map[Mention, Int](
                    mentions(0)->(-1),
                    mentions(1)->0,
                    mentions(2)->1,
                    new Mention(10, "asd", 5)->0,
                    new Mention(12, "asd", 5)->0,
                    new Mention(13, "asd", 5)->0,
                    new Mention(14, "asd", 5)->(-1)
                  )
    val expected = mutable.Map[Int, Int](-1->2,0->4,1->1)
    var results  = Experiment.reverseRatings(tempRatings)
    assert(expected === results)
  }


}
