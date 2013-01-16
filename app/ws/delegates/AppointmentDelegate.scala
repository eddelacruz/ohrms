package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.services.AppointmentList
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.JsObject
import ws.services.AppointmentService
import play.api.libs.ws.Response

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 1/16/13
 * Time: 8:24 AM
 * To change this template use File | Settings | File Templates.
 */

object AppointmentDelegate extends WsHelper {

  val _appointmentProfileForm = Form(
    mapping(
      "id" -> text,
      "description" -> text,
      "first_name" -> text,
      "middle_name" -> text,
      "last_name" -> text,
      "dentist_id" -> text,
      "contact_no" -> text,
      "address" -> text,
      "status" -> number,
      "date_start" -> text,
      "date_end" -> text
    )(AppointmentList.apply)(AppointmentList.unapply)
  )

  /*def getAllLogs(start: Int, count: Int) = {
    val res: Promise[Response] = doGet("/json/settings/audit_log")
    val json: JsValue = res.await.get.json
    val al = ListBuffer[AuditLog]()

    (json \ "AuditLog").as[Seq[JsObject]].map({
      a =>
        al += convertToAuditLog(a)
    })
    al.toList
  }*/

  def convertToAppointmentList (j: JsValue): AppointmentList = {
    new AppointmentList(
      (j \ "id").as[String],
      (j \ "description").as[String],
      (j \ "firstName").as[String],
      (j \ "middleName").as[String],
      (j \ "lastName").as[String],
      (j \ "dentistId").as[String],
      (j \ "contactNo").as[String],
      (j \ "address").as[String],
      (j \ "status").as[Int],
      (j \ "dateStart").as[String],
      (j \ "dateEnd").as[String]
    )
  }

  def submitAddAppointmentsForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/scheduler", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }
}

