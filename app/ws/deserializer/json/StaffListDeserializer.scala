package ws.deserializer.json

import play.api.libs.json._
import ws.services.StaffList

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 12/19/12
 * Time: 1:00 AM
 * To change this template use File | Settings | File Templates.
 */
trait StaffListDeserializer {

  implicit object StaffListFormat extends Format[StaffList]{
    def reads(json: JsValue): StaffList = StaffList(
      (json \ "id").as[String],
      (json \ "firstName").as[String],
      (json \ "middleName").as[String],
      (json \ "lastName").as[String],
      (json \ "contactNo").as[String],
      (json \ "address").as[String],
      (json \ "position").as[String],
      (json \ "userName").as[String],
      (json \ "password").as[String]
    )

    def writes(d: StaffList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "firstName" -> JsString(d.firstName),
        "middleName" -> JsString(d.middleName),
        "lastName" -> JsString(d.lastName),
        "contactNo" -> JsString(d.contactNo),
        "address" -> JsString(d.address),
        "position" -> JsString(d.position),
        "userName" -> JsString(d.userName),
        "password" -> JsString(d.password)
      )
    )
  }

}
