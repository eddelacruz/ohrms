package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.cache.{EhCachePlugin, Cache}
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

case class DentistList(var id: String, userId: String, firstName: Option[String], middleName: Option[String], lastName: Option[String], address: Option[String], contactNo: Option[String], prcNo: Option[String], image: Option[String], userName: Option[String], password: Option[String], specializationName: Option[Seq[String]])
case class Specialization(dentistId: String, name: Option[String])

object DentistService {

  def getRowCountOfTable(tableName: String): Long = {
    DB.withConnection {
      implicit c =>
        val rowCount = SQL("""select count(*) as c from """+tableName+""" where status = '1' """).apply().head
        rowCount[Long]("c")
    }
  }


  def searchDentistList(start: Int, count: Int, filter: String): List[DentistList] = {
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
            |u.user_name,
            |u.password,
            |d.user_id
            |from
            |dentists d
            |INNER JOIN users u
            |ON d.user_id=u.id
            |where d.status = {status}
            |and d.last_name like "%"{filter}"%"
            |or d.first_name like "%"{filter}"%"
            |or d.middle_name like "%"{filter}"%"
            |or d .address like "%"{filter}"%"
            |or d.prc_no like "%"{filter}"%"
            |ORDER BY d.last_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'filter -> filter, 'start -> start, 'count -> count).as {
            get[String]("id") ~
            get[String]("user_id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("prc_no") ~
            get[Option[String]]("image")~
            get[Option[String]]("user_name")~
            get[Option[String]]("password") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j ~ k => DentistList(a, b, c, d, e, f, g, h, i, j, k,  Some(getSpecializationToList(a)))
          } *
        }
        dentistList
    }
  }

  def getDentistList(start: Int, count: Int): List[DentistList] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val dentistList: List[DentistList] = SQL(
          """
            |select
            |d.id,
            |d.user_id,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.address,
            |d.contact_no,
            |d.prc_no,
            |d.image,
            |u.user_name,
            |u.password
            |from
            |dentists d
            |INNER JOIN users u
            |ON d.user_id=u.id
            |where d.status = {status}
            |ORDER BY d.last_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[String]("user_id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("prc_no") ~
            get[Option[String]]("image")~
            get[Option[String]]("user_name")~
            get[Option[String]]("password") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j ~ k => DentistList(a, b, c, d, e, f, g, h, i, j, k, Some(getSpecializationToList(a)))
          } *
        }
        dentistList
    }
  }

  def getAllDentist(): List[DentistList] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val dentistList: List[DentistList] = SQL(
          """
            |select
            |d.id,
            |d.user_id,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.address,
            |d.contact_no,
            |d.prc_no,
            |d.image,
            |u.user_name,
            |u.password
            |from
            |dentists d
            |INNER JOIN users u
            |ON d.user_id=u.id
            |where d.status = {status}
            |ORDER BY d.last_name asc
          """.stripMargin).on('status -> status).as {
          get[String]("id") ~
            get[String]("user_id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("prc_no") ~
            get[Option[String]]("image")~
            get[Option[String]]("user_name")~
            get[Option[String]]("password") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j ~k => DentistList(a, b, c, d, e, f, g, h, i, j, k, Some(getSpecializationToList(a)))
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
            |d.user_id,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.address,
            |d.contact_no,
            |d.prc_no,
            |d.image,
            |u.user_name,
            |u.password
            |from
            |dentists d
            |INNER JOIN users u
            |ON d.user_id=u.id
            |WHERE
            |d.id = {id} AND
            |d.status = 1
          """.stripMargin).on('id -> id).as {
          get[String]("id") ~
            get[String]("user_id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("prc_no") ~
            get[Option[String]]("image")~
            get[Option[String]]("user_name")~
            get[Option[String]]("password") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j ~ k => DentistList(a, b, c, d, e, f, g, h, i, j, k, Some(getSpecializationToList(id)))
          } *
        }
        dentistList
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

  def updateDentist(d: DentistList): Long = {
    val currentUser = getUserId
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
        AuditLogService.logTaskDentist(d, currentUser, task)
    }

  }

  def addDentist(d: DentistList): Long = {
    val currentUser = getUserId
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
     // AuditLogService.logTaskDentist(d, currentUser, task)
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
        AuditLogService.logTaskDentist(d, currentUser, task)
    }
  }

  def addSpecialization(s: Specialization): Long = {
    val currentUser = getUserId
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
      //  AuditLogService.logTaskDentist(d, currentUser, task)
    }
  }


  def deleteDentist(id: String): Long = {
    val currentUser = getUserId
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
