package com.gl.tpexample

case class SubscriberRate(userId: Long, start: Long, end: Long, tariffPlanName: String, rate: Double)

object SubscriberRate {

  implicit def csv: CSV[SubscriberRate] = new CSV[SubscriberRate] {

    def encode(t: SubscriberRate): String = List(t.userId, t.start, t.end, t.tariffPlanName, t.rate).mkString(",")

    def decode(s: String): Either[String, SubscriberRate] = ???

  }

}