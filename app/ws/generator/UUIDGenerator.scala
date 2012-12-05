package ws.generator

import java.util.UUID
import play.api.db.DB
import play.api.Play.current
import anorm.SQL

object UUIDGenerator {
  def generateUUID(tableName: String): String = {
    val id = UUID.randomUUID().toString
    DB.withConnection {
      implicit c =>
        if(SQL(
          """
            select count(*) as c
            from """ + tableName + """
            where id = {id}
          """).on("id" -> id).apply.head == "0"){
          id
        } else {
          generateUUID(tableName)
        }
    }
  }
}