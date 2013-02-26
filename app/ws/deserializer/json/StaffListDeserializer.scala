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
      (json \ "firstName").asOpt[String],
      (json \ "middleName").asOpt[String],
      (json \ "lastName").asOpt[String],
      (json \ "contactNo").asOpt[String],
      (json \ "address").asOpt[String],
      (json \ "position").asOpt[String],
      (json \ "userName").asOpt[String],
      (json \ "password").asOpt[String],
      (json \ "question").asOpt[String],
      (json \ "answer").asOpt[String]
    )

    def writes(d: StaffList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "firstName" -> JsString(d.firstName.get),
        "middleName" -> JsString(d.middleName.get),
        "lastName" -> JsString(d.lastName.get),
        "contactNo" -> JsString(d.contactNo.get),
        "address" -> JsString(d.address.get),
        "position" -> JsString(d.position.get),
        "userName" -> JsString(d.userName.get),
        "password" -> JsString(d.password.get),
        "question" -> JsString(d.question.get),
        "answer" -> JsString(d.answer.get)
      )
    )
  }

}
