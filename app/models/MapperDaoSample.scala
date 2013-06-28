package models

import java.util.Date
import com.googlecode.mapperdao.{ DeclaredIds, NaturalLongId, Entity, ValuesMap }
import com.googlecode.mapperdao.utils.{All, TransactionalCRUD, Setup}
import play.api.db.DB
import play.api.Play.current
import com.googlecode.mapperdao.Query._

object MapperDaoSample {

  trait BaseModel extends Sequenced with RecordedTime

  trait Sequenced {
    val id: Long
  }

  trait RecordedTime {
    val createdTime: Date
    val updatedTime: Date
  }

  // ドメインモデル
  case class User(id: Long, name: String, createdTime: Date, updatedTime: Date) extends BaseModel

  trait BaseEntity[PC <: DeclaredIds[Long], T <: BaseModel] extends Entity[Long, PC, T] {
    val id = key("id") to (_.id)
    val createdTime = column("created_time") to (_.createdTime)
    val updatedTime = column("updated_time") to (_.updatedTime)
  }

  // ドメインモデルとデータベースのマッピングを責務とするクラス
  // テーブル名, 列名はここで定義可能
  // ID列をauto_incrementするか否かはNaturalLongIdを指定するかどうかってことかな??
  object UserEntity extends BaseEntity[NaturalLongId, User] {
    val name = column("name") to (_.name)
    def constructor(implicit m: ValuesMap) = new User(id, name, createdTime, updatedTime) with NaturalLongId
  }

  object Daos {

    // データベース設定を読み込む
    // TODO BaseEntityを実装しているクラスを動的に取得してもいいかも??
    private val (jdbc, md, qd, txm) = Setup.h2(DB.getDataSource(), List(UserEntity))

    // mapperdaoはDDL発行はしてくれない??
    // evolutionで作成する -> play run -> ページアクセスで作成可
    trait BaseDao[PC <: DeclaredIds[Long], T <: BaseModel] extends TransactionalCRUD[Long, PC, T] with All[Long, PC, T] {
      protected val queryDao = qd
      protected val mapperDao = md
      protected val txManager = txm

      protected val entity: BaseEntity[PC, T]
    }

    // DAO for User
    class UserDao extends BaseDao[NaturalLongId, User] {
      protected val entity = UserEntity
      def findByName(name: String): List[User] = queryDao.query(select from entity where entity.name === name orderBy entity.id)
    }

  }

}
