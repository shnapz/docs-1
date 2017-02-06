package com.gl.glconf.parsers

import fs2.{Chunk, Handle, NonEmptyChunk, Pipe, Pull}

/**
  * Created by Andrew on 06.02.2017.
  */

object StreamPipes {
  def splitString(text: String) = {
    text.lastOption match {
      case Some('\n') => text.split('\n') :+ ""
      case _ => text.split('\n')
    }
  }

  def getPullFromHandle[M[_]](handle: Handle[M, String], buffer: String): Pull[M, String, Any] = {
    handle.await1Option.flatMap(x => x match {
      case Some((chunk, nextHandle)) => {
        val split = splitString(chunk)
        if (split.length > 1) {
          Pull.output1[M, String](buffer + split.head) >>
            Pull.output[M, String](Chunk.seq(split.drop(1).dropRight(1))) >>
            getPullFromHandle(nextHandle, split.last)
        }
        else {
          getPullFromHandle(nextHandle, buffer + chunk)
        }
      }
      case None => Pull.output1(buffer)
    })
  }

  def separateLines[M[_]]: Pipe[M, String, String] = input => input.pull[M, String](h => getPullFromHandle(h, ""))
}
