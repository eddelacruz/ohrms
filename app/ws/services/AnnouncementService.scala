package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.cache.{EhCachePlugin, Cache}
import ws.helper.DateWithTime
import java.util.Date
import collection.mutable.ListBuffer
import ws.generator.UUIDGenerator

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 1/23/13
 * Time: 9:55 AM
 * To change this template use File | Settings | File Templates.
 */

case class AnnouncementList(var id: String, userName: Option[String], announcement: Option[String], dateCreated: Option[String])

object AnnouncementService {

  def getRowCountOfTable(tableName: Option[String]): Long = {
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

  def getAnnouncementList(start: Int, count: Int): List[AnnouncementList] = {
    DB.withConnection {
      implicit c =>
        val announcementList: List[AnnouncementList] = SQL(
          """
            |select
            |a.id,
            |u.user_name,
            |a.announcement,
            |a.date_created
            |from
            |announcements a
            |INNER JOIN users u
            |ON a.user_id = u.id
            |ORDER BY date_created asc
            |LIMIT {start}, {count}
          """.stripMargin).on('start -> start, 'count -> count).as {
            get[String]("id") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("announcement") ~
            get[Option[Date]]("date_created") map {
            case a ~ b ~ c  ~ d => AnnouncementList(a, b, c, Some(d.toString))
          } *
        }
        announcementList
    }
  }

  def getAnnouncementListById(id: String): List[AnnouncementList] = {
    DB.withConnection {
      implicit c =>
        val announcementList: List[AnnouncementList] = SQL(
          """
            |select
            |a.id,
            |u.user_name,
            |a.announcement,
            |a.date_created
            |from
            |announcements a
            |INNER JOIN users u
            |ON a.user_id = u.id
            |where a.id = {id}
            |ORDER BY date_created asc
          """.stripMargin).on('id -> id).as {
            get[String]("id") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("announcement") ~
            get[Option[Date]]("date_created") map {
            case a ~ b ~ c  ~ d => AnnouncementList(a, b, c, Some(d.toString))
          } *
        }
        announcementList
    }
  }

  def searchAnnouncementList(start: Int, count: Int, filter: String): List[AnnouncementList] = {
    DB.withConnection {
      implicit c =>
        val announcementList: List[AnnouncementList] = SQL(
          """
            |select
            |a.id,
            |u.user_name,
            |a.announcement,
            |a.date_created
            |from
            |announcements a
            |INNER JOIN users u
            |ON a.user_id = u.id
            |where a.announcment like "%"{filter}"%"
            |ORDER BY date_created asc
            |LIMIT {start}, {count}
          """.stripMargin).on('filter -> filter, 'start -> start, 'count -> count).as {
            get[String]("id") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("announcment") ~
            get[Option[Date]]("date_created")map {
            case a ~ b ~ c ~ d => AnnouncementList(a, b, c, Some(d.toString))
          } *
        }
        announcementList
    }
  }

  def addAnnouncement(d: AnnouncementList): Long = {
    println(getUserId)
    val currentUser = getUserId
    val task = "Add"
    d.id = UUIDGenerator.generateUUID("announcements")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO announcements
            |VALUES
            |(
            |{id},
            |{user_id},
            |{announcement},
            |{date_created})
          """.stripMargin).on(
          'id -> d.id,
          'user_id -> currentUser,
          'announcement -> d.announcement,
          'date_created -> d.dateCreated
        ).executeUpdate()
       // AuditLogService.logTaskAnnouncement(d, currentUser, task)
    }
  }


  def updateAnnouncement(p: AnnouncementList): Long = {
    val currentUser = getUserId
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE appointments SET
            |user_id = {user_name},
            |announcement = {announcement},
            |date_created = {date_created}
            |WHERE id = {id}
          """.stripMargin).on(
          'id -> p.id,
          'user_name -> currentUser,
          'announcement -> p.announcement,
          'date_created -> p.dateCreated
        ).executeUpdate()
       // AuditLogService.logTaskAnnouncement(p, currentUser, task)
    }
  }

  def deleteAnnouncement(id: String): Long = {
    val currentUser = getUserId
    val task = "Delete"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |DELETE FROM announcements
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> id
        ).executeUpdate()
        AuditLogService.logTask(id, currentUser, task)
    }
  }
}

