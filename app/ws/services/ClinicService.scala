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

case class ClinicList(var id: String, clinicName: Option[String], address: Option[String], image: Option[String], userName: Option[String], contactNumber: Option[String])

object ClinicService {

  val username =  Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")

  def getRowCountOfTable(tableName: String): Long = {
    DB.withConnection {
      implicit c =>
        val rowCount = SQL("""select count(*) as c from """+tableName+""" where status = '1' """).apply().head
        rowCount[Long]("c")
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
            |image,
            |user_name,
            |contact_number
            |from
            |clinic
            |ORDER BY clinic_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('start -> start, 'count -> count).as {
          get[String]("id") ~
            get[Option[String]]("clinic_name") ~
            get[Option[String]]("address")~
            get[Option[String]]("image") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("contact_number") map {
            case a ~ b ~ c  ~ d ~ e ~ f=> ClinicList(a, b, c, d, e, f)
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
            |image,
            |user_name,
            |contact_number
            |from
            |clinic
            |where id = {id}
            |ORDER BY clinic_name asc
          """.stripMargin).on('id -> id).as {
          get[String]("id") ~
            get[Option[String]]("clinic_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("image") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("contact_number") map {
            case a ~ b ~ c ~ d ~ e ~ f=> ClinicList(a, b, c, d, e, f)
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
            |image,
            |user_name
            |from
            |clinic
            |where clinic_name like "%"{filter}"%"
            |or address like "%"{filter}"%"
            |ORDER BY clinic_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('filter -> filter, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[Option[String]]("clinic_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("image") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("contact_number") map {
            case a ~ b ~ c ~ d ~ e ~ f=> ClinicList(a, b, c, d, e, f)
          } *
        }
        clinicList
    }
  }

  def addClinic(d: ClinicList): Long = {
    val currentUser = username
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
            |{image},
            |{user_name},
            |{contact_number})
          """.stripMargin).on(
          'id -> d.id,
          'clinic_name -> d.clinicName,
          'address -> d.address,
          'image -> d.image,
          'user_name -> d.userName,
          'contact_number -> d.contactNumber
        ).executeUpdate()
      AuditLogService.logTaskClinic(d, currentUser, task)
    }
  }


  def updateClinic(p: ClinicList): Long = {
    val currentUser = username
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE clinic SET
            |clinic_name = {clinic_name},
            |address = {address},
            |image = {image},
            |user_name = {user_name},
            |contact_number = {contact_number}
            |WHERE id = {id}
          """.stripMargin).on(
          'id -> p.id,
          'clinic_name -> p.clinicName,
          'address -> p.address,
          'image -> p.image,
          'user_name -> p.userName,
          'contact_number -> p.contactNumber
        ).executeUpdate()
        AuditLogService.logTaskClinic(p, currentUser, task)
    }
  }

  def getClinic(): List[ClinicList] = {
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |select
            |id,
            |clinic_name,
            |address,
            |image,
            |user_name,
            |contact_number
            |from
            |clinic
            |ORDER BY clinic_name
            |LIMIT 1
          """.stripMargin).as {
            get[String]("id") ~
            get[Option[String]]("clinic_name") ~
            get[Option[String]]("address") ~
            get[Option[String]]("image") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("contact_number") map {
            case a ~ b ~ c ~ d ~ e ~ f => ClinicList(a, b, c, d, e, f)
          } *
        }
    }
  }


}

