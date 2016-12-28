package defer

class Defer {

  private var actions: List[() => Unit] = Nil

  def apply(action: => Unit): Unit =
    actions = (() => action) :: actions

  def runAll(): Unit = actions.foreach(_.apply())

}
