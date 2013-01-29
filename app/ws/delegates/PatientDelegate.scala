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
import ws.services.PatientList
import play.api.libs.ws.Response

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/10/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
object PatientDelegate extends WsHelper{

  val _patientProfileForm = Form(
    mapping(
      "id" -> text,
      "first_name" -> optional(text),
      "middle_name" -> optional(text),
      "last_name" -> optional(text),
      "address" -> optional(text),
      "contact_no" -> optional(text),
      "date_of_birth" -> optional(text),
      "image" -> optional(text),
      "medical_history" -> optional(text)
    )(PatientList.apply)(PatientList.unapply)
  )

  def getPatientList(start: Int, count: Int) = {
    val res: Promise[Response] = doGet("/json/patients?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val pl = ListBuffer[PatientList]()

    (json \ "PatientList").as[Seq[JsObject]].map({
      p =>
       pl += convertToPatientList(p)
    })
    pl.toList
  }

  def convertToPatientList (j: JsValue): PatientList = {
    new PatientList(
      (j \ "id").as[String],
      (j \ "firstName").asOpt[String],
      (j \ "middleName").asOpt[String],
      (j \ "lastName").asOpt[String],
      (j \ "address").asOpt[String],
      (j \ "contactNo").asOpt[String],
      (j \ "dateOfBirth").asOpt[String],
      (j \ "image").asOpt[String],
      (j \ "medicalHistory").asOpt[String]
    )
  }


  def searchPatientListByLastName(start: Int, count: Int, filter: String) = {
    val res: Promise[Response] = doGet("/json/patients/search?start="+start+"&count="+count+"&filter="+filter)
    val json: JsValue = res.await.get.json
    val spl = ListBuffer[PatientList]()

    (json \ "PatientList").as[Seq[JsObject]].map({
      sp =>
        spl += convertToPatientList(sp)
    })
    spl.toList
  }

  def getPatientListById(id: String) = {
    val res: Promise[Response] = doGet("/json/patients/"+id+"/treatment_plan")
    val json: JsValue = res.await.get.json
    val pl = ListBuffer[PatientList]()

    (json \ "PatientList").as[Seq[JsObject]].map({
      p =>
        pl += convertToPatientList(p)
    })
    pl.toList
  }

  def submitAddPatientForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/patients", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def searchPatientForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/patients", params)
    println()
    println("POST STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("POST BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def submitUpdatePatientForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/patients/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }

  def deleteInformation(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/patients/delete", params)
    println()
    println("DELETE Body: >>>>>>>>>>>>>>> " + res.body)
    println("DELETE STATUS: >>>>>>>>>>>>>>> " + res.status)
  }

}
