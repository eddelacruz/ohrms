package ws.deserializer.json

import play.api.libs.json._
import ws.services.ClinicList

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 1/22/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */

trait ClinicListDeserializer {

  implicit object ClinicListFormat extends Format[ClinicList]{
    def reads(json: JsValue): ClinicList = ClinicList(
      (json \ "id").as[String],
      (json \ "clinicName").as[String],
      (json \ "address").as[String],
      (json \ "image").as[String]
    )

    def writes(d: ClinicList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "clinicName" -> JsString(d.clinicName),
        "address" -> JsString(d.address),
        "image" -> JsString(d.address)
      )
    )
  }

}


