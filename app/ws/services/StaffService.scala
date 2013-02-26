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


case class StaffList(var id: String, firstName: Option[String], middleName: Option[String], lastName: Option[String], contactNo: Option[String], address: Option[String], position: Option[String], userName: Option[String], password: Option[String], question: Option[String], answer: Option[String])

object StaffService {

  val username =  Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")

  def getStaffList(start: Int, count: Int): List[StaffList] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val staffList: List[StaffList] = SQL(
          """
            |select
            |d.id,
            |d.user_name,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.contact_no,
            |d.address,
            |d.position,
            |u.user_name,
            |u.question
            |from
            |staffs d
            |INNER JOIN users u
            |ON d.user_name=u.user_name
            |INNER JOIN security_questions sq ON u.question = sq.id
            |where d.status = {status}
            |ORDER BY d.last_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("address") ~
            get[Option[String]]("position") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("question") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h  ~ i => StaffList(a, b, c, d, e, f, g, h,  Some(""), i,Some(""))
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
            select
            |d.id,
            |d.user_name,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.contact_no,
            |d.address,
            |d.position,
            |u.user_name,
            |u.question
            |from
            |staffs d
            |INNER JOIN users u
            |ON d.user_name=u.user_name
            |INNER JOIN security_questions sq ON u.question = sq.id
            |where d.status = {status}
            |and d.last_name like "%"{filter}"%"
            |or d.first_name like "%"{filter}"%"
            |or d.middle_name like "%"{filter}"%"
            |ORDER BY d.last_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'filter -> filter, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("address") ~
            get[Option[String]]("position") ~
            get[Option[String]]("user_name") ~
          get[Option[String]]("question") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h  ~ i  => StaffList(a, b, c, d, e, f, g, h,  Some(""), i,Some(""))
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
            select
            |d.id,
            |d.user_name,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.contact_no,
            |d.address,
            |d.position,
            |u.user_name,
            |u.password
            |from
            |staffs d
            |INNER JOIN users u
            |ON d.user_name=u.user_name
            |where d.id = {id}
            |ORDER BY d.last_name asc
          """.stripMargin).on('id -> id).as {
          get[String]("id") ~
            get[Option[String]]("first_name") ~
            get[Option[String]]("middle_name") ~
            get[Option[String]]("last_name") ~
            get[Option[String]]("contact_no") ~
            get[Option[String]]("address") ~
            get[Option[String]]("position") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("password") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i => StaffList(a, b, c, d, e, f, g, h, i, Some(""),Some(""))
          } *
        }
        staffList
    }
  }


  def updateStaff(d: StaffList): Long = {
    val currentUser = username
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE users SET
            |password = {password}
            |where id = {id}
          """.stripMargin).on(
          'user_name -> d.userName,
          'password -> d.password,
          'role -> 2,
          'status -> 1,
          'date_created -> DateWithTime.dateNow
        ).executeUpdate()
    }
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
    val currentUser = username
    val task = "Add"
    d.id = UUIDGenerator.generateUUID("staffs")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO users
            |VALUES
            |(
            |{user_name},
            |{password},
            |{role},
            |{status},
            |{date_created},
            |{question},
            |{answer}
            |);
          """.stripMargin).on(
          'user_name -> d.userName,
          'password -> d.password,
          'role -> 2,
          'status -> 1,
          'date_created -> DateWithTime.dateNow,
          'question -> d.question,
          'answer -> d.answer
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
            |{contact_no},
            |{address},
            |{position},
            |{status},
            |{date_created},
            |{date_last_updated},
            |{user_name}
            |);
          """.stripMargin).on(
          'id -> d.id,
          'first_name -> d.firstName,
          'middle_name -> d.middleName,
          'last_name -> d.lastName,
          'address -> d.address,
          'contact_no -> d.contactNo,
          'position -> d.position,
          'user_name -> d.userName,
          'status -> 1,
          'date_created -> DateWithTime.dateNow,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
       AuditLogService.logTaskStaff(d, currentUser, task) //TODO cached user_id when login
    }
  }

  def deleteStaff(id: String): Long = {
    val currentUser = username
    val task = "Delete"
    var userName = ""
    DB.withConnection {
      implicit c =>
        val a = SQL(
          """
            |SELECT `user_name` from `ohrms`.`staffs`
            |where
            |`id` = {id};
          """.stripMargin).on('id -> id).apply().head
        userName = a[String]("user_name")
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE `ohrms`.`users`
            |SET
            |`status` = {status}
            |WHERE user_name = {user_name};
          """.stripMargin).on(
          'user_name -> userName,
          'status -> 0
        ).executeUpdate()
    }
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE staffs SET
            |status = {status}
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> id,
          'status -> 0
        ).executeUpdate()
       AuditLogService.logTaskDeleteStaff(id, currentUser, task)
    }
  }


}
