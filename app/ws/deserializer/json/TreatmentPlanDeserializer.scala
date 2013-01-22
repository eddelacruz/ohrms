package ws.deserializer.json

import play.api.libs.json._
import ws.services.TreatmentPlanType
import ws.services.TreatmentPlanType
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import ws.services.TreatmentPlanType

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 12/19/12
 * Time: 1:31 AM
 * To change this template use File | Settings | File Templates.
 */

trait TreatmentPlanDeserializer {

  implicit object TreatmentPlanTypeFormat extends Format[TreatmentPlanType]{

    def reads(json: JsValue): TreatmentPlanType = TreatmentPlanType(
      (json \ "id").as[String],
      (json \ "serviceId").as[String],
      (json \ "serviceName").as[String],
      (json \ "serviceCode").as[String],
      (json \ "toolType").as[String],
      (json \ "serviceType").as[String],
      (json \ "servicePrice").as[String],
      (json \ "color").as[String],
      (json \ "datePerformed").as[String],
      (json \ "teethName").as[String],
      (json \ "teethView").as[String],
      (json \ "teethPosition").as[String],
      (json \ "teethType").as[String]
    )

    def writes(tp: TreatmentPlanType): JsValue = JsObject(
      Seq(
        "id" -> JsString(tp.id),
        "serviceId" -> JsString(tp.serviceId),
        "serviceName" -> JsString(tp.serviceName),
        "serviceCode" -> JsString(tp.serviceCode),
        "toolType" -> JsString(tp.toolType),
        "serviceType" -> JsString(tp.serviceType),
        "servicePrice" -> JsString(tp.servicePrice),
        "color" -> JsString(tp.color),
        "datePerformed" -> JsString(tp.datePerformed),
        "teethName" -> JsString(tp.teethName),
        "teethView"-> JsString(tp.teethView),
        "teethPosition" -> JsString(tp.teethPosition),
        "teethType" -> JsString(tp.teethType)
      )
    )
  }

}
