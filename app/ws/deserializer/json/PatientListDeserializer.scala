package ws.deserializer.json

import ws.services.{PatientList}
import play.api.libs.json._

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/10/12
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
trait PatientListDeserializer {

  implicit object PatientListFormat extends Format[PatientList]{
    def reads(json: JsValue): PatientList = PatientList(
      (json \ "id").as[String],
      (json \ "firstName").asOpt[String],
      (json \ "middleName").asOpt[String],
      (json \ "lastName").asOpt[String],
      (json \ "address").asOpt[String],
      (json \ "contactNo").asOpt[String],
      (json \ "dateOfBirth").asOpt[String],
      (json \ "image").asOpt[String],
      (json \ "medicalHistory").asOpt[String]
    )

    def writes(p: PatientList): JsValue = JsObject(
      Seq(
        "id" -> JsString(p.id),
        "firstName" -> JsString(p.firstName.get),
        "middleName" -> JsString(p.middleName.get),
        "lastName" -> JsString(p.lastName.get),
        "address" -> JsString(p.address.get),
        "contactNo" -> JsString(p.contactNo.get),
        "dateOfBirth" -> JsString(p.dateOfBirth.get),
        "image" -> JsString(p.image.get),
        "medicalHistory" -> JsString(p.medicalHistory.get)
      )
    )
  }

}
