package com.gl.tpexample

case class Subscriber(id: Long)

sealed trait TariffPlan {
  def rate(logs: Seq[UserAction]): Option[Double]
}

case class Unlimited(fixedRate: Double) extends TariffPlan {
  def rate(logs: Seq[UserAction]): Option[Double] = Some(fixedRate)
}

case class Regular(baseRate: Double) extends TariffPlan {
  def rate(logs: Seq[UserAction]): Option[Double] = Some(
    logs.foldLeft(0.0)((rate, userAction) => userAction match {
      case UserAction(_, _, "SMS", _, _, _) => rate + baseRate * 0.001
      case UserAction(_, _, "Voice", _, duration, _) => rate + baseRate * duration
    }))
}

object UserDB {

  def tariffPlan(s: Subscriber): TariffPlan = s match {
    case Subscriber(1) => Unlimited(100)
    case Subscriber(2) => Regular(5)
  }

  def subscriber(subscriberId: Long): Option[Subscriber] = subscriberId match {
    case 1 | 2 => Some(Subscriber(subscriberId))
    case _ => None
  }

}
