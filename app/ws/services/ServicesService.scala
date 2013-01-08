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
 * User: joh
 * Date: 12/18/12
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */

case class DentalServiceList(var id: String, name: String, code: String, sType: String, target: String, price: String, color: String)

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
            |target,
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
            get[Boolean]("target") ~   // TODO ni eli get TinyInt
            get[String]("price") ~
            get[String]("color") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g => DentalServiceList(a, b, c, d, e.toString(), f, g)
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
            |target,
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
            get[Boolean]("target") ~   // TODO ni eli get TinyInt
            get[String]("price") ~
            get[String]("color") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g => DentalServiceList(a, b, c, d, e.toString(), f, g)
          } *
        }
        dentalServiceList
    }
  }


  def addDentalService(d: DentalServiceList): Long = {
    val currentUser = "c7e5ef5d-07eb-4904-abbe-0aa73c13490f" //static cvbautista
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
            |{target},
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
          'target -> d.target,
          'price -> d.price,
          'color -> d.color,
          'date_created -> DateWithTime.dateNow,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
       // AuditLogService.logTask(d, currentUser, task) //TODO cached user_id when login
    }

  }

  def updateDentalService(d: DentalServiceList): Long = {
    val currentUser = "c7e5ef5d-07eb-4904-abbe-0aa73c13490f" //TODO static cvbautista
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE dental_services SET
            |name = {name},
            |code = {code},
            |type = {type},
            |target = {target},
            |price = {price},
            |color = {color},
            |date_last_updated = {date_last_updated}
            |WHERE id = {id};
          """.stripMargin).on(
          'id -> d.id,
          'name -> d.name,
          'code -> d.code,
          'type -> d.sType,
          'target -> d.target,
          'price -> d.price,
          'color -> d.color,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
     //   AuditLogService.logTask(d, currentUser, task) //TODO cached user_id when login
    }

  }







}
