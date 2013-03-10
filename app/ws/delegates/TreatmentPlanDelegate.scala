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

  def updateTeethNaming(params: JsValue) = {
    val res = doPost("/json/settings/teeth_naming/update", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def getTreatmentPlan(patientId: String, start: Int, count: Int): List[TreatmentPlanType] = {
    val res: Promise[Response] = doGet("/json/treatment_plan/"+patientId+"?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val tp = ListBuffer[TreatmentPlanType]()

    //println(json) TODO print the json of Treatmentplan

    (json \ "TreatmentPlan").as[Seq[JsObject]].map({
      t =>
        tp += convertToTreatmentPlan(t)
    })
    tp.toList
    //List(TreatmentPlanType("1","2","3","4","5","6","7","8","9","10","11","12","13", "14", "15", "16"))
  }

  def convertToTreatmentPlan(j: JsValue): TreatmentPlanType = {
    new TreatmentPlanType(
      (j \ "id").as[String],
      (j \ "serviceId").asOpt[String],
      (j \ "serviceName").asOpt[String],
      (j \ "serviceCode").asOpt[String],
      (j \ "toolType").asOpt[String],
      (j \ "serviceType").asOpt[String],
      (j \ "servicePrice").asOpt[String],
      (j \ "color").asOpt[String],
      (j \ "datePerformed").asOpt[String],
      (j \ "teethId").asOpt[String],
      (j \ "teethName").asOpt[String],
      (j \ "teethView").asOpt[String],
      (j \ "teethPosition").asOpt[String],
      (j \ "teethType").asOpt[String],
      (j \ "patientId").asOpt[String],
      (j \ "dentistId").asOpt[String],
      (j \ "dentistName").asOpt[String],
      (j \ "image").asOpt[String],
      (j \ "imageTemplate").asOpt[String]
    )
  }

}
