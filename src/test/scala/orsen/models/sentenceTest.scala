package test.orsen.models

import org.scalatest.FunSuite

import orsen.models.Sentence

class SentenceTest extends FunSuite {

  test("Sentence construction parses input text from tokens correctly") {
    var expected = "Vageta , what does the scouter say about his Power level ?"
    var tokens = expected.split(' ')
    var sentence = new Sentence(9001, tokens)
    assert(sentence.text == expected)
  }

}
