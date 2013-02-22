package ws.delegates


import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import ws.services.PaymentList
import ws.services.PaymentDetails
import play.api.libs.ws.Response
import play.api.data.format.Formatter
import play.api.data.Mapping
import play.api.data.format.Formats._
import play.api.data.FormError
import play.api.data.validation.Constraint
import play.api.data.validation.Invalid
import play.api.data.validation.Valid
import play.api.data.validation.ValidationError
import play.api.data.validation.Constraints


/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 2/21/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */

object PaymentDelegate extends WsHelper{

  val _paymentProfileForm = Form(
    mapping(
      "id" -> text,
      "patient_id" -> optional(text),
      "payment" -> optional(text),
      "date_of_payment" -> optional(text),
      "user_name" -> optional(text)
    )(PaymentList.apply)(PaymentList.unapply)
  )

  def getPaymentsByPatientId(start: Int, count: Int, patientId: String) = {
    val res: Promise[Response] = doGet("/json/payments/"+patientId+"?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[PaymentList]()

    (json \ "PaymentList").as[Seq[JsObject]].map({
      d =>
        dl += convertToPaymentList(d)
    })
    dl.toList
  }


  def convertToPaymentList (j: JsValue): PaymentList = {
    new PaymentList(
      (j \ "id").as[String],
      (j \ "patientId").asOpt[String],
      (j \ "payment").asOpt[String],
      (j \ "dateOfPayment").asOpt[String],
      (j \ "userName").asOpt[String]
    )
  }

  def convertToPaymentDetails(json: Seq[JsValue]): PaymentDetails = {
    new PaymentDetails(
      new PaymentList(
        (json.head \ "id").as[String],
        (json.headOption.get \ "patientId").asOpt[String],
        (json.headOption.get \ "payment").asOpt[String],
        (json.headOption.get \ "dateOfPayment").asOpt[String],
        (json.headOption.get \ "userName").asOpt[String]
      ), (json.tail.headOption.get \ "totalPayment").asOpt[Double],
        (json.tail.headOption.get \ "balance").asOpt[Double],
        (json.tail.headOption.get \ "totalPrice").asOpt[Double]
    )
  }

  def submitUpdatePaymentForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/payments/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def submitAddPaymentForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/payments", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

}

