package app.tulz.tuplez.parse

import cats.parse._
import app.tulz.tuplez.Composition

class ParserOps[A](underlying: Parser[A]) {

  def ~~[B](that: Parser0[B])(implicit composition: Composition[A, B]): Parser[composition.Composed] =
    Parser.product10(underlying, that).map { case (a, b) => composition.compose(a, b) }

}
