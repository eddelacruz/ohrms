package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import ws.services.ClinicList
import play.api.libs.ws.Response

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 1/22/13
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */

object ClinicDelegate extends WsHelper{
  val _clinicProfileForm = Form(
    mapping(
      "id" -> text,
      "clinic_name" -> optional(text),
      "address" -> optional(text),
      "image" -> optional(text),
      "user_name" -> optional(text),
      "contact_number" -> optional(text)
    )(ClinicList.apply)(ClinicList.unapply)
  )

  def getClinicList(start: Int, count: Int) = {
    val res: Promise[Response] = doGet("/json/clinic?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[ClinicList]()

    (json \ "ClinicList").as[Seq[JsObject]].map({
      d =>
        dl += convertToClinicList(d)
    })
    dl.toList
  }

  def searchClinicList(start: Int, count: Int, filter: String) = {
    val res: Promise[Response] = doGet("/json/clinic/search?start="+start+"&count="+count+"&filter="+filter)
    val json: JsValue = res.await.get.json
    val sdl = ListBuffer[ClinicList]()

    (json \ "ClinicList").as[Seq[JsObject]].map({
      sd =>
        sdl += convertToClinicList(sd)
    })
    sdl.toList
  }


  def convertToClinicList (j: JsValue): ClinicList = {
    new ClinicList(
      (j \ "id").as[String],
      (j \ "clinicName").asOpt[String],
      (j \ "address").asOpt[String],
      (j \ "image").asOpt[String],
      (j \ "user_name").asOpt[String],
      (j \ "contact_number").asOpt[String]
    )
  }


  def getClinicById(id: String) = {
    val res: Promise[Response] = doGet("/json/clinic/%s" format(id))
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[ClinicList]()

    (json \ "ClinicList").as[Seq[JsObject]].map({
      d =>
        dl += convertToClinicList(d)
    })
    dl.toList
  }

  def submitUpdateClinicForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/clinic/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }


  def submitAddClinicForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/clinic", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

}

