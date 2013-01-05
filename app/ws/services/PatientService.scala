package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import java.util.Date
import ws.helper.DateWithTime
import ws.generator.UUIDGenerator

case class PatientList(var id: String, firstName: String, middleName: String, lastName: String, medicalHistoryId: String, address: String, contactNo: String, dateOfBirth: String, image: String)

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/4/12
 * Time: 11:53 PM
 * To change this template use File | Settings | File Templates.
 */

object PatientService {

  def getPatientList(start: Int, count: Int): List[PatientList] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val patientList: List[PatientList] = SQL(
          """
            |select
            |id,
            |first_name,
            |middle_name,
            |last_name,
            |medical_history_id,
            |address,
            |contact_no,
            |date_of_birth,
            |image
            |from
            |patients
            |where status = {status}
            |ORDER BY last_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'start -> start, 'count -> count).as {
            get[String]("id") ~
            get[String]("first_name") ~
            get[String]("middle_name") ~
            get[String]("last_name") ~
            get[String]("medical_history_id") ~
            get[String]("address") ~
            get[String]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[String]("image") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i => PatientList(a, b, c, d, e, f, g, h.toString, i)
          } *
        }
        patientList
    }
  }

  def getPatientListById(id: String): List[PatientList] = {
    DB.withConnection {
      implicit c =>
        val patientList: List[PatientList] = SQL(
          """
            |select
            |id,
            |first_name,
            |middle_name,
            |last_name,
            |medical_history_id,
            |address,
            |contact_no,
            |date_of_birth,
            |image
            |from
            |patients
            |where
            |id = {id} AND
            |status = 1
          """.stripMargin).on('id -> id).as {
          get[String]("id") ~
            get[String]("first_name") ~
            get[String]("middle_name") ~
            get[String]("last_name") ~
            get[String]("medical_history_id") ~
            get[String]("address") ~
            get[String]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[String]("image") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i => PatientList(a, b, c, d, e, f, g, h.toString, i)
          } *
        }
        patientList
    }
  }

  def addPatient(p: PatientList): Long = {
    val currentUser = "c7e5ef5d-07eb-4904-abbe-0aa73c13490f" //static cvbautista
    val task = "Add"
    p.id = UUIDGenerator.generateUUID("patients")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO `ohrms`.`patients`
            |VALUES
            |(
            |{id},
            |{first_name},
            |{middle_name},
            |{last_name},
            |{medical_history_id},
            |{address},
            |{contact_no},
            |{date_of_birth},
            |{image},
            |{status},
            |{date_created},
            |{date_last_updated}
            |);
          """.stripMargin).on(
          'id -> p.id,
          'first_name -> p.firstName,
          'middle_name -> p.middleName,
          'last_name -> p.lastName,
          'medical_history_id -> p.medicalHistoryId,
          'address -> p.address,
          'contact_no -> p.contactNo,
          'date_of_birth -> p.dateOfBirth,
          'image -> p.image,
          'status -> 1,
          'date_created -> DateWithTime.dateNow,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
        AuditLogService.logTask(p, currentUser, task) //TODO cached user_id when login
    }

  }

  def updatePatient(p: PatientList): Long = {
    val currentUser = "c7e5ef5d-07eb-4904-abbe-0aa73c13490f" //TODO static cvbautista
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE patients SET
            |first_name = {first_name},
            |middle_name = {middle_name},
            |last_name = {last_name},
            |address = {address},
            |contact_no = {contact_no},
            |date_of_birth = {date_of_birth},
            |image = {image},
            |date_last_updated = {date_last_updated}
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> p.id,
          'first_name -> p.firstName,
          'middle_name -> p.middleName,
          'last_name -> p.lastName,
          'address -> p.address,
          'contact_no -> p.contactNo,
          'date_of_birth -> p.dateOfBirth,
          'image -> p.image,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
        AuditLogService.logTask(p, currentUser, task) //TODO cached user_id when login
    }

  }

  def deletePatient(id: String): Long = {
    val currentUser = "c7e5ef5d-07eb-4904-abbe-0aa73c13490f" //static cvbautista
    val task = "Delete"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE patients SET
            |status = {status},
            |date_last_updated = {date_last_updated}
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> id,
          'status -> 0,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
        AuditLogService.logTask(id, currentUser, task)
    }
  }

}
