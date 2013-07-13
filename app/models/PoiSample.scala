package models

object PoiSample {

  import info.folone.scala.poi._
  import scalaz._
  import syntax.monoid._
  import syntax.equal._
  import syntax.foldable._
  import std.list._

  def read = {
//    val file = new java.io.File("")
    val path = "sample.xls"
    Workbook(path).map {
      case \/-(workbook) => {
        workbook.sheets
      }
      case -\/(t) => throw t
    }.unsafePerformIO()

  }
}
