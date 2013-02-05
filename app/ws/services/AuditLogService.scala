package ws.services

import ws.generator.UUIDGenerator
import play.api.db.DB
import anorm._
import play.api.Play.current
import anorm.SqlParser._
import anorm.~
import java.util.Date
import ws.helper.DateWithTime

case class AuditLog(var id: String, task: String, description: String, dateCreated: String, author: String)

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
      case "Update" => description = l.firstName +" "+ l.lastName + "'s profile was updated"
      case _ => ""
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO audit_log
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
          'date_created -> DateWithTime.dateNow
        ).executeUpdate()
    }
  }

  def logTask(id: String, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Delete" => description = id + " profile was deleted"
      case _ => ""
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO audit_log
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
          'date_created -> DateWithTime.dateNow
        ).executeUpdate()
    }
  }

  def logTaskOther(id: String, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Delete" => description = id + " information was deleted"
      case _ => ""
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO audit_log
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
          'date_created -> DateWithTime.dateNow
        ).executeUpdate()
    }
  }

  def logTaskDentist(l: DentistList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.firstName +" "+ l.lastName + "'s profile was added"
      case "Update" => description = l.firstName +" "+ l.lastName + "'s profile was updated"
      case _ => ""
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO audit_log
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
          'date_created -> DateWithTime.dateNow//must be date.now "0000-00-00 00:00:00"
        ).executeUpdate()
    }
  }

  def logTaskClinic(l: ClinicList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.clinicName + "'s clinic was added"
      case "Update" => description = l.clinicName + "'s clinic was updated"
      case _ => ""
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO audit_log
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
          'date_created -> DateWithTime.dateNow//must be date.now "0000-00-00 00:00:00"
        ).executeUpdate()
    }
  }

  def logTaskAppointment(l: AppointmentList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.firstName +" "+ l.lastName + "'s appointment was added"
      case "Update" => description = l.firstName +" "+ l.lastName  + "'s appointment was updated"
      case _ => ""
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO audit_log
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
          'date_created -> DateWithTime.dateNow//must be date.now "0000-00-00 00:00:00"
        ).executeUpdate()
    }
  }

  def logTaskAnnouncement(l: AnnouncementList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.announcement  + "'s announcement was added"
      case "Update" => description = l.announcement  + "'s announcement was updated"
      case _ => ""
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO audit_log
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
          'date_created -> DateWithTime.dateNow//must be date.now "0000-00-00 00:00:00"
        ).executeUpdate()
    }
  }

  def logTaskServices(l: DentalServiceList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.name + "'s service was added"
      case "Update" => description = l.name + "'s service was updated"
      case _ => ""
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO audit_log
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
          'date_created -> DateWithTime.dateNow//must be date.now "0000-00-00 00:00:00"
        ).executeUpdate()
    }
  }

  def logTaskStaff(l: StaffList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.firstName +" "+ l.lastName + "'s profile was added"
      case "Update" => description = l.firstName +" "+ l.lastName + "'s profile was updated"
      case _ => ""
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO audit_log
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
          'date_created -> DateWithTime.dateNow//must be date.now "0000-00-00 00:00:00"
        ).executeUpdate()
    }
  }



  def getAllLogs(start: Int, count: Int): List[AuditLog] = {
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
            |ORDER BY
            | a.date_created desc
            |LIMIT {start}, {count}
          """.stripMargin).on('start -> start, 'count -> count).as{
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

  def searchAuditLog(start: Int, count: Int, filter: String): List[AuditLog] = {
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
            |WHERE a.task like "%"{filter}"%"
            |or a.description like "%"{filter}"%"
            |ORDER BY
            | a.date_created desc
            |LIMIT {start}, {count}
          """.stripMargin).on('filter -> filter, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[String]("task") ~
            get[String]("description") ~
            get[Date]("date_created") ~
            get[String]("user_name")map {
            case a ~ b ~ c ~ d ~ e  => AuditLog(a, b, c, d.toString, e)
          } *
        }
        auditLog
    }
  }

}
