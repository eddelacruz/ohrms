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
      (json \ "description").as[String],
      (json \ "firstName").as[String],
      (json \ "middleName").as[String],
      (json \ "lastName").as[String],
      (json \ "dentistId").as[String],
      (json \ "contactNo").as[String],
      (json \ "address").as[String],
      (json \ "status").as[Int],
      (json \ "dateStart").as[String],
      (json \ "dateEnd").as[String]
    )

    def writes(p: AppointmentList): JsValue = JsObject(
      Seq(
        "id" -> JsString(p.id),
        "description" -> JsString(p.description),
        "firstName" -> JsString(p.firstName),
        "middleName" -> JsString(p.middleName),
        "lastName" -> JsString(p.lastName),
        "dentistId" -> JsString(p.dentistId),
        "contactNo" -> JsString(p.contactNo),
        "address" -> JsString(p.address),
        "status" -> JsNumber(p.status),
        "dateStart" -> JsString(p.dateStart),
        "dateEnd" -> JsString(p.dateEnd)

      )
    )
  }

}


