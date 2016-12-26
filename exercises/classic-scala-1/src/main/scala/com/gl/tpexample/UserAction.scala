package com.gl.tpexample

case class UserAction(
                       timestamp: Long,
                       subscriberId: Long,
                       action: String, // "SMS" | "Voice"
                       tariffZone: Long,
                       duration: Long,
                       sizeOfFile: Long
                     )

object UserAction {

  implicit def x: CSV[UserAction] = new CSV[UserAction] {

    def encode(t: UserAction): String = ???

    def decode(s: String): Either[String, UserAction] = {
      val strings: Array[String] = s.split(",")
      val longs = (strings.take(2) ++ strings.drop(3)).map(_.toLong)
      try {
        Right(UserAction(longs(0), longs(1), strings(2), longs(2), longs(3), longs(4)))
      } catch {
        case e: Exception => Left(s)
      }
    }

  }

}
