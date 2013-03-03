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
      (json \ "serviceId").asOpt[String],
      (json \ "serviceName").asOpt[String],
      (json \ "serviceCode").asOpt[String],
      (json \ "toolType").asOpt[String],
      (json \ "serviceType").asOpt[String],
      (json \ "servicePrice").asOpt[String],
      (json \ "color").asOpt[String],
      (json \ "datePerformed").asOpt[String],
      (json \ "teethId").asOpt[String],
      (json \ "teethName").asOpt[String],
      (json \ "teethView").asOpt[String],
      (json \ "teethPosition").asOpt[String],
      (json \ "teethType").asOpt[String],
      (json \ "patientId").asOpt[String],
      (json \ "dentistId").asOpt[String],
      (json \ "dentistName").asOpt[String],
      (json \ "image").asOpt[String],
      (json \ "imageTemplate").asOpt[String]
    )

    def writes(tp: TreatmentPlanType): JsValue = JsObject(
      Seq(
        "id" -> JsString(tp.id),
        "serviceId" -> JsString(tp.serviceId.get),
        "serviceName" -> JsString(tp.serviceName.get),
        "serviceCode" -> JsString(tp.serviceCode.get),
        "toolType" -> JsString(tp.toolType.get),
        "serviceType" -> JsString(tp.serviceType.get),
        "servicePrice" -> JsString(tp.servicePrice.get),
        "color" -> JsString(tp.color.get),
        "datePerformed" -> JsString(tp.datePerformed.get),
        "teethId" -> JsString(tp.teethId.get),
        "teethName" -> JsString(tp.teethName.get),
        "teethView"-> JsString(tp.teethView.get),
        "teethPosition" -> JsString(tp.teethPosition.get),
        "teethType" -> JsString(tp.teethType.get),
        "patientId" -> JsString(tp.patientId.get),
        "dentistId" -> JsString(tp.dentistId.get),
        "dentistName" -> JsString(tp.dentistName.get),
        "image" -> JsString(tp.image.get),
        "imageTemplate" -> JsString(tp.imageTemplate.get)
      )
    )
  }

}
