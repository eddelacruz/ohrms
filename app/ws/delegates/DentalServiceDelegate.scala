package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import ws.services.DentalServiceList
import play.api.libs.ws.Response

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 12/18/12
 * Time: 8:58 PM
 * To change this template use File | Settings | File Templates.
 */
object DentalServiceDelegate extends WsHelper{

  val _dentalServiceProfileForm = Form(
    mapping(
      "id" -> text,
      "name" -> optional(text),
      "code" -> optional(text),
      "type" -> optional(text),
      "target" -> optional(number),
      "price" -> optional(text),
      "color" -> optional(text),
      "image_template" -> optional(text)
    )(DentalServiceList.apply)(DentalServiceList.unapply)
  )

  def searchServiceList(start: Int, count: Int, filter: String) = {
    val res: Promise[Response] = doGet("/json/dental_services/search?start="+start+"&count="+count+"&filter="+filter)
    val json: JsValue = res.await.get.json
    val sdsl = ListBuffer[DentalServiceList]()

    (json \ "DentalServiceList").as[Seq[JsObject]].map({
      sds =>
        sdsl += convertToDentalServiceList(sds)
    })
    sdsl.toList
  }

  def getDentalServiceList(start: Int, count: Int) = {
    val res: Promise[Response] = doGet("/json/dental_services?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[DentalServiceList]()

    (json \ "DentalServiceList").as[Seq[JsObject]].map({
      d =>
        dl += convertToDentalServiceList(d)
    })
    dl.toList
  }

  def getAllDentalServiceList() = {
    val res: Promise[Response] = doGet("/json/dental_services")
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[DentalServiceList]()

    (json \ "DentalServiceList").as[Seq[JsObject]].map({
      d =>
        dl += convertToDentalServiceList(d)
    })
    dl.toList
  }

  def convertToDentalServiceList (j: JsValue): DentalServiceList = {
    new DentalServiceList(
      (j \ "id").as[String],
      (j \ "name").asOpt[String],
      (j \ "code").asOpt[String],
      (j \ "type").asOpt[String],
      (j \ "tool_type").asOpt[Int],
      (j \ "price").asOpt[String],
      (j \ "color").asOpt[String],
      (j \ "image_template").asOpt[String]
    )
  }


  def getDentalServiceInformationById(id: String) = {
    val res: Promise[Response] = doGet("/json/dental_services/%s/information" format(id))
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[DentalServiceList]()

    (json \ "DentalServiceList").as[Seq[JsObject]].map({
      d =>
        dl += convertToDentalServiceList(d)
    })
    dl.toList
  }

  def submitAddDentalServiceForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/dental_services", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def submitUpdateDentalServiceForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/dental_services/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def deleteInformation(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/dental_services/delete", params)
    println()
    println("DELETE Body: >>>>>>>>>>>>>>> " + res.body)
    println("DELETE STATUS: >>>>>>>>>>>>>>> " + res.status)
  }

}
