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
      "first_name" -> text,
      "middle_name" -> text,
      "last_name" -> text,
      "contactNo" -> text,
      "address" -> text,
      "position" -> text,
      "user_name" -> text,
      "password" -> text
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
      (j \ "firstName").as[String],
      (j \ "middleName").as[String],
      (j \ "lastName").as[String],
      (j \ "contactNo").as[String],
      (j \ "address").as[String],
      (j \ "position").as[String],
      (j \ "userName").as[String],
      (j \ "password").as[String]
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

  def submitUpdateStaffForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/staffs/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }


  def submitAddStaffForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/staffs", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }



}
