

import gl.test1._


class Point(val x: Double = 0.0, val y:Double = 0.0)
{

  Console.println(s"Hi, ini.x = $x")

  def this(msg:String,x:Double,y:Double) =
    {
      this(x,y)
      Console.println(msg)
    }


  def + (other:Point): Point = {
    new Point(this.x+other.x, this.y + other.y)
  }

  override def toString: String = s"Point($x,$y)"

}

val p = new Point(3,3)

a