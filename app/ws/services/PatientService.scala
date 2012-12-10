package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import java.util.Date
import ws.generator.UUIDGenerator

case class PatientList(var id: String, firstName: String, middleName: String, lastName: String, medicalHistoryId: String, address: String, contactNo: String, dateOfBirth: String, image: String)

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/4/12
 * Time: 11:53 PM
 * To change this template use File | Settings | File Templates.
 */

object PatientService{

  def getPatientList: List[PatientList] = {
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
          """.stripMargin).on('status -> status).as{
          get[String]("id") ~
          get[String]("first_name") ~
          get[String]("middle_name") ~
          get[String]("last_name") ~
          get[String]("medical_history_id") ~
          get[String]("address") ~
          get[String]("contact_no") ~
          get[Date]("date_of_birth") ~
          get[String]("image") map {
            case a~b~c~d~e~f~g~h~i => PatientList(a,b,c,d,e,f,g,h.toString,i)
          }*
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
            |id = {id}
          """.stripMargin).on('id -> id).as{
            get[String]("id") ~
            get[String]("first_name") ~
            get[String]("middle_name") ~
            get[String]("last_name") ~
            get[String]("medical_history_id") ~
            get[String]("address") ~
            get[String]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[String]("image") map {
            case a~b~c~d~e~f~g~h~i => PatientList(a,b,c,d,e,f,g,h.toString,i)
          }*
        }
        patientList
    }
  }

  def addPatient(p: PatientList): Long = {
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
        'id -> UUIDGenerator.generateUUID("patients"),
        'first_name -> p.firstName,
        'middle_name -> p.middleName,
        'last_name -> p.lastName,
        'medical_history_id -> p.medicalHistoryId,
        'address -> p.address,
        'contact_no -> p.contactNo,
        'date_of_birth -> p.dateOfBirth,
        'image -> p.image,
        'status -> 1,
        'date_created -> "0000-00-00 00:00:00",
        'date_last_updated -> "0000-00-00 00:00:00"
        ).executeUpdate()
    }
  }


}
