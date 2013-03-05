package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import java.util.Date
import play.api.cache.{EhCachePlugin, Cache}
import ws.helper.DateWithTime
import ws.helper.Maid
import ws.generator.UUIDGenerator
import controllers.Application.Secured

case class AppointmentList(var id: String, dentalServiceId: Option[String], firstName: Option[String], middleName: Option[String], lastName: Option[String],dentistId: Option[String],contactNo: Option[String], address: Option[String], dateStart: Option[String], dateEnd: Option[String])
case class AppointmentDetails(a: AppointmentList, dFirstName: Option[String], dMiddleName: Option[String], dLastName: Option[String], dentalServiceName: Option[String])

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 1/16/13
 * Time: 8:37 AM
 * To change this template use File | Settings | File Templates.
 */

object AppointmentService extends Secured {

  def checkIfDentistIsAvailable(dentistId: String, dateStart: String, dateEnd: String): Long = {
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |SELECT
            |    count(*)
            |FROM
            |    ohrms.appointments
            |where
            |    dentist_id = {dentist_id}
            |    and ({date_start} between date_start and date_end
            |	   and {date_end} between date_start and date_end);
          """.stripMargin).on('dentist_id -> dentistId, 'date_start -> dateStart,'date_end -> dateEnd).as(scalar[Long].single)
    }
  }

  //TODO lagyan kung sino ang nagrecord ng appointment
  def getAllAppointments = {
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |select
            |a.id,
            |a.dental_service_id,
            |a.first_name,
            |a.middle_name,
            |a.last_name,
            |a.dentist_id,
            |a.contact_no,
            |a.address,
            |a.date_start,
            |a.date_end,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |ds.name
            |from appointments as a inner join dental_services as ds inner join dentists as d
            |on a.dental_service_id = ds.id and a.dentist_id = d.id
          """.stripMargin
        ).as {
          get[String]("appointments.id") ~
          get[Option[String]]("dental_service_id") ~
          get[Option[String]]("appointments.first_name") ~
          get[Option[String]]("appointments.middle_name") ~
          get[Option[String]]("appointments.last_name") ~
          get[Option[String]]("appointments.dentist_id") ~
          get[Option[String]]("appointments.contact_no") ~
          get[Option[String]]("appointments.address")~
          get[Date]("date_start")~
          get[Date]("date_end")~
          get[Option[String]]("dentists.first_name")~
          get[Option[String]]("dentists.middle_name")~
          get[Option[String]]("dentists.last_name")~
          get[Option[String]]("dental_services.name") map {
          case a ~ b ~ c ~ d ~ e ~ g ~ h ~ i ~ j ~ k ~ l ~ m ~ n ~ o => AppointmentDetails(AppointmentList(a, b, c, d, e,  g, h, i, Some(j.toString), Some(k.toString)), l, m, n, o)
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
          |dental_service_id,
          |first_name,
          |middle_name,
          |last_name,
          |dentist_id,
          |contact_no,
          |address,
          |date_start,
          |date_end
          |from appointments
          |where id = {id}
        """.stripMargin
        ).on('id -> id).as {
          get[String]("id") ~
            get[Option[String]]("dental_service_id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("dentist_id") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("address")~
            get[Date]("date_start")~
            get[Date]("date_end") map {
            case a ~ b ~ c ~ d ~ e ~ g ~ h ~ i ~ j ~ k => AppointmentList(a, b, c, d, e, g, h, i, Some(j.toString.replace(".0", "")), Some(k.toString.replace(".0", "")))
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
            |dental_service_id,
            |first_name,
            |middle_name,
            |last_name,
            |dentist_id,
            |contact_no,
            |address,
            |date_start,
            |date_end
            |from appointments
            |where
            |date_start
            |BETWEEN {date_now} AND {date_end_of_day}
            |order by date_start, last_name asc;
          """.stripMargin
        ).on('date_now -> DateWithTime.dateNow, 'date_end_of_day -> DateWithTime.dateEndOfDay).as {
            get[String]("id") ~
            get[Option[String]]("dental_service_id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("dentist_id") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("address")~
            get[Date]("date_start") ~
            get[Date]("date_end") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j => AppointmentList(a, b, c, d, e, f, g, h, Some(i.toString.replace(".0", "")), Some(j.toString.replace(".0", "")))
          } *
        }
    }
  }

  def addAppointment(d: AppointmentList): Long = {
    val task = "Add"
    d.id = UUIDGenerator.generateUUID("appointments")
    val currentUser = Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")
    //println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Date Start"+d.dateStart)
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO `ohrms`.`appointments`
            |VALUES
            |(
            |{id},
            |{first_name},
            |{dental_service_id},
            |{middle_name},
            |{last_name},
            |{dentist_id},
            |{contact_no},
            |{address},
            |{date_start},
            |{date_end}
            |);
          """.stripMargin).on(
          'id -> d.id,
          'first_name -> d.firstName,
          'dental_service_id -> d.dentalServiceId,
          'middle_name -> d.middleName,
          'last_name -> d.lastName,
          'dentist_id -> d.dentistId,
          'contact_no -> d.contactNo,
          'address -> d.address,
          'date_start -> d.dateStart,
          'date_end -> d.dateEnd
        ).executeUpdate()
        AuditLogService.logTaskAppointment(d, currentUser, task)
    }
  }

  def updateAppointment(d: AppointmentList): Long = {
    val currentUser = Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE appointments SET
            |dental_service_id = {dental_service_id},
            |first_name = {first_name},
            |middle_name = {middle_name},
            |last_name = {last_name},
            |dentist_id = {dentist_id},
            |contact_no = {contact_no},
            |address = {address},
            |date_start = {date_start},
            |date_end = {date_end}
            |WHERE id = {id}
          """.stripMargin).on(
          'id -> d.id,
          'dental_service_id -> d.dentalServiceId.get,
          'first_name -> d.firstName.get,
          'middle_name -> d.middleName.get,
          'last_name -> d.lastName.get,
          'dentist_id -> d.dentistId.get,
          'contact_no -> d.contactNo.get,
          'address -> d.address.get,
          'date_start -> d.dateStart.get,
          'date_end -> d.dateEnd.get
        ).executeUpdate()
        AuditLogService.logTaskAppointment(d, currentUser, task)
    }
  }

/*  def deleteAppointment(id: String): Long = {
    val currentUser = username
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
       AuditLogService.logTaskDeleteAppointment(id, currentUser, task)
    }
  }*/

}

