package models

object CircumflexSample {

  import pro.savant.circumflex.orm._
  import pro.savant.circumflex.core.Circumflex

  def main(args: Array[String]) {
    // database configuration
    val cx = Circumflex
    cx("orm.connection.driver") = "org.h2.Driver"
    cx("orm.connection.url") = "jdbc:h2:mem:play"
    cx("orm.connection.username") = "sa"
    cx("orm.connection.password") = ""

    // create table
    val ddl = new DDLUnit(User)
    println(User.sqlCreate)
    ddl.CREATE()

    // insert
    val hoge = new User(name = "__hoge__")
    val insertedId = (hoge).save()
    println("insertedId: %s".format(insertedId))
    println((new User(name = "__fuga__")).save())

    // select
    User.get(insertedId).foreach(u => println(u)) // findById
    User.findByName("__hoge__").foreach(println)  // findByName
    User.criteria.add(User.name EQ "__hoge__").list().foreach(println) // findByName
    println(User.criteria.add(User.name EQ "__hoge__").toSql)
    println(User.criteria.add(User.name EQ "__hoge__").toString())

    // update
    hoge.name := "__piyo__"
    val updatedId = hoge.save()
    println("updatedId: %s".format(updatedId))
    User.get(updatedId).foreach(u => println(u))

    // delete
    val deletedId = hoge.DELETE_!()

    println(User.get(deletedId))
  }

  class User extends Record[Long, User] with IdentityGenerator[Long, User] {
    val id = "id".BIGINT.NOT_NULL.AUTO_INCREMENT
    val name = "name".VARCHAR(20).NOT_NULL
    val createdTime = "created_time".TIMESTAMP.NOT_NULL
    val updatedTime = "updated_time".TIMESTAMP.NOT_NULL

    def PRIMARY_KEY = id
    def relation = User

    def this(name: String) = {
      this()
      this.name := name
    }

    // for debug print
    override def toString: String = {
      Seq(
        this.id(),
        this.name(),
        this.createdTime(),
        this.updatedTime()
      ).mkString(",")
    }
  }
  object User extends User with Table[Long, User] {

    override def beforeInsert: Seq[(User) => Unit] = {
      val func: User => Unit = { user: User =>
        user.createdTime := new java.util.Date
        user.updatedTime := new java.util.Date
      }
      Seq(func)
    }

    def findByName(name: String) = {
      (this AS "u").map { u =>
        SELECT (u.*) FROM (u) WHERE (u.name.EQ(name)) ORDER_BY (u.id ASC)
      }.list()
    }

    def findWithPagination(limit: Int, offset: Int) = {
      (this AS "u").map { u =>
        SELECT (u.*) FROM (u) ORDER_BY (u.id ASC) LIMIT(5) OFFSET(10)
      }.list()
    }
  }

}
