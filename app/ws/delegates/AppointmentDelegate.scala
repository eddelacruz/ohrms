package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.services.{AppointmentList, AppointmentService}
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.JsObject
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
      "dental_service_id" -> optional(text),
      "first_name" -> optional(text),
      "middle_name" -> optional(text),
      "last_name" -> optional(text),
      "dentist_id" -> optional(text),
      "contact_no" -> optional(text),
      "address" -> optional(text),
      "date_start" -> optional(text),
      "date_end" -> optional(text)
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
      (j \ "dentalServiceId").asOpt[String],
      (j \ "firstName").asOpt[String],
      (j \ "middleName").asOpt[String],
      (j \ "lastName").asOpt[String],
      (j \ "dentistId").asOpt[String],
      (j \ "contactNo").asOpt[String],
      (j \ "address").asOpt[String],
      (j \ "dateStart").asOpt[String],
      (j \ "dateEnd").asOpt[String]
    )
  }

  def getAppointmentById(id: String) = {
    val res: Promise[Response] = doGet("/json/appointments/%s" format(id))
    val json: JsValue = res.await.get.json
    val dl = ListBuffer[AppointmentList]()

    (json \ "AppointmentList").as[Seq[JsObject]].map({
      d =>
        dl += convertToAppointmentList(d)
    })
    dl.toList
  }


  def submitAddAppointmentsForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/appointments", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def submitUpdateAppointmentForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/appointments/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def getAppointmentsToday() = {
    val res: Promise[Response] = doGet("/json/appointments/today")
    val json: JsValue = res.await.get.json
    val al = ListBuffer[AppointmentList]()

    (json \ "AppointmentList").as[Seq[JsObject]].map({
      a =>
        al += convertToAppointmentList(a)
    })
    al.toList
  }
}

