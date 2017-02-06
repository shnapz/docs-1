package com.gl.glconf.parsers

import fs2.Pure
import org.scalatest.FunSuite
import StreamPipes._

/**
  * Created by Andrew on 06.02.2017.
  */
class StreamPipesSuite extends FunSuite {
  test("should return items divided from a single chunk") {
    // arrange
    val input = fs2.Stream.emit[Pure, String]("value\nvalue")

    // act
    val lines = input.through(separateLines)

    // assert
    assertResult(List("value", "value"))(lines.toList)
  }

  test("should return items divided from multiple chunks and concated middle item") {
    // arrange
    val input = fs2.Stream.emit[Pure, String]("value\nval") ++ fs2.Stream.emit[Pure, String]("ue\nvalue")

    // act
    val lines = input.through(separateLines)

    // assert
    assertResult(List("value", "value", "value"))(lines.toList)
  }

  test("should return empty items as empty lines") {
    // arrange
    val input = fs2.Stream.emit[Pure, String]("\n\nval\n") ++ fs2.Stream.emit[Pure, String]("value\n")

    // act
    val lines = input.through(separateLines)

    // assert
    assertResult(List("", "", "val", "value", ""))(lines.toList)
  }

  test("should ignore empty chunks") {
    // arrange
    val input = fs2.Stream.emit[Pure, String]("val") ++ fs2.Stream.emit[Pure, String]("") ++ fs2.Stream.emit[Pure, String]("value\nval")

    // act
    val lines = input.through(separateLines)

    // assert
    assertResult(List("valvalue", "val"))(lines.toList)
  }
}
