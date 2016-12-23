

1.  Please, read article about try/defer statement in Go programming language:
                         https://blog.golang.org/defer-panic-and-recover
                     
2.  Implement something maximum simular in scala. (i.e. write library)

3.  Example - receive something like:

```
object Main
{

  def main(args:Array[String]):Unit =
  {
   if (args.length < 3) {
     System.err.println("usage: copy in out");
   }
   copy(new File(args(1)), new File(args(2)))
  }
  
  def copy(inf: File, outf: File): Long =
   withDefer { defer =>
    val in = new FileInputStream(inf)
    defer{ in.close() }
    val out = new FileOutputStream(outf);
    defer{ out.close() }
    out.getChannel() transferFrom(in.getChannel(), 0, Long.MaxValue)
  }


}

```
