package com.gl.glconf

import fs2.util.Suspendable

import scala.util.Try
import scalaz.{Applicative, MonadError, ~>}


trait GConf {

  def get[M[_],T](path: GConfPath)(implicit nat: Try ~> M): M[T]

  def getUnsafe[T](path: GConfPath): T

}


object GConf
{

  trait Source
  {
    def input[M[_]]()(implicit mAp:Suspendable[M]): fs2.Stream[M,Byte]
  }

  trait Format
  {
    def parse[M[_]](input: fs2.Stream[M,Byte]):M[GConf]
  }

  def load[M[_],S <: Source ,F <: Format](source:Source,format:Format)(implicit mAp:Suspendable[M]): M[GConf] =
  {
     format.parse(source.input[M])
  }


}