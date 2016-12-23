package com.gl.tpexample

object ActionType extends Enumeration
{
  type V = Value
  val SMS,Voice = Value
  //val Voice=Value
}

/*
sealed trait ActionType1
case object SMS1 extends ActionType1
case object Voice1 extends ActionType1
*/

case class UserAction(
  timestamp:       Long,
  subscriberId:    Long,
  //action:           String,  // "SMS" | "Voice"
  actionType:       ActionType.V    ,
  tariffZone:       Long,
  duration:         Long
)


object UserAction
{

  implicit val x: CSVEncoding[UserAction] = ???

}
