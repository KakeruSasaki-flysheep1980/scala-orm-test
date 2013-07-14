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
//        import xerial.lens._
//        val s = workbook.sheets.head
//        val t = ObjectType(s).rawType
//        ObjectSchema(t).methods.foreach(println)

        workbook.sheets.map { case Sheet(sheetName, rows) =>
          val a = rows.map { case Row(index, cells) =>
            println(s"rowIndex: ${index}")
            cells.flatMap { cell =>
              println(s"colIndex: ${cell.index}")
              cell match {
                case c: StringCell => Some(c.data)
                case c: NumericCell => Some(c.data)
                case c: BooleanCell => Some(c.data)
                case c: FormulaCell => Some(c.data)
                case _ => None
              }
            }
          }

          println(a)


//          println(s"sheetName: ${sheetName}")
//
//          rows.map { case Row(index, cells) =>
//            println(s"rowIndex: ${index}")
//
//            cells.map {
//              case c: StringCell => println(c.data)
//              case c: NumericCell => println(c.data)
//              case c: BooleanCell => println(c.data)
//              case c: FormulaCell => println(c.data)
//            }
//          }
        }

        true

//        val book = workbook.asPoi
//        println(s"sheetNumber - ${book.getNumberOfSheets}")
//        book.getSheet("user").getRow(1).getCell(1)
      }
      case -\/(t) => throw t
    }.unsafePerformIO()
  }
}
