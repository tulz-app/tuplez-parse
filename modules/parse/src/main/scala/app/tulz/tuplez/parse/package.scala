package app.tulz.tuplez

import cats.parse._

package object parse {

  implicit def parserOps[A](underlying: Parser[A]): ParserOps[A] = new ParserOps[A](underlying)

}
