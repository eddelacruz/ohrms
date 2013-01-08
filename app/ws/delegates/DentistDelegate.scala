package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import ws.services.DentistList
import play.api.libs.ws.Response

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/10/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
object DentistDelegate extends WsHelper{

  val _dentistProfileForm = Form(
    mapping(
      "id" -> text,
      "first_name" -> text,
      "middle_name" -> text,
      "last_name" -> text,
      "address" -> text,
      "contact_no" -> text,
      "prc_no" -> text,
      "image" -> text ,
      "user_name" -> text,
      "password" -> text,
      "service_name" -> seq(text)
    )(DentistList.apply)(DentistList.unapply)
  )

  def getDentistList(start: Int, count: Int) = {
    val res: Promise[Response] = doGet("/json/dentists?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[DentistList]()

    (json \ "DentistList").as[Seq[JsObject]].map({
      d =>
        dl += convertToDentistList(d)
    })
    dl.toList
  }

  def searchDentistList(start: Int, count: Int, filter: String) = {
    val res: Promise[Response] = doGet("/json/dentists/search?start="+start+"&count="+count+"&filter="+filter)
    val json: JsValue = res.await.get.json
    val sdl = ListBuffer[DentistList]()

    (json \ "DentistList").as[Seq[JsObject]].map({
      sd =>
        sdl += convertToDentistList(sd)
    })
    sdl.toList
  }


  def convertToDentistList (j: JsValue): DentistList = {
    new DentistList(
      (j \ "id").as[String],
      (j \ "firstName").as[String],
      (j \ "middleName").as[String],
      (j \ "lastName").as[String],
      (j \ "address").as[String],
      (j \ "contactNo").as[String],
      (j \ "prcNo").as[String],
      (j \ "image").as[String],
      (j \ "userName").as[String],
      (j \ "password").as[String],
      (j \ "specializationName").as[Seq[String]]
    )
  }


  def getDentistInformationById(id: String) = {
    val res: Promise[Response] = doGet("/json/dentists/%s/information" format(id))
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[DentistList]()

    (json \ "DentistList").as[Seq[JsObject]].map({
      d =>
        dl += convertToDentistList(d)
    })
    dl.toList
  }


  def submitUpdateDentistForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/dentists/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }


  def submitAddDentistForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/dentists", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def deleteInformation(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/dentists/delete", params)
    println()
    println("DELETE Body: >>>>>>>>>>>>>>> " + res.body)
    println("DELETE STATUS: >>>>>>>>>>>>>>> " + res.status)
  }
}