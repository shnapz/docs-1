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
      .toSeq
      .map({ case (subscriberId, actions) => calculateRate(subscriberId, actions) })
      .filter(_.isDefined)
      .map(_.get)
  }

  def calculateRate(subscriberId: Long, actions: Seq[UserAction]): Option[SubscriberRate] = for {
    subscriber <- UserDB.subscriber(subscriberId)
    tariffPlan = UserDB.tariffPlan(subscriber)
    rate <- tariffPlan.rate(actions)
    timestamps = actions.map(_.timestamp)
  } yield SubscriberRate(subscriberId, timestamps.min, timestamps.max, tariffPlan.toString, rate)

  def readFile[T](fname: String)(implicit ser: CSV[T]): Seq[T] =
    readFileAsStrings(fname)
      .map(ser.decode)
      .filter(_.isRight)
      .map(_.right.get)
      .toSeq

  def writeFile[T](fname: String, data: Seq[T])(implicit ser: CSV[T]): Unit = new PrintWriter(fname) {
    for (t <- data) println(ser.encode(t))
    close()
  }

  def readFileAsStrings(fname: String): Iterator[String] =
    io.Source.fromFile(new File(fname)).getLines()

}
