package ws.services

import ws.generator.UUIDGenerator
import play.api.db.DB
import anorm._
import play.api.Play.current
import anorm.SqlParser._
import anorm.~
import java.util.Date
//import org.joda.time._

case class AuditLog(id: String, task: String, description: String, dateCreated: String, author: String)

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 12/10/12
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
object AuditLogService {

  def logTask(l: PatientList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.firstName +" "+ l.lastName + "'s profile was added"
      case _ => ""
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO `ohrms`.`audit_log`
            |VALUES
            |(
            |{id},
            |{task},
            |{user_id},
            |{description},
            |{date_created}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_id -> currentUser, //cached user_id when login
          'description -> description,
          'date_created -> "0000-00-00 00:00:00" //DateTime.now()//must be date.now "0000-00-00 00:00:00"
        ).executeUpdate()
    }
  }

  def getAllLogs(): List[AuditLog] = {
    DB.withConnection {
      implicit c =>
        val auditLog: List[AuditLog] = SQL(
          """
            |SELECT
            | a.id,
            |	a.task,
            |	a.description,
            |	a.date_created,
            |	u.user_name
            |FROM
            |    audit_log as a
            |INNER JOIN
            |    users as u
            |ON
            |	a.user_id = u.id
          """.stripMargin).as{
            get[String]("id") ~
            get[String]("task") ~
            get[String]("description") ~
            get[Date]("date_created") ~
            get[String]("user_name") map {
            case a~b~c~d~e => AuditLog(a,b,c,d.toString,e)
          }*
        }
        auditLog
    }

  }

}
