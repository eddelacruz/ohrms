package ws.services

import ws.generator.UUIDGenerator
import play.api.db.DB
import anorm._
import play.api.Play.current
import anorm.SqlParser._
import anorm.~
import java.util.Date
import ws.helper.DateWithTime

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 12/17/12
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */

case class TreatmentPlanType(id: String, serviceId: String, serviceName: String, serviceCode: String, target: String, serviceType: String, servicePrice: String, color: String, datePerformed: String, teethName: String, teethView: String, teethPosition: String, teethType: String)

object TreatmentPlanService {

  def addTreatment(id: String, serviceId: String): Long = {
    DB.withConnection{
      implicit c =>
        SQL(
          """
            |INSERT INTO `ohrms`.`treatment_plan`
            |VALUES
            |(
            |{id},
            |{service_id},
            |{date_performed}
            |);
          """.stripMargin).on(
        'id -> id,
        'service_id -> serviceId,
        'date_performed -> DateWithTime.dateNow
        ).executeUpdate()
    }
  }

  def addTeethAffected(tp: TreatmentPlanType): Long = {
    DB.withTransaction {
      implicit c =>
        SQL(
          """
            |INSERT INTO `ohrms`.`teeth_affected`
            |VALUES
            |(
            |{id},
            |{treatment_plan_id},
            |{name},
            |{position},
            |{view},
            |{type}
            |);
          """.stripMargin).on(
        'id -> UUIDGenerator.generateUUID("teeth_affected"),
        'treatment_plan_id -> tp.id,
        'name -> tp.teethName,
        'position -> tp.teethPosition,
        'view -> tp.teethView,
        'type -> tp.teethType
      ).executeUpdate()
    }
  }

  def getTreatmentPlan(start: Int, count: Int): List[TreatmentPlanType] = {
    DB.withConnection {
      implicit c =>
        val treatmentPlan: List[TreatmentPlanType] = SQL(
          """
            |SELECT
            |tp.`id`,
            |s.`id` as 'service_id',
            |s.`name` as 'service_name',
            |s.`code` as 'service_code',
            |s.`target`,
            |s.`type` as 'service_type',
            |s.`price` as 'service_price',
            |s.`color`,
            |tp.`date_performed`,
            |ttha.`name` as 'teeth_name',
            |ttha.`view` as 'teeth_view',
            |ttha.`position` as 'teeth_position',
            |ttha.`type` as 'teeth_type'
            |FROM dental_services as s inner join (treatment_plan as tp inner join teeth_affected as ttha)
            |on s.`id` = tp.`service_id` and tp.`id` = ttha.`treatment_plan_id`
            |ORDER BY service_name asc
            |LIMIT {start}, {count}
          """.stripMargin).on('start -> start, 'count -> count).as {
            get[String]("treatment_plan.id") ~
            get[String]("dental_services.id") ~
            get[String]("dental_services.name") ~
            get[String]("dental_services.code") ~
            get[Boolean]("dental_services.target") ~    // TODO elizer tinyint
            get[String]("dental_services.type") ~
            get[String]("dental_services.price") ~
            get[String]("dental_services.color") ~
            get[Date]("treatment_plan.date_performed") ~
            get[String]("teeth_affected.name") ~
            get[String]("teeth_affected.view") ~
            get[String]("teeth_affected.position") ~
            get[String]("teeth_affected.type") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j ~ k ~ l ~ m => TreatmentPlanType(a, b, c, d, e.toString, f, g, h, i.toString, j, k, l, m)
          } *
        }
        treatmentPlan
    }
  }

}
