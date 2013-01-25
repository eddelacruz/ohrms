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
 * User: Robert
 * Date: 1/22/13
 * Time: 6:45 PM
 * To change this template use File | Settings | File Templates.
 */

case class ClinicList(var id: String, clinicName: String, address: String, image: String)

object ClinicService {

  def getRowCountOfTable(tableName: String): Long = {
    DB.withConnection {
      implicit c =>
        val rowCount = SQL("""select count(*) as c from """+tableName+""" where status = '1' """).apply().head
        rowCount[Long]("c")
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

  def getClinicList(start: Int, count: Int): List[ClinicList] = {
    DB.withConnection {
      implicit c =>
        val clinicList: List[ClinicList] = SQL(
          """
            |select
            |id,
            |clinic_name,
            |address,
            |image
            |from
            |clinic
            |ORDER BY clinic_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('start -> start, 'count -> count).as {
          get[String]("id") ~
            get[String]("clinic_name") ~
            get[String]("address")~
            get[String]("image")map {
            case a ~ b ~ c  ~ d => ClinicList(a, b, c, d)
          } *
        }
        clinicList
    }
  }

  def getClinicListById(id: String): List[ClinicList] = {
    DB.withConnection {
      implicit c =>
        val clinicList: List[ClinicList] = SQL(
          """
            |select
            |id,
            |clinic_name,
            |address,
            |image
            |from
            |clinic
            |where id = {id}
            |ORDER BY clinic_name asc
          """.stripMargin).on('id -> id).as {
          get[String]("id") ~
            get[String]("clinic_name") ~
            get[String]("address") ~
            get[String]("image")map {
            case a ~ b ~ c ~ d => ClinicList(a, b, c, d)
          } *
        }
        clinicList
    }
  }

  def searchClinicList(start: Int, count: Int, filter: String): List[ClinicList] = {
    DB.withConnection {
      implicit c =>
        val clinicList: List[ClinicList] = SQL(
          """
            |select
            |id,
            |clinic_name,
            |address,
            |image
            |from
            |clinic
            |where clinic_name like "%"{filter}"%"
            |or address like "%"{filter}"%"
            |ORDER BY clinic_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('filter -> filter, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[String]("clinic_name") ~
            get[String]("address") ~
            get[String]("image")map {
            case a ~ b ~ c ~ d => ClinicList(a, b, c, d)
          } *
        }
        clinicList
    }
  }

  def addClinic(d: ClinicList): Long = {
    println(getUserId)
    val currentUser = getUserId
    val task = "Add"
    d.id = UUIDGenerator.generateUUID("clinic")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO clinic
            |VALUES
            |(
            |{id},
            |{clinic_name},
            |{address},
            |{image})
          """.stripMargin).on(
          'id -> d.id,
          'clinic_name -> d.clinicName,
          'address -> d.address,
          'image -> d.image
        ).executeUpdate()
      AuditLogService.logTaskClinic(d, currentUser, task)
    }
  }


  def updateClinic(p: ClinicList): Long = {
    val currentUser = getUserId
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE clinic SET
            |clinic_name = {clinic_name},
            |address = {address},
            |image = {image}
            |WHERE id = {id}
          """.stripMargin).on(
          'id -> p.id,
          'clinic_name -> p.clinicName,
          'address -> p.address,
          'image -> p.image
        ).executeUpdate()
        AuditLogService.logTaskClinic(p, currentUser, task)
    }
  }


}

