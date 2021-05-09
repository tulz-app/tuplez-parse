package app.tulz.tuplez.parse

import org.junit.Test
import org.junit.Assert._
import cats.parse.{Parser => P}

class ProductConcatTests {

  @Test def `Unit+Unit`(): Unit = {
    testParser(
      parser = P.char('0') ~~ P.char('1'),
      input = "01",
      expected = unit
    )
  }

  P.charIn('1') ~ P.char('/') ~ P.charIn('2')

  @Test def `Unit+Scalar`(): Unit = {
    testParser(
      parser = P.char('0') ~~ P.charIn('1'),
      input = "01",
      expected = '1'
    )
  }

  @Test def `Scalar+Scalar`(): Unit = {
    testParser(
      parser = P.charIn('0') ~~ P.charIn('1'),
      input = "01",
      expected = ('0', '1')
    )
  }

  @Test def `Scalar+Scalar+Scalar`(): Unit = {
    testParser(
      parser = P.charIn('0') ~~ P.charIn('1') ~~ P.charIn('2'),
      input = "012",
      expected = ('0', '1', '2')
    )
  }

  @Test def `Scalar+Unit+Scalar`(): Unit = {
    testParser(
      parser = P.charIn('0') ~~ P.char('1') ~~ P.charIn('2'),
      input = "012",
      expected = ('0', '2')
    )
  }

  @Test def `Scalar+Scalar+Unit`(): Unit = {
    testParser(
      parser = P.charIn('0') ~~ P.charIn('1') ~~ P.char('2'),
      input = "012",
      expected = ('0', '1')
    )
  }
  @Test def `Unit+Scalar+Unit`(): Unit = {
    testParser(
      parser = P.char('0') ~~ P.charIn('1') ~~ P.char('2'),
      input = "012",
      expected = '1'
    )
  }

  @Test def `Scalar+(Scalar+Scalar)`(): Unit = {
    testParser(
      parser = P.charIn('0') ~~ (P.charIn('1') ~~ P.charIn('2')),
      input = "012",
      expected = ('0', '1', '2')
    )
  }

  @Test def `(Scalar+Scalar)+Scalar`(): Unit = {
    testParser(
      parser = (P.charIn('0') ~~ P.charIn('1')) ~~ P.charIn('2'),
      input = "012",
      expected = ('0', '1', '2')
    )
  }

  @Test def `(Scalar+Scalar) + (Scalar+Scalar)`(): Unit = {
    testParser(
      parser = (P.charIn('0') ~~ P.charIn('1')) ~~ (P.charIn('2') ~~ P.charIn('3')),
      input = "0123",
      expected = ('0', '1', '2', '3')
    )
  }

  private lazy val unit: Unit = (): Unit

  private def runParse[A](parser: P[A], input: String): A = {
    parser.parse(input) match {
      case Left(error)        => fail(s"Failed to parse at offsets: ${error.offsets.toList.mkString(", ")}").asInstanceOf[A]
      case Right((_, result)) => result
    }
  }

  private def testParser[A](
    parser: P[A],
    input: String,
    expected: A
  ): Unit =
    assertEquals(
      expected,
      runParse(
        parser,
        input
      )
    )

}
