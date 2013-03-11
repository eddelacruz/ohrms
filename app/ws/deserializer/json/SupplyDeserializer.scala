package ws.deserializer.json
import play.api.libs.json._
import ws.services.SupplyList

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 3/6/13
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
trait SupplyDeserializer {

  implicit object SupplyListFormat extends Format[SupplyList]{
    def reads(json: JsValue): SupplyList = SupplyList(
      (json \ "id").as[String],
      (json \ "patientId").asOpt[String],
      (json \ "name").asOpt[String],
      (json \ "description").asOpt[String],
      (json \ "quantity").asOpt[String],
      (json \ "price").asOpt[String]
    )

    def writes(d: SupplyList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "patientId" -> JsString(d.patientId.get),
        "name" -> JsString(d.name.get),
        "description" -> JsString(d.description.get),
        "quantity" -> JsString(d.quantity.get),
        "price" -> JsString(d.price.get)
      )
    )
  }



}
