package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import ws.helper.DateWithTime
import collection.mutable.ListBuffer
import ws.generator.UUIDGenerator

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/11/12
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */

case class DentistList(var id: String, firstName: String, middleName: String, lastName: String, address: String, contactNo: String, prcNo: String, image: String, userName: String, password: String, specializationName: Seq[String])
case class Specialization(dentistId: String, name: String)

object DentistService {

  val currentUser = "c7e5ef5d-07eb-4904-abbe-0aa73c13490f"

  def getDentistList(start: Int, count: Int): List[DentistList] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val dentistList: List[DentistList] = SQL(
          """
            |select
            |d.id,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.address,
            |d.contact_no,
            |d.prc_no,
            |d.image,
            |u.user_name
            |from
            |dentists d
            |INNER JOIN users u
            |ON d.user_id=u.id
            |where d.status = {status}
            |ORDER BY d.last_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[String]("first_name") ~
            get[String]("middle_name") ~
            get[String]("last_name") ~
            get[String]("address") ~
            get[String]("contact_no") ~
            get[String]("prc_no") ~
            get[String]("image")~
            get[String]("user_name") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i => DentistList(a, b, c, d, e, f, g, h, i, "", getSpecializationToList(a))
          } *
        }
        dentistList
    }
  }

  def getSpecializationToList(id: String): Seq[String] = {
    DB.withConnection {
      implicit c =>
        val specializationList: Seq[String] = SQL(
          """
            |select name
            |from specializations
            |where dentist_id = {id}
          """.stripMargin).on('id -> id).as{
          get[String]("name") map {
          case a => a
        } *
      }
      specializationList
    }
  }


  def getDentistListById(id :String): List[DentistList] = {
    DB.withConnection {
      implicit c =>
        val dentistList: List[DentistList] = SQL(
          """
            |select
            |d.id,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.address,
            |d.contact_no,
            |d.prc_no,
            |d.image,
            |u.user_name
            |from
            |dentists d
            |INNER JOIN users u
            |ON d.user_id=u.id
            |WHERE
            |d.id = {id} AND
            |d.status = 1
          """.stripMargin).on('id -> id).as {
          get[String]("id") ~
            get[String]("first_name") ~
            get[String]("middle_name") ~
            get[String]("last_name") ~
            get[String]("address") ~
            get[String]("contact_no") ~
            get[String]("prc_no") ~
            get[String]("image")~
            get[String]("user_name") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i => DentistList(a, b, c, d, e, f, g, h, i, "", getSpecializationToList(id))
          } *
        }
        dentistList
    }
  }


  def updateDentist(d: DentistList): Long = {
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE dentists SET
            |first_name = {first_name},
            |middle_name = {middle_name},
            |last_name = {last_name},
            |address = {address},
            |contact_no = {contact_no},
            |prc_no = {prc_no},
            |image = {image},
            |date_last_updated = {date_last_updated}
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> d.id,
          'first_name -> d.firstName,
          'middle_name -> d.middleName,
          'last_name -> d.lastName,
          'address -> d.address,
          'contact_no -> d.contactNo,
          'prc_no -> d.prcNo,
          'image -> d.image,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
        AuditLogService.logTaskDentist(d, currentUser, task) //TODO cached user_id when login
    }

  }

  def addDentist(d: DentistList): Long = {
    val task = "Add"
    val userId = UUIDGenerator.generateUUID("users")
    d.id = UUIDGenerator.generateUUID("dentists")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO `ohrms`.`users`
            |VALUES
            |(
            |{id},
            |{user_name},
            |{password},
            |{role},
            |{status},
            |{date_created}
            |);
          """.stripMargin).on(
          'id -> userId,
          'user_name -> d.userName,
          'password -> d.password,
          'role -> 1, //Dentist
          'status -> 1,
          'date_created -> DateWithTime.dateNow
        ).executeUpdate()
      //AuditLogService.logTaskDentist(s, currentUser, task)
    }
    DB.withTransaction {
      implicit c =>
        SQL(
          """
            |INSERT INTO dentists
            |VALUES
            |(
            |{id},
            |{first_name},
            |{middle_name},
            |{last_name},
            |{address},
            |{contact_no},
            |{prc_no},
            |{user_id},
            |{image},
            |{status},
            |{date_created},
            |{date_last_updated}
            |);
          """.stripMargin).on(
          'id -> d.id,
          'first_name -> d.firstName,
          'middle_name -> d.middleName,
          'last_name -> d.lastName,
          'address -> d.address,
          'contact_no -> d.contactNo,
          'prc_no -> d.prcNo,
          'user_id -> userId,
          'image -> d.image,
          'status -> 1,
          'date_created -> DateWithTime.dateNow,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
        AuditLogService.logTaskDentist(d, currentUser, task) //TODO cached user_id when login
    }
  }

  def addSpecialization(s: Specialization): Long = {
    val task = "Add"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO specializations
            |VALUES
            |(
            |{dentist_id},
            |{name}
            |);
          """.stripMargin).on(
          'dentist_id -> s.dentistId,
          'name -> s.name
        ).executeUpdate()
        //AuditLogService.logTaskDentist(s, currentUser, task)
    }
  }


  def deleteDentist(id: String): Long = {
    val currentUser = "c7e5ef5d-07eb-4904-abbe-0aa73c13490f" //static cvbautista
    val task = "Delete"
    println("pumasok dito")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE dentists SET
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
