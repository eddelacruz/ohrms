package ws.delegates


import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import ws.services.{IncomeList, PaymentList}
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
      "first_name" -> text,
      "last_name" -> text,
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
      (j \ "firstName").as[String],
      (j \ "lastName").as[String],
      (j \ "payment").asOpt[String],
      (j \ "dateOfPayment").asOpt[String],
      (j \ "userName").asOpt[String]
    )
  }

  def convertToIncomeList (j: JsValue): IncomeList = {
    new IncomeList(
      (j \ "id").as[String],
      (j \ "firstName").asOpt[String],
      (j \ "lastName").asOpt[String],
      (j \ "datePerformed").asOpt[String],
      (j \ "price").asOpt[String],
      (j \ "serviceName").asOpt[String]
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

  def getPaymentById(id: String) = {
    val res: Promise[Response] = doGet("/json/reports/payment_receipt/"+id)
    val json: JsValue = res.await.get.json
    val pl = ListBuffer[PaymentList]()

    (json \ "PaymentList").as[Seq[JsObject]].map({
      p =>
        pl += convertToPaymentList(p)
    })
    pl.toList
  }

  def getMonthlyIncome(year: Int, month: Int) = {
    val res: Promise[Response] = doGet("/json/monthly_income/"+year+"/"+month)
    val json: JsValue = res.await.get.json
    val pl = ListBuffer[IncomeList]()

    (json \ "IncomeList").as[Seq[JsObject]].map({
      p =>
        pl += convertToIncomeList(p)
    })
    pl.toList
  }
}

