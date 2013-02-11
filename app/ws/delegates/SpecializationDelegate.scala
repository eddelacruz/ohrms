package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import ws.services.SpecializationList
import play.api.libs.ws.Response

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 1/22/13
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */

object SpecializationDelegate extends WsHelper{
  val _specializationProfileForm = Form(
    mapping(
      "id" -> text,
      "dentist_id" -> text,
      "name" -> text
    )(SpecializationList.apply)(SpecializationList.unapply)
  )

  def getSpecializationList(start: Int, count: Int) = {
    val res: Promise[Response] = doGet("/json/specializations?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[SpecializationList]()

    (json \ "SpecializationList").as[Seq[JsObject]].map({
      d =>
        dl += convertToSpecializationList(d)
    })
    dl.toList
  }

  def searchSpecializationList(start: Int, count: Int, filter: String) = {
    val res: Promise[Response] = doGet("/json/specializations/search?start="+start+"&count="+count+"&filter="+filter)
    val json: JsValue = res.await.get.json
    val sdl = ListBuffer[SpecializationList]()

    (json \ "SpecializationList").as[Seq[JsObject]].map({
      sd =>
        sdl += convertToSpecializationList(sd)
    })
    sdl.toList
  }


  def convertToSpecializationList (j: JsValue): SpecializationList = {
    new SpecializationList(
      (j \ "id").as[String],
      (j \ "dentistId").as[String],
      (j \ "name").as[String]
    )
  }


  def getSpecializationById(id: String) = {
    val res: Promise[Response] = doGet("/json/specializations/%s" format(id))
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[SpecializationList]()

    (json \ "SpecializationList").as[Seq[JsObject]].map({
      d =>
        dl += convertToSpecializationList(d)
    })
    dl.toList
  }

  def submitUpdateSpecializationForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/specializations/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }


  def submitAddSpecializationForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/specializations", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def deleteInformation(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/specializations/delete", params)
    println()
    println("DELETE Body: >>>>>>>>>>>>>>> " + res.body)
    println("DELETE STATUS: >>>>>>>>>>>>>>> " + res.status)
  }

}

