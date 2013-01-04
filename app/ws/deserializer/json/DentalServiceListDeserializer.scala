package ws.deserializer.json

import play.api.libs.json._
import ws.services.DentalServiceList

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 12/18/12
 * Time: 8:52 PM
 * To change this template use File | Settings | File Templates.
 */
trait DentalServiceListDeserializer {

  implicit object DentalServiceListFormat extends Format[DentalServiceList]{
    def reads(json: JsValue): DentalServiceList = DentalServiceList(
      (json \ "id").as[String],
      (json \ "name").as[String],
      (json \ "code").as[String],
      (json \ "sType").as[String],
      (json \ "target").as[String],
      (json \ "price").as[String],
      (json \ "color").as[String]
    )

    def writes(d: DentalServiceList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "name" -> JsString(d.name),
        "code" -> JsString(d.code),
        "sType" -> JsString(d.sType),
        "target" -> JsString(d.target),
        "price" -> JsString(d.price),
        "color" -> JsString(d.color)
      )
    )
  }

}
