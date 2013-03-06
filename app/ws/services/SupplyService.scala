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
 * Date: 3/6/13
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */

case class SupplyList(var id: String, patientId: Option[String], name: Option[String], description: Option[String], quantity: Option[String], price: Option[String])
object SupplyService {

  val username =  Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")

  def getRowCountOfTable(tableName: String): Long = {
    DB.withConnection {
      implicit c =>
        val rowCount = SQL("""select count(*) as c from """+tableName+""" where status = '1' """).apply().head
        rowCount[Long]("c")
    }
  }

  def getSupplyList(start: Int, count: Int): List[SupplyList] = {
    DB.withConnection {
      implicit c =>
        val supplyList: List[SupplyList] = SQL(
          """
            |select
            |s.id,
            |s.name,
            |s.description,
            |s.price,
            |s.quantity,
            |p.id,
            |s.patient_id
            |from
            |dental_supplies s
            |LEFT OUTER JOIN patients p
            |ON s.patient_id=p.id
            |ORDER BY name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('start -> start, 'count -> count).as {
            get[String]("dental_supplies.id") ~
            get[Option[String]]("patients.id") ~
            get[Option[String]]("name")~
            get[Option[String]]("description") ~
            get[Option[String]]("quantity") ~
            get[Option[String]]("price") map {
            case a ~ b ~ c  ~ d ~ e ~ f=> SupplyList(a, b, c, d, e, f)
          } *
        }
        supplyList
    }
  }

  def getSupplyListById(id: String): List[SupplyList] = {
    DB.withConnection {
      implicit c =>
        val supplyList: List[SupplyList] = SQL(
          """
            |select
            |s.id,
            |p.id,
            |s.name,
            |s.description,
            |s.price,
            |s.quantity,
            |s.patient_id
            |from
            |dental_supplies s
            |LEFT OUTER JOIN patients p
            |ON s.patient_id=p.id
            |where s.id = {id}
            |ORDER BY name asc
          """.stripMargin).on('id -> id).as {
          get[String]("dental_supplies.id") ~
            get[Option[String]]("patients.id") ~
            get[Option[String]]("name")~
            get[Option[String]]("description") ~
            get[Option[String]]("quantity") ~
            get[Option[String]]("price") map {
            case a ~ b ~ c  ~ d ~ e ~ f=> SupplyList(a, b, c, d, e, f)
          } *
        }
        supplyList
    }
  }


  def searchSupplyList(start: Int, count: Int, filter: String): List[SupplyList] = {
    DB.withConnection {
      implicit c =>
        val supplyList: List[SupplyList] = SQL(
          """
            |select
            |s.id,
            |s.name,
            |s.description,
            |s.price,
            |s.quantity,
            |s.patient_id
            |p.id
            |from
            |dental_supplies s
            |LEFT OUTER JOIN patients p
            |ON s.patient_id=p.id
            |where name like "%"{filter}"%"
            |or description like "%"{filter}"%"
            |ORDER BY name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('filter -> filter,'start -> start, 'count -> count).as {
            get[String]("dental_supplies.id") ~
            get[Option[String]]("patients.id") ~
            get[Option[String]]("name")~
            get[Option[String]]("description") ~
            get[Option[String]]("quantity") ~
            get[Option[String]]("price") map {
            case a ~ b ~ c  ~ d ~ e ~ f=> SupplyList(a, b, c, d, e, f)
          } *
        }
        supplyList
    }
  }

  def addSupply(s: SupplyList): Long = {
    val currentUser = username
    val task = "Add"
    s.id = UUIDGenerator.generateUUID("dental_supplies")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO dental_supplies
            |VALUES
            |(
            |{id},
            |{name},
            |{patient_id},
            |{quantity},
            |{description},
            |{status},
            |{price})
          """.stripMargin).on(
          'id -> s.id,
          'name -> s.name,
          'patient_id -> s.patientId,
          'quantity -> s.quantity,
          'description -> s.description,
          'status -> 1,
          'price -> s.price
        ).executeUpdate()
        AuditLogService.logTaskSupply(s, currentUser, task)
    }
  }


  def updateSupply(s: SupplyList): Long = {
    val currentUser = username
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE dental_supplies SET
            |name = {name},
            |patient_id = {patient_id},
            |price = {price},
            |description = {description},
            |quantity = {quantity}
            |WHERE id = {id}
          """.stripMargin).on(
          'id -> s.id,
          'name -> s.name,
          'patient_id -> s.patientId,
          'quantity -> s.quantity,
          'description -> s.description,
          'price -> s.price
        ).executeUpdate()
        AuditLogService.logTaskSupply(s, currentUser, task)
    }
  }


}
