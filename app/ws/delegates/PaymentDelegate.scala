package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import ws.services.PaymentList
import play.api.libs.ws.Response

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

