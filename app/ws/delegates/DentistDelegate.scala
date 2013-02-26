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
      "first_name" -> optional(text),
      "middle_name" -> optional(text),
      "last_name" -> optional(text),
      "address" -> optional(text),
      "contact_no" -> optional(text),
      "prc_no" -> optional(text),
      "user_name" -> optional(text),
      "password" -> optional(text),
      "specialization_name" -> optional(seq(text)),
      "question" -> optional(text),
      "answer" -> optional(text)
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
      (j \ "firstName").asOpt[String],
      (j \ "middleName").asOpt[String],
      (j \ "lastName").asOpt[String],
      (j \ "address").asOpt[String],
      (j \ "contactNo").asOpt[String],
      (j \ "prcNo").asOpt[String],
      (j \ "userName").asOpt[String],
      (j \ "password").asOpt[String],
      (j \ "specializationName").asOpt[Seq[String]],
      (j \ "question").asOpt[String],
      (j \ "answer").asOpt[String]
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

  def getAllDentists() = {
    val res: Promise[Response] = doGet("/json/dentists/all")
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[DentistList]()

    (json \ "DentistList").as[Seq[JsObject]].map({
      d =>
        dl += convertToDentistList(d)
    })
    dl.toList
  }
}