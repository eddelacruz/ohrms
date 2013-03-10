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

case class PatientList(var id: String, firstName: Option[String], middleName: Option[String], lastName: Option[String], address: Option[String], contactNo: Option[String], dateOfBirth: Option[String], medicalHistory: Option[String], gender: String)
case class PatientLastVisit(p: PatientList, dateLastVisit: Option[String])

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/4/12
 * Time: 11:53 PM
 * To change this template use File | Settings | File Templates.
 */

object PatientService extends Secured{

  val username =  Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")

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
            |p.medical_history,
            |p.gender
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
            get[Option[String]]("medical_history")~
            get[String]("gender")map {
            case a ~ b ~ c ~ d ~ f ~ g ~ h ~ j ~ k=> PatientList(a, b, c, d, f, g, Some(h.toString), j, k)
          } *
        }
        patientList
    }
  }

  def getAllPatients: List[PatientList] = {
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
            |p.medical_history,
            |p.gender
            |from
            |patients p
            |where status = {status}
            |ORDER BY last_name asc
          """.stripMargin).on('status -> status ).as {
          get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[Option[String]]("medical_history")~
            get[String]("gender")map {
            case a ~ b ~ c ~ d ~ f ~ g ~ h ~ j ~ k=> PatientList(a, b, c, d, f, g, Some(h.toString), j, k)
          } *
        }
        patientList
    }
  }

  def getAllPatientNames: List[String] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |select
            |p.first_name
            |from
            |patients p
            |where status = {status}
            |ORDER BY last_name asc
          """.stripMargin).on('status -> status ).as( str("first_name") * )
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
            |medical_history,
            |gender
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
            get[Option[String]]("medical_history") ~
            get[String]("gender") map {
            case a ~ b ~ c ~ d ~ f ~ g ~ h ~ j ~ k => PatientList(a, b, c, d, f, g, Some(h.toString), j, k)
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
            |medical_history,
            |gender
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
            get[Option[String]]("medical_history") ~
            get[String]("gender")map {
            case a ~ b ~ c ~ d ~ f ~ g ~ h ~ j ~ k => PatientList(a, b, c, d, f, g, Some(h.toString), j, k)
          } *
        }
        searchPatientList
    }
  }


  def addPatient(p: PatientList): Long = {
    val currentUser = Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")
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
            |{medical_history},
            |{status},
            |{date_created},
            |{date_last_updated},
            |{gender}
            |);
          """.stripMargin).on(
          'id -> p.id,
          'first_name -> p.firstName,
          'middle_name -> p.middleName,
          'last_name -> p.lastName,
          'address -> p.address,
          'contact_no -> p.contactNo,
          'date_of_birth -> p.dateOfBirth,
          'medical_history -> p.medicalHistory,
          'status -> 1,
          'date_created -> DateWithTime.dateNow,
          'date_last_updated -> DateWithTime.dateNow,
          'gender -> p.gender
      ).executeUpdate()
        AuditLogService.logTaskPatient(p, currentUser, task)
    }

  }

  def updatePatient(p: PatientList): Long = {
    val currentUser = Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")
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
            |medical_history = {medical_history},
            |date_last_updated = {date_last_updated},
            |gender = {gender}
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> p.id,
          'first_name -> p.firstName,
          'middle_name -> p.middleName,
          'last_name -> p.lastName,
          'address -> p.address,
          'contact_no -> p.contactNo,
          'date_of_birth -> p.dateOfBirth,
          'medical_history -> p.medicalHistory,
          'date_last_updated -> DateWithTime.dateNow,
          'gender -> p.gender
        ).executeUpdate()
        AuditLogService.logTaskPatient(p, currentUser, task) //TODO cached user_id when login
   }

  }

  def deletePatient(id: String): Long = {
    val currentUser = Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")
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
        AuditLogService.logTaskDeletePatient(id, currentUser, task)
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
            |p.medical_history,
            |p.gender,
            |tp.date_performed
            |from
            |patients p
            |left outer join
            |treatment_plan tp
            |on p.id = tp.patient_id
            |where p.status = {status}
            |GROUP BY p.id
            |ORDER BY tp.date_performed desc, p.last_name asc
            |LIMIT {start}, {count}
            |) as result
          """.stripMargin).on('status -> status, 'start -> start, 'count -> count).as {
            get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[Option[String]]("medical_history") ~
            get[String]("gender") ~
            get[Option[Date]]("date_performed") map {
            case a ~ b ~ c ~ d ~ f ~ g ~ h ~ j ~ k ~ l => PatientLastVisit(PatientList(a, b, c, d, f, g, Some(h.toString), j, k), Some(l.toString.replace("Some", "").replace("(","").replace(".0)","")))
          } *
        }
        patientList
    }
  }

  def getPatientVisitsByYear(year: Int, month: Int): Long = {
    val date = year+"-"+month+"-01"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |select count(*) from
            |(select
            |p.id,
            |p.first_name,
            |p.middle_name,
            |p.last_name,
            |p.address,
            |p.contact_no,
            |p.date_of_birth,
            |p.medical_history,
            |p.gender,
            |tp.date_performed
            |from
            |patients p
            |inner join
            |treatment_plan tp
            |on p.id = tp.patient_id
            |where p.status = '1'
            |and date_performed between {date} and LAST_DAY({date})
            |GROUP BY p.id
            |ORDER BY p.last_name asc
            |) as result
          """.stripMargin).on('date -> date).as(scalar[Long].single)
    }
  }

  def getPatientVisitsByMonth(year: Int, month: Int, day: Int): Long = {
    val date = year+"-"+month+"-"+day
    //val date = format("%d-%d-%d", year, month, day)
    DB.withConnection {
        implicit c =>
          SQL(
            """
              |select count(*) from
              |(select
              |p.id,
              |p.first_name,
              |p.middle_name,
              |p.last_name,
              |p.address,
              |p.contact_no,
              |p.date_of_birth,
              |p.medical_history,
              |p.gender,
              |tp.date_performed
              |from
              |patients p
              |inner join
              |treatment_plan tp
              |on p.id = tp.patient_id
              |where p.status = '1'
              |and DATE_FORMAT(date_performed, '%Y-%m-%d') = DATE({date})
              |GROUP BY DATE_FORMAT(tp.date_performed, '%Y-%m-%d')
              |ORDER BY p.last_name asc
              |) as result
            """.stripMargin).on('date -> date).as(scalar[Long].single)
      }
    }

  def searchPatientLastVisit(start: Int,count: Int, filter: String): List[PatientLastVisit] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val searchPatientList: List[PatientLastVisit] = SQL(
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
            |p.medical_history,
            |p.gender,
            |tp.date_performed
            |from
            |patients p
            |left outer join
            |treatment_plan tp
            |on p.id = tp.patient_id
            |where p.status = {status}
            |and last_name like "%"{filter}"%"
            |or first_name like "%"{filter}"%"
            |or middle_name like "%"{filter}"%"
            |or address like "%"{filter}"%"
            |ORDER BY tp.date_performed desc
            |LIMIT {start}, {count}
            |) as result
            |group by id
          """.stripMargin).on('status -> status,'filter -> filter, 'start -> start, 'count -> count).as {
            get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[Option[String]]("medical_history") ~
            get[String]("gender") ~
            get[Option[Date]]("date_performed")  map {
              case a ~ b ~ c ~ d ~ f ~ g ~ h ~ j ~ k ~ l => PatientLastVisit(PatientList(a, b, c, d, f, g, Some(h.toString), j, k), Some(l.toString.replace("Some", "").replace("(","").replace(".0)","")))
           } *
        }
        searchPatientList
    }
  }

  def getPatientsByDateRange(startDate: String, endDate: String): List[PatientList] = {
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
            |p.medical_history,
            |p.gender
            |from
            |patients p
            |where status = {status} AND
            |(date_created between DATE({start_date}) and DATE({end_date})
            |or date_created = {end_date})
            |ORDER BY last_name asc
          """.stripMargin).on('status -> status, 'start_date -> {startDate}, 'end_date -> {endDate}).as {
            get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Date]("date_of_birth") ~
            get[Option[String]]("medical_history")~
            get[String]("gender")map {
            case a ~ b ~ c ~ d ~ f ~ g ~ h ~ j ~ k=> PatientList(a, b, c, d, f, g, Some(h.toString), j, k)
          } *
        }
        patientList
    }
  }

}
