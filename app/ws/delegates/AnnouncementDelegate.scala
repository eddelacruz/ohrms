package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import ws.services.AnnouncementList
import play.api.libs.ws.Response

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 1/22/13
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */

object AnnouncementDelegate extends WsHelper{

  val _announcementProfileForm = Form(
    mapping(
      "id" -> text,
      "user_name" -> optional(text),
      "announcement" -> optional(text),
      "date_created" -> optional(text)
    )(AnnouncementList.apply)(AnnouncementList.unapply)
  )

  def getAnnouncementList(start: Int, count: Int) = {
    val res: Promise[Response] = doGet("/json/announcements?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[AnnouncementList]()


    (json \ "AnnouncementList").as[Seq[JsObject]].map({
      d =>
        dl += convertToAnnouncementList(d)
    })
    dl.toList
  }

  def searchAnnouncementList(start: Int, count: Int, filter: String) = {
    val res: Promise[Response] = doGet("/json/announcements/search?start="+start+"&count="+count+"&filter="+filter)
    val json: JsValue = res.await.get.json
    val sdl = ListBuffer[AnnouncementList]()

    (json \ "AnnouncementList").as[Seq[JsObject]].map({
      sd =>
        sdl += convertToAnnouncementList(sd)
    })
    sdl.toList
  }


  def convertToAnnouncementList (j: JsValue): AnnouncementList = {
    new AnnouncementList(
      (j \ "id").as[String],
      (j \ "username").asOpt[String],
      (j \ "description").asOpt[String],
      (j \ "dateCreated").asOpt[String]
    )
  }


  def getAnnouncementById(id: String) = {
    val res: Promise[Response] = doGet("/json/announcements/%s" format(id))
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[AnnouncementList]()

    (json \ "AnnouncementList").as[Seq[JsObject]].map({
      d =>
        dl += convertToAnnouncementList(d)
    })
    dl.toList
  }

  def submitUpdateAnnouncementForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/announcements/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }


  def submitAddAnnouncementForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/announcements", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def deleteInformation(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/announcements/delete", params)
    println()
    println("DELETE Body: >>>>>>>>>>>>>>> " + res.body)
    println("DELETE STATUS: >>>>>>>>>>>>>>> " + res.status)
  }

}

