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

}
