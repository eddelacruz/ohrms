package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import ws.services.StaffList
import play.api.libs.ws.Response

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 12/19/12
 * Time: 12:51 AM
 * To change this template use File | Settings | File Templates.
 */
object StaffDelegate extends WsHelper{

  val _staffProfileForm = Form(
    mapping(
      "id" -> text,
      "first_name" -> optional(text),
      "middle_name" -> optional(text),
      "last_name" -> optional(text),
      "contact_no" -> optional(text),
      "address" -> optional(text),
      "position" -> optional(text),
      "user_name" -> optional(text),
      "password" -> optional(text),
      "question" -> optional(text),
      "answer" -> optional(text)
    )(StaffList.apply)(StaffList.unapply)
  )

  def getStaffList(start: Int, count: Int) = {
    val res: Promise[Response] = doGet("/json/staffs?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[StaffList]()

    (json \ "StaffList").as[Seq[JsObject]].map({
      d =>
        dl += convertToStaffList(d)
    })
    dl.toList
  }


  def convertToStaffList (j: JsValue): StaffList = {
    new StaffList(
      (j \ "id").as[String],
      (j \ "firstName").asOpt[String],
      (j \ "middleName").asOpt[String],
      (j \ "lastName").asOpt[String],
      (j \ "contactNo").asOpt[String],
      (j \ "address").asOpt[String],
      (j \ "position").asOpt[String],
      (j \ "userName").asOpt[String],
      (j \ "password").asOpt[String],
      (j \ "question").asOpt[String],
      (j \ "answer").asOpt[String]
    )
  }


  def getStaffById(id: String) = {
    val res: Promise[Response] = doGet("/json/staffs/%s"format(id))
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[StaffList]()

    (json \ "StaffList").as[Seq[JsObject]].map({
      d =>
        dl += convertToStaffList(d)
    })
    dl.toList
  }

  def searchStaffList(start: Int, count: Int, filter: String) = {
    val res: Promise[Response] = doGet("/json/staffs/search?start="+start+"&count="+count+"&filter="+filter)
    val json: JsValue = res.await.get.json
    val sdl = ListBuffer[StaffList]()

    (json \ "StaffList").as[Seq[JsObject]].map({
      sd =>
        sdl += convertToStaffList(sd)
    })
    sdl.toList
  }

  def submitUpdateStaffForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/staffs/update", params)
    println(params)
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }


  def submitAddStaffForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/staffs", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def deleteInformation(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/staffs/delete", params)
    println()
    println("DELETE Body: >>>>>>>>>>>>>>> " + res.body)
    println("DELETE STATUS: >>>>>>>>>>>>>>> " + res.status)
  }

}
