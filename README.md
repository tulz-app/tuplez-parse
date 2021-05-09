![Maven Central](https://img.shields.io/maven-central/v/app.tulz/tuplez-parse_sjs1_2.13.svg)

# tuplez-parse

A syntax extension for [cats-parse](https://github.com/typelevel/cats-parse) for product tuple concatenation. 

Based on the [tuplez](https://github.com/tulz-app/tuplez) library.

Published for Scala `2.12`, `2.13` and `3.0.0-RC3`, JVM and Scala.js 1.5.1+.

## Adding to a project

```scala
"app.tulz" %%% "tuplez-parse" % "0.3.0"
```

Additionally, one of the following dependencies is required (see [tuplez](https://github.com/tulz-app/tuplez)). 

This is required because this library depends on `tuplez-full-light` but with the `Provided` modifier, 
so it will not be transitively added to your project.

```scala
// tupleN + scalar, scalar + tupleN, tupleN + tupleM, up to Tuple22
"app.tulz" %%% "tuplez-full" % "0.3.6"

// or

// tupleN + scalar, scalar + tupleN, tupleN + tupleM, up to Tuple10
"app.tulz" %%% "tuplez-full-light" % "0.3.6"

// or

// tupleN + scalar, up to Tuple22
"app.tulz" %%% "tuplez-basic" % "0.3.6"

// or

// tupleN + scalar, up to Tuple10 
"app.tulz" %%% "tuplez-basic-light" % "0.3.6" 
```

## Extension method

Currently, the only extension method provided is `~~`. It calls the underlying `~` method, and concatenates the resulting 
tuple using the implicits from the tuplez library. 

## Usage

```scala
import cats.parse.{Parser => P}
import app.tulz.tuplez.parse._

// no concatenation
P.char('0') ~ P.char('1') // Parser[(Unit, Unit)]

// with concatenation
P.char('0') ~~ P.char('1') // Parser[Unit]


// no concatenation
P.charIn('0') ~ P.charIn('1') ~ P.charIn('2') // Parser[((Char, Char), Char)]

// with concatenation
P.charIn('0') ~~ P.charIn('1') ~~ P.charIn('2') // Parser[(Char, Char, Char)]


// no concatenation
P.charIn('1') ~ P.char('/') ~ P.charIn('2') // Parser[((Char, Unit), Char)]

// with concatenation
P.charIn('1') ~~ P.char('/') ~~ P.charIn('2') // Parser[(Char, Char)]

```

## Author

Iurii Malchenko – [@yurique](https://twitter.com/yurique) / [keybase.io/yurique](https://keybase.io/yurique)


## License

`tuplez-parse` is provided under the [MIT license](https://github.com/tulz-app/tuplez-parse/blob/main/LICENSE.md).
