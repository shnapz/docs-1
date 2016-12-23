package com.gl.tpexample

case class UserAction(
  timestamp:       Long,
  subscriberId:    Long,
  action:           String,  // "SMS" | "Voice"
  tariffZone:       Long,
  duration:         Long,
  sizeOfFile:       Long
)


object UserAction
{

  implicit def x: CSV[UserAction] = ???

}
