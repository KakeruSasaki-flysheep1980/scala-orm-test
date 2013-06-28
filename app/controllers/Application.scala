package controllers

import play.api._
import play.api.mvc._
import java.util.Date

object Application extends Controller {
  
  def index = Action {
    import models.MapperDaoSample.Daos.UserDao
    import models.MapperDaoSample.User

    val now = new Date
    val userDao = new UserDao

    val created = userDao.create(new User(2L, "__name__", now, now))
    println("======== created: %s".format(created))

    val retrieved = userDao.retrieve(2L)
    println("======== retrieved: %s".format(retrieved))

    println("======== findByName: %s".format(userDao.findByName("__name__")))

    val updated = userDao.update(retrieved.get, new User(2L, "__updated__", now, now))
    println("======== updated: %s".format(updated))

    userDao.delete(2L)
    println("======== retrieved: %s".format(userDao.retrieve(2L)))

    Ok(views.html.index("Your new application is ready."))
  }
  
}
