package defer

object Test extends App {

  def withDefer[Result](action: Defer => Result): Result = {
    val defer = new Defer
    try {
      action(defer)
    } finally {
      defer.runAll()
    }
  }

  def myFunction(a: Int, b: Int): Unit = {
    withDefer { defer =>
      println(a)
      defer {println(1)}
      println(b)
      defer {println(2)}
    }
  }

  myFunction(10,20)

}
