package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.libs.ws.Response

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

}
