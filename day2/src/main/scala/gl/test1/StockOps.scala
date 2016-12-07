package gl.test1

import java.io.IOException

object StockOps {



  def traitFun(op:StockOperation): String =
  {
    val myCode = "APPL"
    val `while` = 1
    op match {
      case Ask(`myCode`,x) => s"!!!!APPL sk $x"
      case Ask(code,Amount("$",x)) => s"ask $code $x"
      case Bid(code,x) => s"buy $code $x"
      case _ => "other"
    }

  }

  def sum(x:StockOperation, y:StockOperation): Amount =
  {
    (x, y) match {
      case (xa @ Ask(_,_), ya @ Ask(_,_) ) if (xa.x.name == ya.x.name) => Amount(xa.x.name,xa.x.value+ya.x.value)
      case (xa @ Ask(_,_), ya @ Bid(_,_) ) if (xa.x.name == ya.x.name) => Amount(xa.x.name,xa.x.value- ya.x.value)
      case (Bid(xCode,Amount(xCurrency,xValue)),
            Ask(yCode,Amount(yCurrency,yValue))) if (xCurrency == yCurrency) => Amount(xCurrency, -xValue+yValue)
      case (Bid(xCode,Amount(xCurrency,xValue)),
             Bid(yCode,Amount(yCurrency,yValue))) if (xCurrency == yCurrency) => Amount(xCurrency,-xValue-yValue)
      case _ =>
             throw new Exception("Be-be-be!!")
    }
  }


}
