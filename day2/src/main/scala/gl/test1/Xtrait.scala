package gl.test1

case class Amount(name:String, value:Int)

sealed trait StockOperation

case class Ask(code:String, x:Amount) extends StockOperation

case class Bid(code:String, x:Amount) extends StockOperation

case class Error(msg:String) extends StockOperation





