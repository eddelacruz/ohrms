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
      (json \ "firstName").as[String],
      (json \ "middleName").as[String],
      (json \ "lastName").as[String],
      (json \ "address").as[String],
      (json \ "contactNo").as[String],
      (json \ "dateOfBirth").as[String],
      (json \ "image").as[String],
      (json \ "medicalHistory").as[String]
    )

    def writes(p: PatientList): JsValue = JsObject(
      Seq(
        "id" -> JsString(p.id),
        "firstName" -> JsString(p.firstName),
        "middleName" -> JsString(p.middleName),
        "lastName" -> JsString(p.lastName),
        "address" -> JsString(p.address),
        "contactNo" -> JsString(p.contactNo),
        "dateOfBirth" -> JsString(p.dateOfBirth),
        "image" -> JsString(p.image),
        "medicalHistory" -> JsString(p.medicalHistory)
      )
    )
  }

}
