package ws.generator

import java.util.UUID
import play.api.db.DB
import play.api.Play.current
import anorm.SQL

object UUIDGenerator {
  def generateUUID(tableName: String): String = {
    val id = UUID.randomUUID.toString
    var status = None : Option[Long]
    //val id = "a5434d36-dd94-425e-87a3-3c859db7b130"
    DB.withConnection {
      implicit c =>
        status = SQL(
          """
            SELECT COUNT(*) AS c
            FROM """ + tableName + """
            WHERE id = {id}
          """).on('id -> id).apply.head[Option[Long]]("c")
    }
    status match{
      case Some(0) => id
      case _ => generateUUID(tableName)
    }
  }
}