package com.gl

import java.io.{File, FileInputStream, InputStream}

import fs2.Stream
import fs2.util.Suspendable

import scala.util.{Failure, Success, Try}
import scalaz.concurrent.Task
import scalaz.{Applicative, ~>}

package object glconf {

  implicit def s2path(s:String):GConfPath = GConfPath(Seq(s))


  implicit object tryToTask extends (Try ~> Task)
  {
    override def apply[A](fa: Try[A]): Task[A] =
      fa match {
        case Success(x) => Task.now(x)
        case Failure(e) => Task.fail(e)
      }
  }

  implicit class FileSource(f:File) extends GConf.Source {

    override def input[M[_]]()(implicit mAp:Suspendable[M]): Stream[M, Byte] = {
      val fis: InputStream = new FileInputStream(f)
      val mfis: M[InputStream] = mAp.pure(fis)
      fs2.io.readInputStream[M](mfis,1024)
    }

  }

}

