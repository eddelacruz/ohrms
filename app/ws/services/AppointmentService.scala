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

case class AppointmentList(var id: String, description: String, firstName: String, middleName: String, lastName: String,dentistId: String,contactNo: String, address: String, status: Int, dateStart: String, dateEnd: String)
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
          get[String]("description") ~
          get[String]("first_name") ~
          get[String]("middle_name") ~
          get[String]("last_name") ~
          get[String]("dentist_id") ~
          get[String]("contact_no") ~
          get[String]("address")~
          get[Int]("status")~
          get[Date]("date_start")~
          get[Date]("date_end") map {
          case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j ~ k => AppointmentList(a, b, c, d, e, f, g, h, i, j.toString, k.toString)
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
        //AuditLogService.logTask(d, currentUser, task)
    }
  }
}

