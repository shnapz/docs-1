package defer

import org.scalatest.{BeforeAndAfterEach, FunSuite}

import scala.util.Try

/**
  * Created by v-igsyro on 12/26/2016.
  */
class package$Test extends FunSuite with BeforeAndAfterEach{

  class FileMoq(val path: String)

  abstract class FileStreamMoq(val file: FileMoq) {
    var isClosed = false
    def close() = { isClosed = true; } //Simulate closing and log action for test purpose
  }

  val CloseInputStreamLogMsg = "Close input stream"
  val CloseOutputStreamLogMsg = "Close output stream"

  class FileInputStreamMoq(file: FileMoq) extends FileStreamMoq(file: FileMoq){
    override def close(): Unit = super.close(); actionCallsLog = CloseInputStreamLogMsg :: actionCallsLog }

  class FileOutputStream(file: FileMoq) extends FileStreamMoq(file: FileMoq){
    override def close(): Unit = super.close(); actionCallsLog = CloseOutputStreamLogMsg :: actionCallsLog }

  var in: FileInputStreamMoq = null
  var out: FileOutputStream = null
  var actionCallsLog: List[String] = List()

  def simulateCopy(inf: FileMoq, outf: FileMoq) =
    withDefer { defer =>
      in = new FileInputStreamMoq(inf)
      defer{ in.close() }
      out = new FileOutputStream(outf)
      defer{ out.close() }
    }

  def simulateCopy2(inf: FileMoq, outf: FileMoq) =
    withDefer { defer =>
      in = new FileInputStreamMoq(inf)
      defer{ in.close() }
      throw new RuntimeException("Be-be-be")
      out = new FileOutputStream(outf)
      defer{ out.close() }
    }



  override def beforeEach(): Unit = {
    in = null
    out = null
    actionCallsLog = List()
  }

  test("An input file is closed") {
    simulateCopy(new FileMoq("//fake/path/to/inputFile"), new FileMoq("//fake/path/to/outputFile"))
    assert(in.isClosed)
  }

  test("An output file is closed") {
    simulateCopy(new FileMoq("//fake/path/to/inputFile"), new FileMoq("//fake/path/to/outputFile"))
    assert(out.isClosed)
  }

  test("An output stream is closed firstly"){
    simulateCopy(new FileMoq("//fake/path/to/inputFile"), new FileMoq("//fake/path/to/outputFile"))
    assert(actionCallsLog(0) === CloseOutputStreamLogMsg)
  }

  test("An input stream is closed lastly"){
    simulateCopy(new FileMoq("//fake/path/to/inputFile"), new FileMoq("//fake/path/to/outputFile"))
    assert(actionCallsLog(1) === CloseInputStreamLogMsg)
  }

  test("When we have exception, input stream must be close") {
    Try(
      simulateCopy2(new FileMoq("//fake/path/to/inputFile"), new FileMoq("//fake/path/to/outputFile"))
    )
    assert(actionCallsLog(0) === CloseInputStreamLogMsg)
  }

}
