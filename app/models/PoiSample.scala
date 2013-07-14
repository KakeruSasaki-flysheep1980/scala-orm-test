package models

object PoiSample {

  import info.folone.scala.poi._
  import scalaz._
  import syntax.monoid._
  import syntax.equal._
  import syntax.foldable._
  import std.list._

  def read = {
    val path = "sample.xls"
//    val s = Workbook(path).map {
//      case \/-(workbook) => {
//        workbook.sheets
//      }
//      case -\/(t) => throw t
//    }.unsafePerformIO().seq.head
//    println(s"sheetName - ${s.name}")

    Workbook(path).map {
      case \/-(workbook) => {
        val book = workbook.asPoi
        println(s"sheetNumber - ${book.getNumberOfSheets}")
        book.getSheet("user").getRow(1).getCell(1)
      }
      case -\/(t) => throw t
    }.unsafePerformIO()
  }
}
