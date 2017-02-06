package com.gl.glconf.parsers

import java.util

import com.gl.glconf.GConf
import com.gl.glconf.GConf.Format
import fs2.{Handle, Pull, Pure, Stream}

import scala.annotation.tailrec
import scala.collection.immutable.Queue

// Pull:  Done| Output(x)| Error | p1 >> p2 (where p1:Pull, p2: Pull)

// Free
//


//


object PropertyParser extends Format {

  sealed trait TreeData
  case class Leaf(value: String) extends TreeData
  case class Node(map:Map[String,Leaf]) extends TreeData

  override def parse[M[_]](input: Stream[M, Byte]): M[TreeData] = {
    (input through fs2.text.utf8Decode) map (s => parseString())
  }


  //=> Task1: utf8 test => Stream[M,String] (splitted by '\n'),
  // => Task2: Stream[M,PropertyDef] => (fold) TreeData.
  // => Task3: GConf <=> TaskData:
  // => Task4:  s/PropertyDef/Json.




  // representation of string "a.b.c=v"
  //  name: ["a","b","c"],  [v - value]
  case class PropertyDef(name:Seq[String], value:String)

  def splitByLines[M[_]](input: Stream[M,String]):Stream[M,String] =
  {
    def go(h:Handle[M,String],buff:String): Pull[M,String,Unit] =
    {
      h.await1.optional.flatMap{
        case Some((schunk,next)) =>
          val parts = schunk.split('\n')
          if (parts.size==1) {
            go(next, buff+schunk)
          } else {
            val fullParts = parts.take(parts.size - 1)
            val pull: Pull[M, String, Unit] = Pull.pure(())
            fullParts.toSeq.foldLeft((buff, pull)) { (s, e) =>
              val (buff, pull) = s
              ("", pull >> Pull.output1(buff + e))
            }
            pull >> go(next, parts.last)
          }
        case None =>  Pull.output1(buff) >> Pull.done
      }
    }
    input.pull[M,String](h=>go(h,""))
  }

  def splitByLines1[M[_]](input: Stream[M,String]):Stream[M,String] =
  {
    def go(h:Handle[M,String],buff:String): Pull[M,String,Unit] =
    {
      h.await1.optional.flatMap{
        case Some((schunk,next)) =>
          val parts = schunk.split('\n')
          if (parts.size==1) {
            go(next, buff+schunk)
          } else {
            val fullParts = parts.take(parts.size - 1)
            val pull: Pull[M, String, Unit] = Pull.pure(())
            fullParts.toSeq.foldLeft((buff, pull)) { (s, e) =>
              val (buff, pull) = s
              ("", pull >> Pull.output1(buff + e))
            }
            pull >> go(next, parts.last)
          }
        case None =>  Pull.output1(buff) >> Pull.done
      }
    }
    input.pull[M,String](h=>go(h,""))
  }


  //def onElement(f: Option[Either[Char]]=>Pull):Stream

  def charsToLines[M[_]](input: Stream[M,Char]): Stream[M,String] =
  {

    def go(h:Handle[M,Char],buff:Queue[Char]): Pull[M,String,Unit] =
    {
      h.await1.optional.flatMap{
        case Some((ch,next)) =>
          if (ch=='\n') {
            Pull.output1(buff mkString "") >> go(next,Queue.empty)
          } else {
            go(next,buff :+ ch)
          }
        case None => Pull.output1(buff mkString "") >> Pull.done
      }
    }

    input.pull[M,String](h => go(h,Queue.empty))

  }




}
