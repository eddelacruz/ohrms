package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.libs.ws.Response
import ws.services.TreatmentPlanType

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 12/17/12
 * Time: 7:32 PM
 * To change this template use File | Settings | File Templates.
 */
object TreatmentPlanDelegate extends WsHelper{

  def addTreatment(params: JsValue) = {
    val res = doPost("/json/treatment_plan", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def getTreatmentPlan(start: Int, count: Int): List[TreatmentPlanType] = {
    val res: Promise[Response] = doGet("/json/treatment_plan?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val tp = ListBuffer[TreatmentPlanType]()

    //println(json) TODO print the json of Treatmentplan

    (json \ "TreatmentPlan").as[Seq[JsObject]].map({
      t =>
        tp += convertToTreatmentPlan(t)
    })
    tp.toList
  }

  def convertToTreatmentPlan(j: JsValue): TreatmentPlanType = {
    new TreatmentPlanType(
      (j \ "id").as[String],
      (j \ "serviceId").as[String],
      (j \ "serviceName").as[String],
      (j \ "serviceCode").as[String],
      (j \ "toolType").as[String],
      (j \ "serviceType").as[String],
      (j \ "servicePrice").as[String],
      (j \ "color").as[String],
      (j \ "datePerformed").as[String],
      (j \ "teethName").as[String],
      (j \ "teethView").as[String],
      (j \ "teethPosition").as[String],
      (j \ "teethType").as[String]
    )
  }

}
