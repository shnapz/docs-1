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

  def tariffPlan(s:Subscriber): TariffPlan = ???

  def subscriber(subscriberId:Long): Option[Subscriber] = ???

  def retrieveSubscriber(subscriberId:Long): Either[String,Subscriber] =
    subscriber(subscriberId).toRight(s"subscriber with id $subscriberId is not found"")

}



