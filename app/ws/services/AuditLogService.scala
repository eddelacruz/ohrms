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

  def logTaskDeleteDentist(id: String, currentUser: String, task: String): Long = {
    var description: String = ""
    def getName = {
      DB.withConnection {
        implicit c =>
          val getName = SQL(
            """
              |select
              |first_name
              |from dentists
              |where id = {id}
            """.stripMargin
          ).on('id -> id).apply().head
          getName[String]("first_name")
      }
    }
    task match {
      case "Delete" => description = "Dr. " + getName + "'s profile was deleted"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_name -> currentUser, //cached user_id when login
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,
          'module -> "d"
        ).executeUpdate()
    }
  }

  def logTaskDeleteStaff(id: String, currentUser: String, task: String): Long = {
    var description: String = ""
    def getName = {
      DB.withConnection {
        implicit c =>
          val getName = SQL(
            """
              |select
              |first_name
              |from staffs
              |where id = {id}
            """.stripMargin
          ).on('id -> id).apply().head
          getName[String]("first_name")
      }
    }
    task match {
      case "Delete" => description = "Staff " + getName + "'s profile was deleted"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_name -> currentUser, //cached user_id when login
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,
          'module -> "s"
        ).executeUpdate()
    }
  }

  def logTaskDeletePatient(id: String, currentUser: String, task: String): Long = {
    var description: String = ""
    def getName = {
      DB.withConnection {
        implicit c =>
          val getName = SQL(
            """
              |select
              |first_name
              |from patients
              |where id = {id}
            """.stripMargin
          ).on('id -> id).apply().head
          getName[String]("first_name")
      }
    }
    task match {
      case "Delete" => description = "Patient " +getName + "'s profile was deleted"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,
          'module -> "p",
          'user_name -> currentUser
      ).executeUpdate()
    }
  }

  def logTaskDeleteService(id: String, currentUser: String, task: String): Long = {
    var description: String = ""
    def getName = {
      DB.withConnection {
        implicit c =>
          val getName = SQL(
            """
              |select
              |name
              |from dental_services
              |where id = {id}
            """.stripMargin
          ).on('id -> id).apply().head
          getName[String]("name")
      }
    }
    task match {
      case "Delete" => description = getName + "'s dental_services was deleted"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_name -> currentUser, //cached user_id when login
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,
          'module -> "ds"
        ).executeUpdate()
    }
  }

  def logTaskDeleteSpecialization(id: String, currentUser: String, task: String): Long = {
    var description: String = ""
    def getName = {
      DB.withConnection {
        implicit c =>
          val getName = SQL(
            """
              |select
              |name
              |from specializations
              |where id = {id}
            """.stripMargin
          ).on('id -> id).apply().head
          getName[String]("name")
      }
    }
    task match {
      case "Delete" => description = getName + "was deleted"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_name -> currentUser, //cached user_id when login
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,
          'module -> "sp"
        ).executeUpdate()
    }
  }

  def logTaskDeleteAnnouncement(id: String, currentUser: String, task: String): Long = {
    var description: String = ""
    def getName = {
      DB.withConnection {
        implicit c =>
          val getName = SQL(
            """
              |select
              |announcement
              |from announcements
              |where id = {id}
            """.stripMargin
          ).on('id -> id).apply().head
          getName[String]("announcement")
      }
    }
    task match {
      case "Delete" => description = getName + "was deleted"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_name -> currentUser, //cached user_id when login
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,
          'module -> "an"
        ).executeUpdate()
    }
  }

  def logTaskDeleteAppointment(id: String, currentUser: String, task: String): Long = {
    var description: String = ""
    def getName = {
      DB.withConnection {
        implicit c =>
          val getName = SQL(
            """
              |select
              |first_name
              |from appointments
              |where id = {id}
            """.stripMargin
          ).on('id -> id).apply().head
          getName[String]("first_name")
      }
    }
    task match {
      case "Delete" => description = getName + "'s appointment was deleted"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_name -> currentUser, //cached user_id when login
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,
          'module -> "ap"
        ).executeUpdate()
    }
  }

  def logTaskPatient(l: PatientList, currentUser: String, task: String): Long = {
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,
          'module -> "p",
          'user_name -> currentUser
      ).executeUpdate()
    }
  }

  def logTaskPayment(l: PaymentList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.userName + "Recorded " + l.payment +" payment"
      case "Update" => description = l.userName + "Updated payment "+ l.id
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,//must be date.now "0000-00-00 00:00:00"
          'module -> "py",
          'user_name -> currentUser //cached user_id when login
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,//must be date.now "0000-00-00 00:00:00"
          'module -> "d",
          'user_name -> currentUser //cached user_id when login
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_name -> currentUser, //cached user_id when login
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,//must be date.now "0000-00-00 00:00:00"
          'module -> "c"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,//must be date.now "0000-00-00 00:00:00"
          'module -> "ap",
          'user_name -> currentUser
      ).executeUpdate()
    }
  }

  def logTaskAnnouncement(l: AnnouncementList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.description  + " reminder was added"
      case "Update" => description = l.description  + " reminder was updated"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,//must be date.now "0000-00-00 00:00:00"
          'module -> "re",
          'user_name -> currentUser //cached user_id when login
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
            |(`id`,
            |`task`,
            |`description`,
            |`date_created`,
            |`module`,
            |`user_name`)
            |VALUES
            |(
            |{id},
            |{task},
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,
          'module -> "ds",
          'user_name -> currentUser
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_name -> currentUser, //cached user_id when login
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,//must be date.now "0000-00-00 00:00:00"
          'module -> "s"
        ).executeUpdate()
    }
  }

  def logTaskSpecialization(l:SpecializationList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.name + "'s information was added"
      case "Update" => description = l.name + "'s information was updated"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_name -> currentUser, //cached user_id when login
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,//must be date.now "0000-00-00 00:00:00"
          'module -> "sp"
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
            |	a.user_name = u.user_name
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
            |	a.user_name = u.user_name
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

  def getAuditLogReport(module: String, dateStart: String, dateEnd: String): List[AuditLog] = {
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
            |	a.user_name = u.user_name
            |and (a.date_created BETWEEN {date_start} AND {date_end})
            |ORDER BY
            | a.date_created asc
          """.stripMargin).on( 'date_start -> dateStart, 'date_end -> dateEnd).as {
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


  def logTaskSupply(l: SupplyList, currentUser: String, task: String): Long = {
    var description: String = ""
    task match {
      case "Add" => description = l.name + "'s supply was added"
      case "Update" => description = l.name + "'s supply was updated"
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
            |{description},
            |{date_created},
            |{module},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> UUIDGenerator.generateUUID("audit_log"),
          'task -> task,
          'user_name -> currentUser, //cached user_id when login
          'description -> description.replace("Some", "").replace("(","").replace(")","").replace("Some", "").replace("(","").replace(")",""),
          'date_created -> DateWithTime.dateNow,//must be date.now "0000-00-00 00:00:00"
          'module -> "c"
        ).executeUpdate()
    }
  }
}
