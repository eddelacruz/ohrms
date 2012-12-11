package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.services.PatientList
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.JsObject
import ws.services.AuditLog
import play.api.libs.ws.Response

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/10/12
 * Time: 10:10 PM
 * To change this template use File | Settings | File Templates.
 */
object AuditLogDelegate extends WsHelper{

  def getAllLogs = {
    val res: Promise[Response] = doGet("/json/settings/audit_log")
    val json: JsValue = res.await.get.json
    val al = ListBuffer[AuditLog]()

    (json \ "AuditLog").as[Seq[JsObject]].map({
      a =>
        al += convertToAuditLog(a)
    })
    al.toList
  }

  def convertToAuditLog(j: JsValue): AuditLog = {
    new AuditLog(
      (j \ "id").as[String],
      (j \ "task").as[String],
      (j \ "description").as[String],
      (j \ "dateCreated").as[String],
      (j \ "author").as[String]
    )
  }

}