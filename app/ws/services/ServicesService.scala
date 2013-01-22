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
 * Date: 12/18/12
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */

case class DentalServiceList(var id: String, name: String, code: String, sType: String, toolType: Int, price: String, color: String)

object ServicesService {

  def getRowCountOfTable(tableName: String): Long = {
    DB.withConnection {
      implicit c =>
        val rowCount = SQL("""select count(*) as c from """+tableName+""" where status = '1' """).apply().head
        rowCount[Long]("c")
    }
  }


  def getDentalServiceList(start: Int, count: Int): List[DentalServiceList] = {

    DB.withConnection {
      implicit c =>
        val dentalServiceList: List[DentalServiceList] = SQL(
          """
            |select
            |id,
            |name,
            |code,
            |type,
            |tool_type,
            |price,
            |color
            |from
            |dental_services
            |ORDER BY name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('start -> start, 'count -> count).as {
          get[String]("id") ~
            get[String]("name") ~
            get[String]("code") ~
            get[String]("type") ~
            get[Int]("tool_type") ~
            get[String]("price") ~
            get[String]("color") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g => DentalServiceList(a, b, c, d, e, f, g)
          } *
        }
        dentalServiceList
    }
  }


  def searchServiceList(start: Int, count: Int, filter: String): List[DentalServiceList] = {
    DB.withConnection {
      implicit c =>
        val dentalServiceList: List[DentalServiceList] = SQL(
          """
            |select
            |id,
            |name,
            |code,
            |type,
            |tool_type,
            |price,
            |color
            |from
            |dental_services
            |where name like "%"{filter}"%"
            |or type like "%"{filter}"%"
            |or color like "%"{filter}"%"
            |or price like "%"{filter}"%"
            |ORDER BY name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('filter -> filter, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[String]("name") ~
            get[String]("code") ~
            get[String]("type") ~
            get[Int]("tool_type") ~
            get[String]("price") ~
            get[String]("color") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g => DentalServiceList(a, b, c, d, e, f, g)
          } *
        }
        dentalServiceList
    }
  }

  def getDentalServiceListById(id :String): List[DentalServiceList]  = {

    DB.withConnection {
      implicit c =>
        val dentalServiceList: List[DentalServiceList] = SQL(
          """
            |select
            |id,
            |name,
            |code,
            |type,
            |tool_type,
            |price,
            |color
            |from
            |dental_services
            |WHERE id = {id}
            |ORDER BY name asc
          """.stripMargin).on('id -> id).as {
          get[String]("id") ~
            get[String]("name") ~
            get[String]("code") ~
            get[String]("type") ~
            get[Int]("tool_type") ~
            get[String]("price") ~
            get[String]("color") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g => DentalServiceList(a, b, c, d, e, f, g)
          } *
        }
        dentalServiceList
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

  def addDentalService(d: DentalServiceList): Long = {
    val currentUser = getUserId
    val task = "Add"
    d.id = UUIDGenerator.generateUUID("dental_services")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO dental_services
            |VALUES
            |(
            |{id},
            |{name},
            |{code},
            |{type},
            |{tool_type},
            |{price},
            |{color},
            |{date_created},
            |{date_last_updated}
            |);
          """.stripMargin).on(
          'id -> d.id,
          'name -> d.name,
          'code -> d.code,
          'type-> d.sType,
          'tool_type -> d.toolType,
          'price -> d.price,
          'color -> d.color,
          'date_created -> DateWithTime.dateNow,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
       AuditLogService.logTaskServices(d, currentUser, task)
    }

  }

  def updateDentalService(d: DentalServiceList): Long = {
    val currentUser = getUserId
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE dental_services SET
            |name = {name},
            |code = {code},
            |type = {type},
            |tool_type = {tool_type},
            |price = {price},
            |color = {color},
            |date_last_updated = {date_last_updated}
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> d.id,
          'name -> d.name,
          'code -> d.code,
          'type -> d.sType,
          'tool_type -> d.toolType,
          'price -> d.price,
          'color -> d.color,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
      AuditLogService.logTaskServices(d, currentUser, task)
    }

  }







}
