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
      (json \ "firstName").asOpt[String],
      (json \ "middleName").asOpt[String],
      (json \ "lastName").asOpt[String],
      (json \ "address").asOpt[String],
      (json \ "contactNo").asOpt[String],
      (json \ "prcNo").asOpt[String],
      (json \ "userName").asOpt[String],
      (json \ "password").asOpt[String],
      (json \ "specializationName").asOpt[List[String]],
      (json \ "question").asOpt[String],
      (json \ "answer").asOpt[String]
    )

    def writes(d: DentistList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "firstName" -> JsString(d.firstName.get),
        "middleName" -> JsString(d.middleName.get),
        "lastName" -> JsString(d.lastName.get),
        "address" -> JsString(d.address.get),
        "contactNo" -> JsString(d.contactNo.get),
        "prcNo" -> JsString(d.prcNo.get),
        "userName" -> JsString(d.userName.get),
        "password" -> JsString(d.password.get),
        "specializationName" -> JsArray(d.specializationName.get.map(sn => JsString(sn))),
        "question" -> JsString(d.question.get),
        "answer" -> JsString(d.answer.get)
      )
    )
  }

}
