package models

object ActivateSample {

  import net.fwbrasil.activate.ActivateContext
  import net.fwbrasil.activate.storage.memory.TransientMemoryStorage
  import net.fwbrasil.activate.entity.Entity
  import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
  import net.fwbrasil.activate.storage.relational.idiom.mySqlDialect

  object MyContext extends ActivateContext {
    val storage = new TransientMemoryStorage
  }

  object HogeContext extends ActivateContext {
    val storage = new PooledJdbcRelationalStorage {
      val dialect = mySqlDialect
      val url = "jdbc:mysql://127.0.0.1/xxx"
      val user = "user"
      val password = "pass"
      val jdbcDriver = "com.mysql.jdbc.Driver"
    }
  }

  def main(args: Array[String]) {
    import MyContext._

    println(all[User])

    transactional {
      new User("__hoge__")
    }

    transactional {
      println(all[User])

      all[User].headOption.map {user =>
        println("user.id = %s".format(user.id))
        println("user.name = %s".format(user.name))
        println("user.creationDate = %s".format(user.creationDate))
        println("user.creationDateTime = %s".format(user.creationDateTime))
        println("user.creationTimestamp = %s".format(user.creationTimestamp))
      }
    }

    transactional {
      val hoge = new User("__hoge__")

      byId[User](hoge.id) // => Option[User]
      println(byId[User](hoge.id))

      all[User] // List[User]
      println(all[User])

      select[User] where(_.name :== "__hoge__") // List[User]
      println(select[User] where(_.name :== "__hoge__"))
    }

    println("*** update/delete ***")
    transactional {
      println(select[User] where(_.name :== "__piyo__")) // => List()
    }

    transactional {
      // update
      all[User].headOption.map { user =>
        user.name = "__piyo__"
      }
    }

    transactional {
      println(select[User] where(_.name :== "__piyo__")) // => List(User(...))
    }

    transactional {
      // delete
      val user = (select[User] where(_.name :== "__piyo__")).head
      user.delete
    }

    transactional {
      println(select[User] where(_.name :== "__piyo__")) // => List()
    }
  }

  class User(var name: String) extends Entity

}
