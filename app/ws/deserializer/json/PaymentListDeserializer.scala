package ws.deserializer.json

import play.api.libs.json._
import ws.services.PaymentList

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 2/21/13
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */

trait PaymentListDeserializer {

  implicit object PaymentListFormat extends Format[PaymentList]{
    def reads(json: JsValue): PaymentList = PaymentList(
      (json \ "id").as[String],
      (json \ "patientId").asOpt[String],
      (json \ "payment").asOpt[String],
      (json \ "dateOfPayment").asOpt[String],
      (json \ "userName").asOpt[String]
    )

    def writes(d: PaymentList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "patientId" -> JsString(d.patientId.get),
        "payment" -> JsString(d.payment.get),
        "dateOfPayment" -> JsString(d.dateOfPayment.get),
        "userName" -> JsString(d.userName.get)
      )
    )
  }
}

