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
      "first_name" -> text,
      "middle_name" -> text,
      "last_name" -> text,
      "medical_history_id" -> text,
      "address" -> text,
      "contact_no" -> text,
      "date_of_birth" -> text,
      "image" -> text
    )(PatientList.apply)(PatientList.unapply)
  )

  def getPatientList = {
    val res: Promise[Response] = doGet("/json/patients")
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
      (j \ "firstName").as[String],
      (j \ "middleName").as[String],
      (j \ "lastName").as[String],
      (j \ "medicalHistoryId").as[String],
      (j \ "address").as[String],
      (j \ "contactNo").as[String],
      (j \ "dateOfBirth").as[String],
      (j \ "image").as[String]
    )
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

  def submitUpdatePatientForm(params: Map[String, Seq[String]]) = {
    val res = doPost("/json/patients/update", params)
    println()
    println("PUT STATUS: >>>>>>>>>>>>>>> " + res.status)
    println("PUT BODY: >>>>>>>>>>>>>>> " + res.body)
  }


}
