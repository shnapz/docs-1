package com.gl.glconf

import java.util.concurrent.atomic.AtomicInteger

import scalaz.concurrent.Task



class MonadUsageTest /*extends FunSpec*/
{

  def myFun(): Unit =
  {
    val pairs = for {x <- countGenerator()
                      obj <- config.get[Task,Int]("objectSource")
                    } yield (x,obj)
    System.out.println("pairs")
  }

  def config(): GConf = ???

  def countGenerator():Task[Int] = Task.delay{ counter.incrementAndGet() }

  val counter = new AtomicInteger(0)

}
