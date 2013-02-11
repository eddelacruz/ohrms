package ws.deserializer.json

import play.api.libs.json._
import ws.services.SpecializationList

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 1/22/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */

trait SpecializationListDeserializer {

  implicit object SpecializationListFormat extends Format[SpecializationList]{
    def reads(json: JsValue): SpecializationList = SpecializationList(
      (json \ "id").as[String],
      (json \ "dentistId").as[String],
      (json \ "name").as[String]
    )

    def writes(d: SpecializationList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "dentistId" -> JsString(d.dentistId),
        "name" -> JsString(d.name)
      )
    )
  }

}


