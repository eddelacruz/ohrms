package ws.deserializer.json

import play.api.libs.json._
import ws.services.PaymentList
import ws.services.PaymentDetails
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

  implicit object PaymentDetailsFormat extends Format[PaymentDetails]{
    def reads(json: JsValue): PaymentDetails = PaymentDetails(
      PaymentList(
        (json \ "id").as[String],
        (json \ "patientId").asOpt[String],
        (json \ "payment").asOpt[String],
        (json \ "dateOfPayment").asOpt[String],
        (json \ "userName").asOpt[String]
    ),  (json \ "totalPayment").asOpt[Double],
        (json \ "balance").asOpt[Double],
        (json \ "totalPrice").asOpt[Double]
   )

    def writes(pd: PaymentDetails): JsValue =
      JsArray(
        Seq(
          JsObject(
            Seq(
              "id" -> JsString(pd.p.id),
              "patientId" -> JsString(pd.p.patientId.get),
              "payment" -> JsString(pd.p.payment.get),
              "dateOfPayment" -> JsString(pd.p.dateOfPayment.get),
              "userName" -> JsString(pd.p.userName.get)
            )
          ), JsObject(
              Seq(
              "totalPayment" -> JsNumber(pd.totalPayment.get),
              "balance" -> JsNumber(pd.balance.get),
              "totalPrice" -> JsNumber(pd.totalPrice.get)
              )
          )
       )
      )
  }
}

