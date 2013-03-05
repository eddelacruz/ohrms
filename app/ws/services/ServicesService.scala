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

case class DentalServiceList(var id: String, name: Option[String], code: Option[String], sType: Option[String], toolType: Option[Int], price: Option[String], color: Option[String], imageTemplate: Option[String])

object ServicesService {

  val username =  Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")

  def getRowCountOfTable(tableName: String): Long = {
    DB.withConnection {
      implicit c =>
        val rowCount = SQL("""select count(*) as c from """+tableName+""" where status = '1' """).apply().head
        rowCount[Long]("c")
    }
  }

  def getAllDentalServiceTypes: List[String] = {
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |select Distinct(type) from dental_services where status = 1;
          """.stripMargin).as( str("type") * )
    }
  }

  def getAllPatientNames: List[String] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |select
            |p.first_name
            |from
            |patients p
            |where status = {status}
            |ORDER BY last_name asc
          """.stripMargin).on('status -> status ).as( str("first_name") * )
    }
  }


  def getDentalServiceList(start: Int, count: Int): List[DentalServiceList] = {
    val status = 1
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
            |color,
            |image_template
            |from
            |dental_services
            |where status = {status}
            |ORDER BY name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[Option[String]]("name") ~
            get[Option[String]]("code") ~
            get[Option[String]]("type") ~
            get[Option[Int]]("tool_type") ~
            get[Option[String]]("price") ~
            get[Option[String]]("color") ~
            get[Option[String]]("image_template") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h => DentalServiceList(a, b, c, d, e, f, g, h)
          } *
        }
        dentalServiceList
    }
  }


  def searchServiceList(start: Int, count: Int, filter: String): List[DentalServiceList] = {
    val status = 1
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
            |color,
            |image_template
            |from
            |dental_services
            |where status = {status} and
            |name like "%"{filter}"%"
            |or code like "%"{filter}"%"
            |or type like "%"{filter}"%"
            |or price like "%"{filter}"%"
            |ORDER BY name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'filter -> filter, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[Option[String]]("name") ~
            get[Option[String]]("code") ~
            get[Option[String]]("type") ~
            get[Option[Int]]("tool_type") ~
            get[Option[String]]("price") ~
            get[Option[String]]("color") ~
            get[Option[String]]("image_template") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h => DentalServiceList(a, b, c, d, e, f, g, h)
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
            |color,
            |image_template
            |from
            |dental_services
            |WHERE id = {id}
            |and status = 1
            |ORDER BY name asc
          """.stripMargin).on('id -> id).as {
          get[String]("id") ~
            get[Option[String]]("name") ~
            get[Option[String]]("code") ~
            get[Option[String]]("type") ~
            get[Option[Int]]("tool_type") ~
            get[Option[String]]("price") ~
            get[Option[String]]("color") ~
            get[Option[String]]("image_template") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h => DentalServiceList(a, b, c, d, e, f, g, h)
          } *
        }
        dentalServiceList
    }
  }

  def addDentalService(d: DentalServiceList): Long = {
    val currentUser = username
    val task = "Add"
    //d.id = UUIDGenerator.generateUUID("dental_services")
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
            |{date_last_updated},
            |{status},
            |{image_template}
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
          'date_last_updated -> DateWithTime.dateNow,
          'status -> 1,
          'image_template -> d.imageTemplate
      ).executeUpdate()
       AuditLogService.logTaskServices(d, currentUser, task)
    }
  }

  def addBannedService(id: String, service: String): Long = {
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO banned_dental_services
            |VALUES
            |(
            |{id},
            |{dental_service_id}
            |);
          """.stripMargin).on(
          'id -> id,
          'dental_service_id -> service
      ).executeUpdate()
    }
  }

  def updateDentalService(d: DentalServiceList): Long = {
    val currentUser = username
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
            |image_template = {image_template}
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
          'image -> d.imageTemplate,
          'date_last_updated -> DateWithTime.dateNow
        ).executeUpdate()
      AuditLogService.logTaskServices(d, currentUser, task)
    }
  }

  def getBannedServicesByServiceCode(serviceId: String): List[String] = {
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |select id from dental_services
            |where
            |    id IN
            |(select
            |	bds.dental_service_id
            |from
            |	dental_services as ds
            |left outer join
            |	banned_dental_services as bds ON ds.id = bds.id
            |where
            |	ds.id = {service_id})
          """.stripMargin).on('service_id -> serviceId)().map( row => row[String]("id")).toList
    }
  }



  def getAllDentalServices(): List[DentalServiceList] = {
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
            |color,
            |image_template
            |from
            |dental_services
            |ORDER BY name asc
          """.stripMargin).as {
            get[String]("id") ~
            get[Option[String]]("name") ~
            get[Option[String]]("code") ~
            get[Option[String]]("type") ~
            get[Option[Int]]("tool_type") ~
            get[Option[String]]("price") ~
            get[Option[String]]("color") ~
            get[Option[String]]("image_template") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h => DentalServiceList(a, b, c, d, e, f, g, h)
          } *
        }
        dentalServiceList
    }
  }

  def deleteServices(id: String): Long = {
    val currentUser = username
    val task = "Delete"
    DB.withConnection {
      implicit c =>
        SQL(
          """
          |UPDATE dental_services SET
          |status = {status}
          |WHERE id = {id};
          """.stripMargin).on(
        'id -> id,
        'status -> 0
        ).executeUpdate()
    }
    AuditLogService.logTaskDeleteService(id, currentUser, task)
  }

  def getToothName(toothId: String): String = {
    DB.withConnection {
      implicit c =>
        val toothName = SQL("select name from teeth_affected where id = {tooth_id}").on('tooth_id -> toothId).apply().head
        toothName[String]("name")
    }
  }
}

