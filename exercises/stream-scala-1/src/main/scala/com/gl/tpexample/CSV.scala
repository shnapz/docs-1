package com.gl.tpexample

trait CSV[T] {

  def decode(s: String): Either[String, T]

  def encode(t: T): String

}
