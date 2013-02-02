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

case class PatientList(var id: String, firstName: Option[String], middleName: Option[String], lastName: Option[String], address: Option[String], contactNo: Option[String], dateOfBirth: Option[String], image: Option[String], medicalHistory: Option[String])
case class PatientLastVisit(p: PatientList, dateLastVisit: Option[String])

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/4/12
 * Time: 11:53 PM
 * To change this template use File | Settings | File Templates.
 */

object PatientService extends Secured{

  def getRowCountOfTable(tableName: String): Long = {
    DB.withConnection {
      implicit c =>
        val rowCount = SQL("""select count(*) as c from """+tableName+""" where status = '1' """).apply().head
        rowCount[Long]("c")
    }
  }

  def getPatientList(start: Int, count: Int): List[PatientList] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val patientList: List[PatientList] = SQL(
          """
            |select
            |p.id,
            |p.first_name,
            |p.middle_name,
            |p.last_name,
            |p.address,
            |p.contact_no,
            |p.date_of_birth,
            |p.image,
            |p.medical_history
            |from
            |patients p
            |where status = {status}
            |ORDER BY last_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'start -> start, 'count -> count).as {
            get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[Option[String]]("image") ~
            get[Option[String]]("medical_history") map {
            case a ~ b ~ c ~ d ~ f ~ g ~ h ~ i ~ j => PatientList(a, b, c, d, f, g, Some(h.toString), i, j)
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
            |address,
            |contact_no,
            |date_of_birth,
            |image,
            |medical_history
            |from
            |patients
            |where
            |id = {id} AND
            |status = 1
          """.stripMargin).on('id -> id).as {
          get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[Option[String]]("image") ~
            get[Option[String]]("medical_history") map {
            case a ~ b ~ c ~ d ~ f ~ g ~ h ~ i ~ j => PatientList(a, b, c, d, f, g, Some(h.toString), i, j)
          } *
        }
        patientList
    }
  }

  def searchPatientListByLastName(start: Int,count: Int,filter: String): List[PatientList] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val searchPatientList: List[PatientList] = SQL(
          """
            |select
            |id,
            |first_name,
            |middle_name,
            |last_name,
            |address,
            |contact_no,
            |date_of_birth,
            |image,
            |medical_history
            |from
            |patients
            |where status = {status}
            |and last_name like "%"{filter}"%"
            |or first_name like "%"{filter}"%"
            |or middle_name like "%"{filter}"%"
            |or address like "%"{filter}"%"
            |ORDER BY last_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status,'filter -> filter, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[Option[String]]("image") ~
            get[Option[String]]("medical_history") map {
            case a ~ b ~ c ~ d ~ f ~ g ~ h ~ i ~ j => PatientList(a, b, c, d, f, g, Some(h.toString), i, j)
          } *
        }
        searchPatientList
    }
  }

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

  def addPatient(p: PatientList): Long = {
    println(getUserId)
    val currentUser = getUserId
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
            |{address},
            |{contact_no},
            |{date_of_birth},
            |{image},
            |{medical_history},
            |{status},
            |{date_created},
            |{date_last_updated}
            |);
          """.stripMargin).on(
          'id -> p.id,
          'first_name -> Option(p.firstName),
          'middle_name -> Option(p.middleName),
          'last_name -> Option(p.lastName),
          'address -> Option(p.address),
          'contact_no -> Option(p.contactNo),
          'date_of_birth -> Option(p.dateOfBirth),
          'image -> Option(p.image),
          'medical_history -> Option(p.medicalHistory),
          'status -> 1,
          'date_created -> DateWithTime.dateNow,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
        AuditLogService.logTask(p, currentUser, task)
    }

  }

  def updatePatient(p: PatientList): Long = {
    val currentUser = getUserId
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
            |medical_history = {medical_history},
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
          'medical_history -> p.medicalHistory,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
        AuditLogService.logTask(p, currentUser, task) //TODO cached user_id when login
   }

  }

  def deletePatient(id: String): Long = {
    val currentUser = getUserId
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

  def getPatientLastVisit(start: Int, count: Int): List[PatientLastVisit] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val patientList: List[PatientLastVisit] = SQL(
          """
            |select * from
            |(select
            |p.id,
            |p.first_name,
            |p.middle_name,
            |p.last_name,
            |p.address,
            |p.contact_no,
            |p.date_of_birth,
            |p.image,
            |p.medical_history,
            |tp.date_performed
            |from
            |patients p
            |left outer join
            |treatment_plan tp
            |on p.id = tp.patient_id
            |where p.status = {status}
            |ORDER BY tp.date_performed desc
            |LIMIT {start}, {count}
            |) as result
            |group by id
          """.stripMargin).on('status -> status, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[Option[String]]("image") ~
            get[Option[String]]("medical_history") ~
            get[Option[Date]]("date_performed") map {
            case a ~ b ~ c ~ d ~ f ~ g ~ h ~ i ~ j ~ k => PatientLastVisit(PatientList(a, b, c, d, f, g, Some(h.toString), i, j), Some(k.toString))
          } *
        }
        patientList
    }
  }

}
