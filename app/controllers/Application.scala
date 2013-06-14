package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
//    println(models.User.findByName("hoge"))
    Ok(views.html.index("Your new application is ready."))
  }
  
}
