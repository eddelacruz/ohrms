package ws.delegates

import play.api.libs.concurrent.Promise
import play.api.libs.ws._
import play.api.libs.json.{JsArray, JsString, JsValue, JsObject}
import collection.mutable.ListBuffer
import ws.services.{PatientLastVisit, PatientList}
import ws.helper.WsHelper
import play.api.data.Form
import play.api.data.Forms._
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
      "medical_history" -> optional(text),
      "gender" -> text
    )(PatientList.apply)(PatientList.unapply)
  )

  def getPatientLastVisit(start: Int, count: Int) = {
    val res: Promise[Response] = doGet("/json/patients/last_visit?start="+start+"&count="+count)
    val json: JsValue = res.await.get.json
    val pl = ListBuffer[PatientLastVisit]()

    (json \ "PatientList").as[Seq[Seq[JsObject]]].map({
      p =>
        pl += convertToPatientLastVisit(p)
    })
    //println(">>ETO UNG DAHILAn"+(json \ "PatientList").map(kuma => convertToPatientLastVisit(kuma)))
    pl.toList
  }

  def searchPatientLastVisit(start: Int, count: Int, filter: String) = {
    val res: Promise[Response] = doGet("/json/patients/last_visit/search?start="+start+"&count="+count+"&filter="+filter)
    val json: JsValue = res.await.get.json
    val spl = ListBuffer[PatientLastVisit]()

    (json \ "PatientList").as[Seq[Seq[JsObject]]].map({
      sp =>
        spl += convertToPatientLastVisit(sp)
    })
    spl.toList
  }

  def convertToPatientLastVisit(json: Seq[JsValue]): PatientLastVisit = {
    //println(json.tail.headOption.get \ "dateLastVisit")
    //println(json \ "dateLastVisit")
    new PatientLastVisit(
      new PatientList(
        (json.head \ "id").as[String],//(json \ "id").as[String],
        (json.headOption.get \ "firstName").asOpt[String],
        (json.headOption.get \ "middleName").asOpt[String],
        (json.headOption.get \ "lastName").asOpt[String],
        (json.headOption.get \ "address").asOpt[String],
        (json.headOption.get \ "contactNo").asOpt[String],
        (json.headOption.get \ "dateOfBirth").asOpt[String],
        (json.headOption.get \ "medicalHistory").asOpt[String],
        (json.head\ "gender").as[String]
      ), (json.tail.headOption.get \ "dateLastVisit").asOpt[String]
    )
  }

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

  def getAllPatients() = {
    val res: Promise[Response] = doGet("/json/patients/all")
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
      (j \ "medicalHistory").asOpt[String],
      (j \ "gender").as[String]
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

  def getPatientListByDateRange(startDate: String, endDate: String) = {
    val res: Promise[Response] = doGet("/json/patients/"+startDate+"/"+endDate)
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
