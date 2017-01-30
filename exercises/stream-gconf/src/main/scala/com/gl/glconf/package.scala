package com.gl

import scala.util.{Failure, Success, Try}
import scalaz.concurrent.Task
import scalaz.~>

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


}

