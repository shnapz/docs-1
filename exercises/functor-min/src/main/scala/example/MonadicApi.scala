package example


// Functor, ApplicativeFunctor, Monad

// X <: Y,  Opt[X] < Opt[Y]

sealed trait Opt[+T]
case class Sm[T](value:T) extends Opt[T]
case object Non extends Opt[Nothing]

trait FunctorApiProvider[M[_]]
{

  def mmap[A,B](a:M[A], f: A => B):M[B]

  //
  //def comap[A,B](a:A, f:M[A]=>M[B]):B

}


trait MonadicApiProvider[M[_]] extends FunctorApiProvider[M]
{


  def bind[A,B](a:M[A], f: A => M[B]):M[B]

  //
  def ap[A,B](f:A=>M[B])(a:M[A]):M[B] =
    bind(a,f)

  //
  def ap2[A,B](f:A=>M[B]): M[A]=>M[B] = (x => bind(x,f))

  def unit[A](a:A):M[A]

  //
  def flatten[A](a:M[M[A]]):M[A]

  def unbind[A](a:M[A]):A

  def mmap[A,B](a:M[A], f: A=>B):M[B] =
    bind(a, (x: A) => unit(f(x)))

  // Identity:    m  flatMap unit = m
  // M[a] flatMap (x => M[x}) = M[a]

  //unit(v).flatMap f  = f(v)
  //  M[v] flatMap f :  f(v)

  //m flatMap g flatMap h => m flatMap (x => (g(x)  flatMap h))
  //

}

object Implicits {

implicit class MonadicsOps[A,M[_]](x:M[A])
{

  def flatMap[B](f:A=>M[B])(implicit m:MonadicApiProvider[M])=
    m.bind[A,B](x,f)

  def map[B](f:A=>B)(implicit m:MonadicApiProvider[M]):M[B] =
    m.mmap(x,f)

  def unbind()(implicit m:MonadicApiProvider[M]):A =
    m.unbind(x)

  //def <*>[B] (f: A=>B)(implicit m:MonadicApiProvider[M]):M[B] =
  //

}

// f: A => B,  g: B=>C ,  gf : A=>C
// f: A => M[B],  g: B => M[C]
//       for( a <- init;
//            b <- f(a)
//            c <- g(b) ) yield c
//      a flatMap ( for(b <- f(a); c<-g(b) ) yield c
//      init flatMap (f(a) flatMap (.. )

implicit object OptMonadicApiProvider extends MonadicApiProvider[Opt]
{

  def bind[A,B](a:Opt[A], f: A => Opt[B]):Opt[B] =
    a match {
      case Sm(v) => f(v)
      case Non => Non
    }

  def unit[A](a:A):Opt[A] = Sm(a)

  def unbind[A](a:Opt[A]):A =
    a match {
      case Sm(v) => println(v)
                    v
      case Non => throw new RuntimeException("Empty ")
    }

  def flatten[A](a:Opt[Opt[A]]):Opt[A] =
    a match {
      case Sm(v) => v
      case Non => Non
    }

  {

    //val a = x.get
    //val b = a+1

    // val b = (x map (_ +1)).get

  }

}

}


trait Config[M[_]]
{

  def get[T](name:String)(implicit m:MonadicApiProvider[M]): M[T]


}

/*
trait Kleisli[M[_],A,B]
{

  def apply(a:A):M[B]


}
*/



object ConfigClient
{

  import Implicits._

  def retrieveSum(config:Config[Opt],xn:String, yn:String): Int =
  {
    val z = for( x <- config.get[Int](xn);
                 y <- config.get[Int](yn))  yield x+y

    z.unbind()

  }



}


//(Int,Int) map (_ + 1)
