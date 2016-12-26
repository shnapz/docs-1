package com.gl.tpexample

import java.io.{File, PrintWriter}

object Main {

  def main(args: Array[String]): Unit = {
    val input = readFile[UserAction](args(0))
    val output = rate(input)
    writeFile[SubscriberRate](args(1), output)
  }

  def rate(log: Seq[UserAction]): Seq[SubscriberRate] = {
    log.groupBy(action => action.subscriberId)
      .toList
      .map({ case (subscriberId, actions) => calculateRate(subscriberId, actions) })
      .filter(o => o.isDefined)
      .map(o => o.get)
  }

  def calculateRate(subscriberId: Long, actions: Seq[UserAction]): Option[SubscriberRate] = {
    UserDB.subscriber(subscriberId) match {
      case Some(subscriber) => {
        val tariffPlan = UserDB.tariffPlan(subscriber)
        val rate = tariffPlan.rate(actions) match {
          case Right(r) => r
          case Left(l) => 0
        }
        val timestamps = actions.map(a => a.timestamp)
        Some(SubscriberRate(subscriberId, timestamps.min, timestamps.max, tariffPlan.toString, rate))
      }
      case _ => None
    }
  }

  def readFile[T](fname: String)(implicit ser: CSV[T]): Seq[T] =
    readFileAsStrings(fname)
      .map(s => ser.decode(s))
      .filter(e => e.isRight)
      .map(e => e.right.get)
      .toSeq

  def writeFile[T](fname: String, data: Seq[T])(implicit ser: CSV[T]): Unit = new PrintWriter(fname) {
    for (t <- data) println(ser.encode(t))
    close()
  }

  def readFileAsStrings(fname: String): Iterator[String] =
    io.Source.fromFile(new File(fname)).getLines()

}
