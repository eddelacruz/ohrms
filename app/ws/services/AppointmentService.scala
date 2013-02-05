package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import java.util.Date
import play.api.cache.{EhCachePlugin, Cache}
import ws.helper.DateWithTime
import ws.generator.UUIDGenerator
import controllers.Application.Secured

case class AppointmentList(var id: String, description: Option[String], firstName: Option[String], middleName: Option[String], lastName: Option[String],dentistId: Option[String],contactNo: Option[String], address: Option[String], status: Option[Int], dateStart: Option[String], dateEnd: Option[String])
/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 1/16/13
 * Time: 8:37 AM
 * To change this template use File | Settings | File Templates.
 */

object AppointmentService extends Secured {

  def getUserId = {
    val username =  Cache.getAs[String]("user_name").get
    DB.withConnection {
      implicit c =>
        val getCurrentUserId = SQL(
          """
            |select
            |id
            |from users
            |where user_name = {username}
          """.stripMargin
        ).on('username -> username).apply().head
        getCurrentUserId[String]("id")
    }
  }

  //TODO lagyan kung sino ang nagrecord ng appointment
  def getAllAppointments = {
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |select
            |id,
            |description,
            |first_name,
            |middle_name,
            |last_name,
            |dentist_id,
            |contact_no,
            |address,
            |status,
            |date_start,
            |date_end
            |from appointments
          """.stripMargin
        ).as {
          get[String]("id") ~
          get[Option[String]]("description") ~
          get[Option[String]]("first_name") ~
          get[Option[String]]("middle_name") ~
          get[Option[String]]("last_name") ~
          get[Option[String]]("dentist_id") ~
          get[Option[String]]("contact_no") ~
          get[Option[String]]("address")~
          get[Option[Int]]("status")~
          get[Date]("date_start")~
          get[Date]("date_end") map {
          case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j ~ k => AppointmentList(a, b, c, d, e, f, g, h, i, Some(j.toString), Some(k.toString))
        } *
      }
    }
  }

  def getAppointmentById(id: String) : List[AppointmentList] = {
    DB.withConnection {
      implicit c =>
      val appointmentList: List[AppointmentList] = SQL(
        """
          |select
          |id,
          |description,
          |first_name,
          |middle_name,
          |last_name,
          |dentist_id,
          |contact_no,
          |address,
          |status,
          |date_start,
          |date_end
          |from appointments
          |where id = {id}
        """.stripMargin
        ).on('id -> id).as {
          get[String]("id") ~
            get[Option[String]]("description") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("dentist_id") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("address")~
            get[Option[Int]]("status")~
            get[Option[Date]]("date_start")~
            get[Option[Date]]("date_end") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j ~ k => AppointmentList(a, b, c, d, e, f, g, h, i, Some(j.toString), Some(k.toString))
          } *
        }
      appointmentList
    }
  }

  def getAppointmentsToday = {
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |select
            |id,
            |description,
            |first_name,
            |middle_name,
            |last_name,
            |dentist_id,
            |contact_no,
            |address,
            |status,
            |date_start,
            |date_end
            |from appointments
            |where
            |  DATE(date_start) = DATE({date_only})
            |or
            |DATE(date_end) = DATE({date_only})
            |order by date_start asc;
          """.stripMargin
        ).on('date_only -> DateWithTime.dateOnly).as {
          get[String]("id") ~
            get[Option[String]]("description") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("dentist_id") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("address")~
            get[Option[Int]]("status")~
            get[Option[Date]]("date_start")~
            get[Option[Date]]("date_end") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j ~ k => AppointmentList(a, b, c, d, e, f, g, h, i, Some(j.toString), Some(k.toString))
          } *
        }
    }
  }

  def addAppointment(d: AppointmentList): Long = {
    println(getUserId)
    val currentUser = getUserId
    val task = "Add"
    d.id = UUIDGenerator.generateUUID("appointments")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO `ohrms`.`appointments`
            |VALUES
            |(
            |{id},
            |{description},
            |{first_name},
            |{middle_name},
            |{last_name},
            |{dentist_id},
            |{contact_no},
            |{address},
            |{status},
            |{date_start},
            |{date_end}
            |);
          """.stripMargin).on(
          'id -> d.id,
          'description -> d.description,
          'first_name -> d.firstName,
          'middle_name -> d.middleName,
          'last_name -> d.lastName,
          'dentist_id -> d.dentistId,
          'contact_no -> d.contactNo,
          'address -> d.address,
          'status -> d.status,
          'date_start -> d.dateStart,
          'date_end -> d.dateEnd
        ).executeUpdate()
        AuditLogService.logTaskAppointment(d, currentUser, task)
    }
  }

  def updateAppointment(d: AppointmentList): Long = {
    println(getUserId)
    val currentUser = getUserId
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE appointments SET
            |description = {description},
            |first_name = {first_name},
            |middle_name = {middle_name},
            |last_name = {last_name},
            |dentist_id = {dentist_id},
            |contact_no = {contact_no},
            |address = {address},
            |status = {status},
            |date_start = {date_start},
            |date_end = {date_end}
            |WHERE id = {id}
          """.stripMargin).on(
          'id -> d.id,
          'description -> d.description,
          'first_name -> d.firstName,
          'middle_name -> d.middleName,
          'last_name -> d.lastName,
          'dentist_id -> d.dentistId,
          'contact_no -> d.contactNo,
          'address -> d.address,
          'status -> d.status,
          'date_start -> d.dateStart,
          'date_end -> d.dateEnd
        ).executeUpdate()
        AuditLogService.logTaskAppointment(d, currentUser, task)
    }
  }

  def deleteAppointment(id: String): Long = {
    val currentUser = getUserId
    val task = "Delete"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE appointments SET
            |status = {status}
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> id,
          'status -> 0
        ).executeUpdate()
      //  AuditLogService.logTask(id, currentUser, task)
    }
  }

}

