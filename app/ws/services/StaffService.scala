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
 * User: joh
 * Date: 12/19/12
 * Time: 12:37 AM
 * To change this template use File | Settings | File Templates.
 */


case class StaffList(var id: String, firstName: String, middleName: String, lastName: String, contactNo: String, address: String, position: String, userName: String, password: String)

object StaffService {


  def getStaffList(start: Int, count: Int): List[StaffList] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val staffList: List[StaffList] = SQL(
          """
            |select
            |d.id,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.contact_no,
            |d.address,
            |d.position,
            |u.user_name
            |from
            |staffs d
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
            get[String]("contact_no") ~
            get[String]("address") ~
            get[String]("position") ~
            get[String]("user_name") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h => StaffList(a, b, c, d, e, f, g, h, "")
          } *
        }
        staffList
    }
  }

  def searchStaffList(start: Int, count: Int, filter: String): List[StaffList] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val staffList: List[StaffList] = SQL(
          """
            |select
            |d.id,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.contact_no,
            |d.address,
            |d.position,
            |u.user_name
            |from
            |staffs d
            |INNER JOIN users u
            |ON d.user_id=u.id
            |where d.status = {status}
            |and d.last_name like "%"{filter}"%"
            |or d.first_name like "%"{filter}"%"
            |or d.middle_name like "%"{filter}"%"
            |ORDER BY d.last_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'filter -> filter, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[String]("first_name") ~
            get[String]("middle_name") ~
            get[String]("last_name") ~
            get[String]("contact_no") ~
            get[String]("address") ~
            get[String]("position") ~
            get[String]("user_name") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h => StaffList(a, b, c, d, e, f, g, h, "")
          } *
        }
        staffList
    }
  }

  def getStaffListById(id: String): List[StaffList] = {
    DB.withConnection {
      implicit c =>
        val staffList: List[StaffList] = SQL(
          """
            |select
            |d.id,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.contact_no,
            |d.address,
            |d.position,
            |u.user_name
            |from
            |staffs d
            |INNER JOIN users u
            |ON d.user_id=u.id
            |where d.id = {id}
            |ORDER BY d.last_name asc
          """.stripMargin).on('id -> id).as {
          get[String]("id") ~
            get[String]("first_name") ~
            get[String]("middle_name") ~
            get[String]("last_name") ~
            get[String]("contact_no") ~
            get[String]("address") ~
            get[String]("position") ~
            get[String]("user_name") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h => StaffList(a, b, c, d, e, f, g, h, "")
          } *
        }
        staffList
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

  def updateStaff(d: StaffList): Long = {
    val currentUser = getUserId
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE staffs SET
            |first_name = {first_name},
            |middle_name = {middle_name},
            |last_name = {last_name},
            |address = {address},
            |contact_no = {contact_no},
            |position = {position},
            |date_last_updated = {date_last_updated}
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> d.id,
          'first_name -> d.firstName,
          'middle_name -> d.middleName,
          'last_name -> d.lastName,
          'address -> d.address,
          'contact_no -> d.contactNo,
          'position -> d.position,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
      AuditLogService.logTaskStaff(d, currentUser, task)
    }

  }

  def addStaff(d: StaffList): Long = {
    val currentUser = getUserId
    val task = "Add"
    val userId = UUIDGenerator.generateUUID("users")
    d.id = UUIDGenerator.generateUUID("staffs")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO users
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
      AuditLogService.logTaskStaff(d, currentUser, task)
    }
    DB.withTransaction {
      implicit c =>
        SQL(
          """
            |INSERT INTO staffs
            |VALUES
            |(
            |{id},
            |{first_name},
            |{middle_name},
            |{last_name},
            |{address},
            |{contact_no},
            |{position},
            |{user_id},
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
          'position -> d.position,
          'user_id -> userId,
          'status -> 1,
          'date_created -> DateWithTime.dateNow,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
       AuditLogService.logTaskStaff(d, currentUser, task) //TODO cached user_id when login
    }
  }


}
