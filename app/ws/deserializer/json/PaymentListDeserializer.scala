package ws.deserializer.json

import play.api.libs.json._
import ws.services.{PaymentList, IncomeList}
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
      (json \ "firstName").as[String],
      (json \ "lastName").as[String],
      (json \ "payment").asOpt[String],
      (json \ "dateOfPayment").asOpt[String],
      (json \ "userName").asOpt[String]
    )

    def writes(d: PaymentList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "patientId" -> JsString(d.patientId.get),
        "firstName" -> JsString(d.firstName),
        "lastName" -> JsString(d.lastName),
        "payment" -> JsString(d.payment.get),
        "dateOfPayment" -> JsString(d.dateOfPayment.get),
        "userName" -> JsString(d.userName.get)
      )
    )
  }
  case class MonthlyIncome(var id: String, firstName : Option[String], lastName : Option[String], datePerformed : Option[String], price : Option[String],serviceName : Option[String])

  implicit object IncomeListFormat extends Format[IncomeList]{
    def reads(json: JsValue): IncomeList = IncomeList(
      (json \ "id").as[String],
      (json \ "firstName").asOpt[String],
      (json \ "lastName").asOpt[String],
      (json \ "datePerformed").asOpt[String],
      (json \ "price").asOpt[String],
      (json \ "serviceName").asOpt[String]
    )

    def writes(d: IncomeList): JsValue = JsObject(
      Seq(
        "id" -> JsString(d.id),
        "firstName" -> JsString(d.firstName.get),
        "lastName" -> JsString(d.lastName.get),
        "datePerformed" -> JsString(d.datePerformed.get),
        "price" -> JsString(d.price.get),
        "serviceName" -> JsString(d.serviceName.get)
      )
    )
  }
}

