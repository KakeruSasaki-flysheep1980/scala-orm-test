package models

import xerial.lens._

case class SomeModel(id: Long, name: String)

class ReflectionSample {

  def findById(id: Long): Option[SomeModel] = {
    val os = ObjectSchema(classOf[ReflectionSample])
    val method = os.methods.find(_.name == "findById").get
    method.params.map(_.name).foreach(println)








    None
  }

  def findByName(name: String): Option[SomeModel] = {
    None
  }

}
