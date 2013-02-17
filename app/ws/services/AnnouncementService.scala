package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.cache.{EhCachePlugin, Cache}
import ws.helper.DateWithTime
import ws.helper.Maid
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

case class AnnouncementList(var id: String, username: Option[String], description: Option[String], dateCreated: Option[String])

object AnnouncementService {

  def getRowCountOfTable(tableName: Option[String]): Long = {
    DB.withConnection {
      implicit c =>
        val rowCount = SQL("""select count(*) as c from """+tableName+""" where status = '1' """).apply().head
        rowCount[Long]("c")
    }
  }

  def getAnnouncementList(start: Int, count: Int): List[AnnouncementList] = {
    DB.withConnection {
      implicit c =>
        val announcementList: List[AnnouncementList] = SQL(
          """
            |select
            |r.id,
            |u.user_name,
            |r.description,
            |r.date_created
            |from
            |reminders r
            |INNER JOIN users u
            |ON r.user_name = u.user_name
            |ORDER BY date_created asc
            |LIMIT {start}, {count}
          """.stripMargin).on('start -> start, 'count -> count).as {
            get[String]("id") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("description") ~
            get[Date]("date_created") map {
            case a ~ b ~ c  ~ d => AnnouncementList(a, b, c, Some(d.toString))
          } *
        }
        announcementList
    }
  }


  def getAnnouncementsToday = {
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |select
            |r.id,
            |u.user_name,
            |r.description,
            |r.date_created
            |from
            |reminders r
            |INNER JOIN users u
            |ON r.user_name = u.user_name
            |where
            |  DATE(r.date_created) = DATE({date_only})
            |ORDER BY date_created asc
            |""".stripMargin
        ).on('date_only -> DateWithTime.dateOnly).as { //
            get[String]("id") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("description") ~
            get[Date]("date_created") map {
            case a ~ b ~ c ~ d => AnnouncementList(a, b, c, Some(d.toString))
            } *
        }
    }
  }

  def getAnnouncementListById(id: String): List[AnnouncementList] = {
    DB.withConnection {
      implicit c =>
        val announcementList: List[AnnouncementList] = SQL(
          """
            select
            |r.id,
            |u.user_name,
            |r.description,
            |r.date_created
            |from
            |reminders r
            |INNER JOIN users u
            |ON r.user_name = u.user_name
            |where r.id = {id}
            |ORDER BY date_created asc
          """.stripMargin).on('id -> id).as {
            get[String]("id") ~
            get[Option[String]]("user_name") ~
            get[Option[String]]("description") ~
            get[Option[Date]]("date_created") map {
            case a ~ b ~ c  ~ d => AnnouncementList(a, b, c, Some(d.toString.replace("Some", "").replace("(","").replace(")","").replace(".0","")))
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
            select
            |r.id,
            |u.user_name,
            |r.description,
            |r.date_created
            |from
            |reminders r
            |INNER JOIN users u
            |ON r.user_name = u.user_name
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
    val currentUser = Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")
    val task = "Add"
    d.id = UUIDGenerator.generateUUID("reminders")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO reminders
            |VALUES
            |(
            |{id},
            |{description},
            |{date_created},
            |{user_name})
          """.stripMargin).on(
          'id -> d.id,
          'description -> d.description,
          'date_created -> d.dateCreated,
          'user_name -> currentUser
      ).executeUpdate()
      AuditLogService.logTaskAnnouncement(d, currentUser, task)
    }
  }


  def updateAnnouncement(p: AnnouncementList): Long = {
    val currentUser = Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE reminders SET
            |user_name = {user_name},
            |description = {description},
            |date_created = {date_created}
            |WHERE id = {id}
          """.stripMargin).on(
          'id -> p.id,
          'user_name -> currentUser,
          'description -> p.description,
          'date_created -> p.dateCreated
        ).executeUpdate()
       AuditLogService.logTaskAnnouncement(p, currentUser, task)
    }
  }

  def deleteAnnouncement(id: String): Long = {
    val currentUser = Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")
    val task = "Delete"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |DELETE FROM reminders
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> id
        ).executeUpdate()
        AuditLogService.logTaskDeleteAnnouncement(id, currentUser, task)
    }
  }
}

