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
      (json \ "name").asOpt[String],
      (json \ "code").asOpt[String],
      (json \ "type").asOpt[String],
      (json \ "tool_type").asOpt[Int],
      (json \ "price").asOpt[String],
      (json \ "color").asOpt[String],
      (json \ "image_template").asOpt[String]
    )

    def writes(d: DentalServiceList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "name" -> JsString(d.name.get),
        "code" -> JsString(d.code.get),
        "type" -> JsString(d.sType.get),
        "tool_type" -> JsNumber(d.toolType.get),
        "price" -> JsString(d.price.get),
        "color" -> JsString(d.color.get),
        "image_template" -> JsString(d.imageTemplate.get)
      )
    )
  }

}
