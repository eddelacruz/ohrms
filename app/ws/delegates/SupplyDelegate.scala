package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import ws.services.SupplyList
import play.api.libs.ws.Response

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 3/6/13
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
object SupplyDelegate extends WsHelper{

  val _supplyProfileForm = Form(
    mapping(
      "id" -> text,
      "patient_id" -> optional(text),
      "name" -> optional(text),
      "description" -> optional(text),
      "quantity" -> optional(text),
      "price" -> optional(text)
    )(SupplyList.apply)(SupplyList.unapply)
  )

  def getSupplyList(start: Int, count: Int) = {
    val res: Promise[Response] = doGet("/json/dental_supplies?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[SupplyList]()

    (json \ "SupplyList").as[Seq[JsObject]].map({
      d =>
        dl += convertToSupplyList(d)
    })
    dl.toList
  }

  def searchSupplyList(start: Int, count: Int, filter: String) = {
    val res: Promise[Response] = doGet("/json/dental_supplies/search?start="+start+"&count="+count+"&filter="+filter)
    val json: JsValue = res.await.get.json
    val sdl = ListBuffer[SupplyList]()

    (json \ "SupplyList").as[Seq[JsObject]].map({
      sd =>
        sdl += convertToSupplyList(sd)
    })
    sdl.toList
  }


  def convertToSupplyList (j: JsValue): SupplyList = {
    new SupplyList(
      (j \ "id").as[String],
      (j \ "patientId").asOpt[String],
      (j \ "name").asOpt[String],
      (j \ "description").asOpt[String],
      (j \ "quantity").asOpt[String],
      (j \ "price").asOpt[String]
    )
  }


  def getSupplyById(id: String) = {
    val res: Promise[Response] = doGet("/json/dental_supplies/%s" format(id))
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[SupplyList]()

    (json \ "SupplyList").as[Seq[JsObject]].map({
      d =>
        dl += convertToSupplyList(d)
    })
    dl.toList
  }

  def submitUpdateSupplyForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/dental_supplies/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }


  def submitAddSupplyForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/dental_supplies", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

}
