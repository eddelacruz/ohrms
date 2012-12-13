package controllers

import play.api._
import libs.json.JsObject
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import views.html.{patient, modal}
import ws.services._
import ws.generator.UUIDGenerator
import ws.helper.WsHelper
import ws.deserializer.json.{AuditLogDeserializer, PatientListDeserializer, DentistListDeserializer}
import collection.mutable.ListBuffer
import ws.services.PatientList
import ws.services.DentistList


/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/10/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
object Json extends Controller with WsHelper with PatientListDeserializer with AuditLogDeserializer with DentistListDeserializer{

  def getPatientList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("PatientList" -> toJson(PatientService.getPatientList(start, count)))))
  }

  def getDentistList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("DentistList" -> toJson(DentistService.getDentistList(start, count)))))
  }

  def getDentistInformationById(id : String) = Action {
    Ok(JsObject(Seq("DentistList" -> toJson(DentistService.getDentistListById(id)))))
  }

  def getPatientTreatmentPlan(id: String) = Action {
    Ok(JsObject(Seq("PatientList" -> toJson(PatientService.getPatientListById(id)))))
  }

  def submitPatientAddForm = Action {
    implicit request =>
      val id = ""
      val firstName = request.body.asFormUrlEncoded.get("first_name").head
      val middleName = request.body.asFormUrlEncoded.get("middle_name").head
      val lastName = request.body.asFormUrlEncoded.get("last_name").head
      val address = request.body.asFormUrlEncoded.get("address").head
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").head
      val dateOfBirth = request.body.asFormUrlEncoded.get("date_of_birth").head
      val image = request.body.asFormUrlEncoded.get("image").head
      val medicalHistoryId = request.body.asFormUrlEncoded.get("medical_history_id").head
      val pl = PatientList("", firstName, middleName, lastName, medicalHistoryId, address, contactNo, dateOfBirth, image)

      if (PatientService.addPatient(pl) >= 1) {
        Redirect("/patients")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }

  def auditLog = Action {
    Ok(JsObject(Seq("AuditLog" -> toJson(AuditLogService.getAllLogs()))))
  }

  def submitPatientUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
      val firstName = request.body.asFormUrlEncoded.get("first_name").head
      val middleName = request.body.asFormUrlEncoded.get("middle_name").head
      val lastName = request.body.asFormUrlEncoded.get("last_name").head
      val address = request.body.asFormUrlEncoded.get("address").head
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").head
      val dateOfBirth = request.body.asFormUrlEncoded.get("date_of_birth").head
      val image = request.body.asFormUrlEncoded.get("image").head
      val medicalHistoryId = request.body.asFormUrlEncoded.get("medical_history_id").head
      val pl = PatientList(id, firstName, middleName, lastName, medicalHistoryId, address, contactNo, dateOfBirth, image)

      if (PatientService.updatePatient(pl) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  def submitDentistUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
      val firstName = request.body.asFormUrlEncoded.get("first_name").head
      val middleName = request.body.asFormUrlEncoded.get("middle_name").head
      val lastName = request.body.asFormUrlEncoded.get("last_name").head
      val address = request.body.asFormUrlEncoded.get("address").head
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").head
      val prcNo = request.body.asFormUrlEncoded.get("prc_no").head
      val image = request.body.asFormUrlEncoded.get("image").head
      val sn = request.body.asFormUrlEncoded.get("service_name").head
      val userName = request.body.asFormUrlEncoded.get("user_name").head
      val password = request.body.asFormUrlEncoded.get("password").head
      var specializationName: Seq[String] = Seq(sn)
      val dl = DentistList(id, firstName, middleName, lastName, address, contactNo, prcNo, image, userName, password, specializationName)

      if (DentistService.updateDentist(dl) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  def submitDentistAddForm = Action {
    implicit request =>
      val specializationList = Seq[String]()
      val id = UUIDGenerator.generateUUID("dentists")
      val firstName = request.body.asFormUrlEncoded.get("first_name").head
      val middleName = request.body.asFormUrlEncoded.get("middle_name").head
      val lastName = request.body.asFormUrlEncoded.get("last_name").head
      val address = request.body.asFormUrlEncoded.get("address").head
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").head
      val prcNo = request.body.asFormUrlEncoded.get("prc_no").head
      val image = request.body.asFormUrlEncoded.get("image").head
      val userName = request.body.asFormUrlEncoded.get("user_name").head
      val password = request.body.asFormUrlEncoded.get("password").head
      val dl = DentistList(id, firstName, middleName, lastName, address, contactNo, prcNo, image, userName, password, specializationList)

      var index = 0
      if (DentistService.addDentist(dl) >= 1) {
        try{
          while (request.body.asFormUrlEncoded.get("specializationName["+index+"]") != null) {
            val specializationName = request.body.asFormUrlEncoded.get("specializationName["+index+"]").head
            val dentistId = dl.id
            val sl = new Specialization(dentistId, specializationName)
            index += 1
            DentistService.addSpecialization(sl)
          }
        } catch {
          case e: Exception =>
            println("----->>>>>end of iteration of Specialization<<<<<-----")
        }
        Redirect("/dentists")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }


  def deletePatientInformation = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head

      if (PatientService.deletePatient(id) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

}
