package ws.deserializer.json

import ws.services.AppointmentList
import play.api.libs.json._

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 1/16/13
 * Time: 8:32 AM
 * To change this template use File | Settings | File Templates.
 */

trait AppointmentDeserializer {

  implicit object AppointmentListFormat extends Format[AppointmentList]{
    def reads(json: JsValue): AppointmentList = AppointmentList(
      (json \ "id").as[String],
      (json \ "dentalServiceId").asOpt[String],
      (json \ "firstName").asOpt[String],
      (json \ "middleName").asOpt[String],
      (json \ "lastName").asOpt[String],
      (json \ "dentistId").asOpt[String],
      (json \ "contactNo").asOpt[String],
      (json \ "address").asOpt[String],
      (json \ "dateStart").asOpt[String],
      (json \ "dateEnd").asOpt[String]
    )

    def writes(p: AppointmentList): JsValue = JsObject(
      Seq(
        "id" -> JsString(p.id),
        "dentalServiceId" -> JsString(p.dentalServiceId.get),
        "firstName" -> JsString(p.firstName.get),
        "middleName" -> JsString(p.middleName.get),
        "lastName" -> JsString(p.lastName.get),
        "dentistId" -> JsString(p.dentistId.get),
        "contactNo" -> JsString(p.contactNo.get),
        "address" -> JsString(p.address.get),
        "dateStart" -> JsString(p.dateStart.get),
        "dateEnd" -> JsString(p.dateEnd.get)

      )
    )
  }

}


