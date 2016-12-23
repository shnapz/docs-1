package com.gl.tpexample

import java.io.File

//TODO: implement 2 types of tariff plans
// type Unlimit: monthly-fee
// type:  SMS - 0.001$ * baseCost,   Voice - baseCost
//    customer, which have second TP, can have additional filelds - their rate coefficients.

case class SubscriberRate(userId: Long, start:Long, end:Long, tariffPlanName: String, rate:Double)

object SubscriberRate
{

  implicit def csv: CSV[SubscriberRate] = ???

}

trait CSV[T]
{

  def decode(s:String):Either[String,T]

  def encode(t:T):String

}


object Main {


  /**
    * Input:  CSV  file, with UserActions
    * Output:  CSV file || screen output: from SubscriberRate
    *    UserId,
    * @param args
    */
  def main(args: Array[String]):Unit =
  {
    val input = readFile[UserAction,CSV](args(0))
    val output = rate(input)
    writeFile[SubscriberRate,CSV](args(1),output)
  }

  def rate(log:Seq[UserAction]):Seq[SubscriberRate] = ???

  def readFile[T,Encoding[_]](fname:String)(implicit ser:Encoding[T]):Seq[T] = ???

  def writeFile[T,Encoding[_]](fname:String, data: Seq[T])(implicit ser:Encoding[T]):Unit = ???


  def readFileAsStrings(fname:String):Iterator[String] =
    io.Source.fromFile(new File(fname)).getLines()



}
