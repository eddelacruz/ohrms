package ws.deserializer.json

import play.api.libs.json._
import ws.services.DentistList

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/11/12
 * Time: 7:13 PM
 * To change this template use File | Settings | File Templates.
 */
trait DentistListDeserializer {

  implicit object DentistListFormat extends Format[DentistList]{
    def reads(json: JsValue): DentistList = DentistList(
      (json \ "id").as[String],
      (json \ "firstName").as[String],
      (json \ "middleName").as[String],
      (json \ "lastName").as[String],
      (json \ "address").as[String],
      (json \ "contactNo").as[String],
      (json \ "prcNo").as[String],
      (json \ "image").as[String],
      (json \ "userName").as[String],
      (json \ "password").as[String],
      (json \ "specializationName").as[List[String]]
    )

    def writes(d: DentistList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "firstName" -> JsString(d.firstName),
        "middleName" -> JsString(d.middleName),
        "lastName" -> JsString(d.lastName),
        "address" -> JsString(d.address),
        "contactNo" -> JsString(d.contactNo),
        "prcNo" -> JsString(d.prcNo),
        "image" -> JsString(d.image),
        "userName" -> JsString(d.userName),
        "password" -> JsString(d.password),
        "specializationName" -> JsArray(d.specializationName.map(sn => JsString(sn)))
      )
    )
  }

}
