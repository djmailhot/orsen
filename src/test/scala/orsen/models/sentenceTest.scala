package test.orsen.models

import org.scalatest.FunSuite

import orsen.models.Sentence

class SentenceTest extends FunSuite {

  test("Sentence construction parses input text into tokens correctly") {
    var text = "Vageta , what does the scouter say about his Power level ?"
    var expected = text.split(' ')
    var sentence = new Sentence(9001, text)
    assert(sentence.tokens == expected)
  }

}
