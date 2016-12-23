package com.gl.tpexample

import java.io.File

import scala.util.{Failure, Success, Try}
import scala.util.control.NonFatal

//TODO: implement 2 types of tariff plans
// type Unlimit: monthly-fee
// type:  SMS - 0.001$ * baseCost,   Voice - baseCost
//    customer, which have second TP, can have additional filelds - their rate coefficients.

case class SubscriberRate(userId: Long, start:Long, end:Long, tariffPlanName: String, rate:Double)

object SubscriberRate
{

  implicit def csv: CSVEncoding[SubscriberRate] = ???

}

trait CSVEncoding[T]
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
    import UserAction._
    val input = readFile[UserAction](args(0))
    val output = rate(input)
    writeFile[SubscriberRate,CSVEncoding](args(1),output)
  }

  def rate(log:Seq[UserAction]):Seq[SubscriberRate] = ???

  def readFile[T](fname:String)(implicit ser:CSVEncoding[T]): Either[String,Seq[T]] =
  {

    /*
    try {
      Right(
       for (s <- readFileAsStrings(fname).toSeq) yield {
         ser.decode(s).left.map { message =>
           throw new IllegalAccessException(message)
         }
       }
      )
    } catch {
      case NonFatal(e) => Left(e.getMessage)
    }
    */

    Try {
      for ((s,i) <- readFileAsStrings(fname).zipWithIndex.toSeq) yield {
        ser.decode(s) match {
          case Left(message) => throw new IllegalStateException(s"$message at line $i")
          case Right(line) => line
        }
      }
    }   .toEither.left.map(_.getMessage)

    /*
    val s0: Either[String,Seq[T]] = Right(Seq())
    readFileAsStrings(fname).foldLeft(s0){(state,line) =>
        for(seq <- state;
            t <- ser.decode(line)
         ) yield {
          seq :+ t
        }
    }
    */

  }

  def writeFile[T,Encoding[_]](fname:String, data: Seq[T])(implicit ser:Encoding[T]):Unit = ???

  def readFileAsStrings(fname:String):Iterator[String] =
    io.Source.fromFile(new File(fname)).getLines()



}
