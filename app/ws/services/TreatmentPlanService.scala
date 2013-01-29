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

case class TreatmentPlanType(var id: String, serviceId: Option[String], serviceName: Option[String], serviceCode: Option[String], toolType: Option[String], serviceType: Option[String], servicePrice: Option[String], color: Option[String], datePerformed: Option[String], teethName: Option[String], teethView: Option[String], teethPosition: Option[String], teethType: Option[String], patientId: Option[String], dentistId: Option[String], dentistName: Option[String], image: Option[String])

object TreatmentPlanService {

  def addTreatment(tp: TreatmentPlanType) = {
    DB.withConnection{
      implicit c =>
        SQL(
          """
            |INSERT INTO `ohrms`.`treatment_plan`
            |(`id`,
            |`service_id`,
            |`date_performed`,
            |`patient_id`,
            |`dentist_id`,
            |`status`,
            |`image`,
            |`teeth_id`)
            |VALUES
            |(
            |{id},
            |{service_id},
            |{date_performed},
            |{patient_id},
            |{dentist_id},
            |{status},
            |{image},
            |{teeth_id}
            |);
          """.stripMargin).on(
        'id -> UUIDGenerator.generateUUID("treatment_plan"),
        'service_id -> tp.serviceId,
        'date_performed -> tp.datePerformed,
        'patient_id -> tp.patientId,
        'dentist_id -> tp.dentistId,
        'status -> 1,
        'image -> tp.image,
        'teeth_id -> tp.teethName
      ).executeUpdate()
    }
    println("pumasok sa addTreatmentService")
  }

  /*def addTeethAffected(tp: TreatmentPlanType): Long = {
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
  }*/

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
            |s.`tool_type`,
            |s.`type` as 'service_type',
            |s.`price` as 'service_price',
            |s.`color`,
            |tp.`date_performed`,
            |ttha.`name` as 'teeth_name',
            |ttha.`view` as 'teeth_view',
            |ttha.`position` as 'teeth_position',
            |ttha.`type` as 'teeth_type',
            |p.`id` as 'patient_id',
            |d.`id` as 'dentist_id',
            |d.`first_name`,
            |d.`middle_name`,
            |d.`last_name`,
            |tp.`image`
            |FROM treatment_plan as tp inner join teeth_affected as ttha inner join dental_services as s inner join patients as p
            |inner join dentists as d
            |on tp.`service_id` = s.`id` AND
            |tp.`teeth_id` = ttha.`name` AND
            |tp.`patient_id` = p.`id` AND
            |tp.`dentist_id` = d.`id`
            |LIMIT {start}, {count}
          """.stripMargin).on('start -> start, 'count -> count).as {
            get[String]("treatment_plan.id") ~
            get[Option[String]]("dental_services.id") ~
            get[Option[String]]("dental_services.name") ~
            get[Option[String]]("dental_services.code") ~
            get[Option[Int]]("dental_services.tool_type") ~
            get[Option[String]]("dental_services.type") ~
            get[Option[String]]("dental_services.price") ~
            get[Option[String]]("dental_services.color") ~
            get[Option[Date]]("treatment_plan.date_performed") ~
            get[Option[String]]("teeth_affected.name") ~
            get[Option[String]]("teeth_affected.view") ~
            get[Option[String]]("teeth_affected.position") ~
            get[Option[String]]("teeth_affected.type") ~
            get[Option[String]]("patients.id") ~
            get[Option[String]]("dentists.id") ~
            get[Option[String]]("dentists.first_name") ~
            get[Option[String]]("dentists.middle_name") ~
            get[Option[String]]("dentists.last_name") ~
            get[Option[String]]("treatment_plan.image") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j ~ k ~ l ~ m ~ n ~ o ~ p ~ q ~ r ~ s=> TreatmentPlanType(a, b, c, d, Some(e.toString), f, g, h, Some(i.toString), j, k, l, m, n, o, Some(p+" "+q+" "+r), s)
          } *
        }
        treatmentPlan
    }
  }

}
