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
    val user = Cache.getAs[String]("user_name").toString
    val username =  user.replace("Some", "").replace("(","").replace(")","")
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

