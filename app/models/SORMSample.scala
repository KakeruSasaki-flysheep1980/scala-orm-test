package models

object SORMSample {

  import sorm._

  def main(args: Array[String]) {
    // insert
    val hoge = Db.save(User("__hoge__"))
    println(hoge)
    val fuga = Db.save(User("__fuga__"))
    println(fuga)

    // select
    // findByName
    println(Db.query[User].whereEqual("name", "__hoge__").fetchOne())
    // findById
    println(Db.query[User].whereEqual("id", 2L).fetchOne()) // Option
    println(Db.fetchById[User](2L)) // 無い場合はNoSuchElementException

    val selected = Db.query[User].whereEqual("name", "__hoge__").fetchOne().get
    println(selected.id)

    // update
    val updated = Db.save(selected.copy(name = "__piyo__"))
    println(updated)

    // delete
    Db.delete(updated)
    println(Db.query[User].whereEqual("name", "__hoge__").fetchOne())

    val users = Db.query[User]
      .whereLike("name", "hoge")  // like hoge
      .offset(1)  // offset 1
      .limit(3) // limit 3
      .order("id", false) // order by id desc
      .fetch()

    {
      import sorm.Dsl._
      val users = Db.query[User]
        .where(
          (
            ( "name" equal "__hoge__" ) or ("name" equal "__fuga__") and
            ("id" notEqual 1L)
          )
        )
        .fetch()
    }

    {
      println("****** transaction test ******")

      def printlnWithThread(str: Any) = {
        println("[%s]%s".format(Thread.currentThread().getName, str))
      }

      // transaction
//      val testUser = Db.save(User("__transaction_test__"))
//      val testUser2 = Db.save(User("__transaction_test2__"))
//      printlnWithThread(Db.fetchById[User](testUser.id))

//      // 別スレッドで更新処理
//      scala.actors.Actor.actor {
//        scala.util.control.Exception.allCatch.either {
//          Db.transaction {
//            val testUser = Db.save(User("__transaction_test__"))
//            //          val updated = Db.save(testUser.copy(name = "__hoge__"))
//            //          printlnWithThread(updated)
//            printlnWithThread(testUser)
//            Thread.sleep(5000L) // wait 5s
//            throw new RuntimeException
//          }
//        }
//      }
//
////      Thread.sleep(3000L) // wait 3s
////      printlnWithThread(Db.fetchById[User](testUser2.id))
////      printlnWithThread(Db.fetchById[User](testUser.id))
//      printlnWithThread(Db.query[User].whereEqual("name", "__transaction_test__").fetchOne())
//      Thread.sleep(5000L) // wait 3s
//      printlnWithThread(Db.query[User].whereEqual("name", "__transaction_test__").fetchOne())

      {
        println("****** raw sql test ******")
        Db.save(User("__hoge__"))
        Db.save(User("__hoge__"))
        val users = Db.fetchWithSql[User]("SELECT id FROM user WHERE name = ?", "__hoge__")
        println("users: " + users)
      }
    }
  }

  // エンティティ実装
  // idはORMが勝手に管理してる
  // 自分でidを発行したい場合は別途その列を用意しないとならない
  case class User(name: String)

  object Db extends Instance (
    // 管理してるテーブルを羅列
    entities = Set() + Entity[User](unique = Set() + Seq("name")),

    // データベースの接続設定
    url = "jdbc:h2:mem:play",
    user = "sa",
    password = "",

    // 起動時の処理(Create = テーブル作成)
    initMode = InitMode.Create,

    // コネクション数
    poolSize = 3
  )

}
