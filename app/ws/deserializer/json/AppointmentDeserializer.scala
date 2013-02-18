package ws.deserializer.json

import ws.services.{AppointmentList, AppointmentDetails}
import play.api.libs.json._

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 1/16/13
 * Time: 8:32 AM
 * To change this template use File | Settings | File Templates.
 */

trait AppointmentDeserializer {

  implicit object AppointmentListFormat extends Format[AppointmentList]{
    def reads(json: JsValue): AppointmentList = AppointmentList(
      (json \ "id").as[String],
      (json \ "dentalServiceId").asOpt[String],
      (json \ "firstName").asOpt[String],
      (json \ "middleName").asOpt[String],
      (json \ "lastName").asOpt[String],
      (json \ "dentistId").asOpt[String],
      (json \ "contactNo").asOpt[String],
      (json \ "address").asOpt[String],
      (json \ "dateStart").asOpt[String],
      (json \ "dateEnd").asOpt[String]
    )

    def writes(p: AppointmentList): JsValue = JsObject(
      Seq(
        "id" -> JsString(p.id),
        "dentalServiceId" -> JsString(p.dentalServiceId.get),
        "firstName" -> JsString(p.firstName.get),
        "middleName" -> JsString(p.middleName.get),
        "lastName" -> JsString(p.lastName.get),
        "dentistId" -> JsString(p.dentistId.get),
        "contactNo" -> JsString(p.contactNo.get),
        "address" -> JsString(p.address.get),
        "dateStart" -> JsString(p.dateStart.get),
        "dateEnd" -> JsString(p.dateEnd.get)
      )
    )
  }

  implicit object AppointmentDetailsFormat extends Format[AppointmentDetails]{
    def reads(json: JsValue): AppointmentDetails = AppointmentDetails(
      AppointmentList(
        (json \ "id").as[String],
        (json \ "dentalServiceId").asOpt[String],
        (json \ "firstName").asOpt[String],
        (json \ "middleName").asOpt[String],
        (json \ "lastName").asOpt[String],
        (json \ "dentistId").asOpt[String],
        (json \ "contactNo").asOpt[String],
        (json \ "address").asOpt[String],
        (json \ "dateStart").asOpt[String],
        (json \ "dateEnd").asOpt[String]
      ),
      (json \ "dFirstName").asOpt[String],
      (json \ "dMiddleName").asOpt[String],
      (json \ "dLastName").asOpt[String],
      (json \ "dentalServiceName").asOpt[String]
    )

    def writes(ad: AppointmentDetails): JsValue =
      JsArray(
        Seq(
          JsObject(
            Seq(
              "id" -> JsString(ad.a.id),
              "dentalServiceId" -> JsString(ad.a.dentalServiceId.get),
              "firstName" -> JsString(ad.a.firstName.get),
              "middleName" -> JsString(ad.a.middleName.get),
              "lastName" -> JsString(ad.a.lastName.get),
              "dentistId" -> JsString(ad.a.dentistId.get),
              "contactNo" -> JsString(ad.a.contactNo.get),
              "address" -> JsString(ad.a.address.get),
              "dateStart" -> JsString(ad.a.dateStart.get),
              "dateEnd" -> JsString(ad.a.dateEnd.get)
            )
          ), JsObject(Seq(
            "dFirstName" -> JsString(ad.dFirstName.get),
            "dMiddleName" -> JsString(ad.dMiddleName.get),
            "dLastName" -> JsString(ad.dLastName.get),
            "dentalServiceName" -> JsString(ad.dentalServiceName.get)
            ))
        )
      )
  }

}


