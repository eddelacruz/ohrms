package ws.deserializer.json

import ws.services.{PatientLastVisit, PatientList}
import play.api.libs.json._
import java.util.Date

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
      (json \ "medicalHistory").asOpt[String],
      (json \ "gender").as[String]
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
        "medicalHistory" -> JsString(p.medicalHistory.get),
        "gender" -> JsString(p.gender)
      )
    )
  }

  implicit object PatientLastVisitFormat extends Format[PatientLastVisit]{
    def reads(json: JsValue): PatientLastVisit = PatientLastVisit(
      PatientList(
        (json \ "id").as[String],
        (json \ "firstName").asOpt[String],
        (json \ "middleName").asOpt[String],
        (json \ "lastName").asOpt[String],
        (json \ "address").asOpt[String],
        (json \ "contactNo").asOpt[String],
        (json \ "dateOfBirth").asOpt[String],
        (json \ "medicalHistory").asOpt[String],
        (json \ "gender").as[String]
      ), (json \ "dateLastVisit").asOpt[String]
    )

    def writes(plv: PatientLastVisit): JsValue =
      JsArray(
        Seq(
          JsObject(
            Seq(
              "id" -> JsString(plv.p.id),
              "firstName" -> JsString(plv.p.firstName.get),
              "middleName" -> JsString(plv.p.middleName.get),
              "lastName" -> JsString(plv.p.lastName.get),
              "address" -> JsString(plv.p.address.get),
              "contactNo" -> JsString(plv.p.contactNo.get),
              "dateOfBirth" -> JsString(plv.p.dateOfBirth.get),
              "medicalHistory" -> JsString(plv.p.medicalHistory.get),
              "gender" -> JsString(plv.p.gender)
            )
          ), JsObject(Seq("dateLastVisit" -> JsString(plv.dateLastVisit.get)))
        )
      )
  }

}
