package ws

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB

case class PatientList(id: String, firstName: String, middleName: String, lastName: String, medicalHistoryId: String, address: String, contactNo: String, dateOfBirth: String, image:
String)

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/5/12
 * Time: 1:49 AM
 * To change this template use File | Settings | File Templates.
 */
object PatientService {

  def getPatientList: List[PatientList] = {
    DB.withConnection{
      implicit c =>
        val patientList: List[PatientList] = SQL(
          """
            |select id,
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
          """.stripMargin
        ).as {
          get[String]("id")~
          get[String]("first_name")~
          get[String]("middle_name")~
          get[String]("last_name")~
          get[String]("medical_history_id")~
          get[String]("address")~
          get[String]("contact_no")~
          get[java.util.Date]("date_of_birth")~
          get[String]("image") map {
            case a~b~c~d~e~f~g~h~i => PatientList(a,b,c,d,e,f,g,h.toString,i)
          }*
        }
        patientList

    }
  }

}
