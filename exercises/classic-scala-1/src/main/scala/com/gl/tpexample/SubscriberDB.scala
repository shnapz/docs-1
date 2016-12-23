package com.gl.tpexample

import scala.reflect.runtime.universe._


trait Subscriber
{
  def id: Long
}

trait TariffPlan
{
  /**
    * @param logs  set of actions for one user.
    * output: price or error message if logs is incorrect.
    */
  def rate(logs:Seq[UserAction]):Either[String,Double]
}

trait Unlimited extends TariffPlan
{
  // TODO: implement
}



object UserDB
{

  def tariffPlan[T <: TariffPlan](s:Subscriber): T = ???

  def subscriber(subscriberId:Long): Option[Subscriber] = ???

}



