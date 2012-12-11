package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import ws.helper.DateWithTime
import collection.mutable.ListBuffer

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/11/12
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */

case class DentistList(var id: String, firstName: String, middleName: String, lastName: String, address: String, contactNo: String, prcNo: String, image: String, userName: String, serviceName: Seq[String])

object DentistService {

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
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i => DentistList(a, b, c, d, e, f, g, h, i, getSpecializationToList(a))
          } *
        }
        dentistList
    }
  }

  def getSpecializationToList(id: String): Seq[String] = {
    DB.withConnection {
      implicit c =>
        var specializationList: Seq[String] = SQL(
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
            |d.id = {id}
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
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i => DentistList(a, b, c, d, e, f, g, h, i, getSpecializationToList(id))
          } *
        }
        dentistList
    }
  }


  def updateDentist(d: DentistList): Long = {
    val currentUser = "c7e5ef5d-07eb-4904-abbe-0aa73c13490f" //TODO static cvbautista
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
            |status = {status},
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
        AuditLogService.logTask(d, currentUser, task) //TODO cached user_id when login
    }

  }

}
