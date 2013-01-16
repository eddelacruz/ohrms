package controllers

import play.api._
import libs.json.JsObject
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import ws.services._
import ws.generator.UUIDGenerator
import ws.helper.WsHelper
import ws.deserializer.json.{AuditLogDeserializer, PatientListDeserializer, DentistListDeserializer, DentalServiceListDeserializer, StaffListDeserializer, TreatmentPlanDeserializer, AppointmentDeserializer}
import collection.mutable.ListBuffer
import ws.services.PatientList
import ws.services.DentistList
import ws.services.StaffList


/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/10/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
object Json extends Controller with WsHelper with PatientListDeserializer with AuditLogDeserializer with DentistListDeserializer with DentalServiceListDeserializer with StaffListDeserializer with TreatmentPlanDeserializer with AppointmentDeserializer{

  def getPatientList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("PatientList" -> toJson(PatientService.getPatientList(start, count)))))
  }

  def searchPatientListByLastName(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("PatientList" -> toJson(PatientService.searchPatientListByLastName(start, count, filter)))))
  }

  def searchDentistList(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("DentistList" -> toJson(DentistService.searchDentistList(start, count, filter)))))
  }

  def searchAuditLog(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("AuditLog" -> toJson(AuditLogService.searchAuditLog(start, count, filter)))))
  }

  def searchStaffList(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("StaffList" -> toJson(StaffService.searchStaffList(start, count, filter)))))
  }

  def searchServiceList(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("DentalServiceList" -> toJson(ServicesService.searchServiceList(start, count, filter)))))
  }

  def getDentistList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("DentistList" -> toJson(DentistService.getDentistList(start, count)))))
  }

  def getDentalServiceList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("DentalServiceList" -> toJson(ServicesService.getDentalServiceList(start, count)))))
  }

  def getStaffList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("StaffList" -> toJson(StaffService.getStaffList(start, count)))))
}

  def getStaffById(id : String) = Action {
    Ok(JsObject(Seq("StaffList" -> toJson(StaffService.getStaffListById(id)))))
  }

  def getDentalServiceInformationById(id : String) = Action {
    Ok(JsObject(Seq("DentalServiceList" -> toJson(ServicesService.getDentalServiceListById(id)))))
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

  def auditLog(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("AuditLog" -> toJson(AuditLogService.getAllLogs(start,count)))))
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
            println("----->>>>> (END OF ITERATION OF SPECIALIZATION) <<<<<-----")
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

  def deleteDentistInformation = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head

      if (DentistService.deleteDentist(id) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  /*def addTreatmentPlan = Action {
    implicit request =>
      var index = 0
      val teethStructure = request.body.asJson.get.\("teeth_structure")

      val treatmentPlanId = UUIDGenerator.generateUUID("treatment_plan")
      val serviceId = teethStructure(0).\("service_id").as[String]
      TreatmentPlanService.addTreatment(treatmentPlanId, serviceId)

      try{
        while (teethStructure(index) != null) {
          val tView = teethStructure(index).\("view").as[String]
          val tName = teethStructure(index).\("name").as[String]
          val tType = teethStructure(index).\("type").as[String]
          val tPosition = tName match {
            case "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" => "upper"
            case "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" => "lower"
          }
          // 1 - Inclusive, 2 - Selective Fill, 3 - Selective Root, 4 - Selective Bridge, 5 - Selective Grid
          val target = 2
          val tp = new TreatmentPlanType(treatmentPlanId, "", "", "", target, "", "", "", "", tName, tView, tPosition, tType)
          index += 1
          TreatmentPlanService.addTeethAffected(tp)
        }
      } catch {
        case e: Exception =>
          println("----->>>>> (END OF ITERATION OF TEETH AFFECTED) <<<<<-----")
          println(e)
      }
      println("\n \n----->>>>> (TEETH STRUCTURE JSON) <<<<<-----")
      println(teethStructure)
      Status(200)
  }*/

  def getTreatmentPlan(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("TreatmentPlan" -> toJson(TreatmentPlanService.getTreatmentPlan(start, count)))))
  }

  //TODO donot delete for future use
  /*def addTeethAffected(treatmentPlanId: String, teethStructure: JsValue) = Action{
    println("hoi bruha")
    var index = 0
    try{
      while (teethStructure(index) != null) {
        val tView = teethStructure(index).\("view").as[String]
        val tName = teethStructure(index).\("name").as[String]
        val tType = teethStructure(index).\("type").as[String]
        val tPosition = tName match {
          case "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" => "upper"
          case "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" => "lower"
        }
        // 1 - Inclusive, 2 - Selective Fill, 3 - Selective Root, 4 - Selective Bridge, 5 - Selective Grid
        val target = "2"
        val tp = new TreatmentPlanType(treatmentPlanId, "", "", "", target, "", "", "", "", tName, tView, tPosition, tType)
        index += 1
        TreatmentPlanService.addTeethAffected(tp)
      }
    } catch {
      case e: Exception =>
        println("----->>>>> (END OF ITERATION OF TEETH AFFECTED) <<<<<-----")
        println(e)
    }
    Status(200)
  }*/

  def submitDentalServiceAddForm = Action {
    implicit request =>
      val id = ""
      val name = request.body.asFormUrlEncoded.get("name").head
      val code = request.body.asFormUrlEncoded.get("code").head
      val sType = request.body.asFormUrlEncoded.get("type").head
      val target = request.body.asFormUrlEncoded.get("target").head.toInt
      val price = request.body.asFormUrlEncoded.get("price").head
      val color = request.body.asFormUrlEncoded.get("color").head
      val dl = DentalServiceList("", name, code, sType, target, price, color)

      if (ServicesService.addDentalService(dl) >= 1) {
        Redirect("/dental_services")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }

  def submitDentalServiceUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
      val name = request.body.asFormUrlEncoded.get("name").head
      val code = request.body.asFormUrlEncoded.get("code").head
      val sType = request.body.asFormUrlEncoded.get("type").head
      val target = request.body.asFormUrlEncoded.get("target").head.toInt
      val price = request.body.asFormUrlEncoded.get("price").head
      val color = request.body.asFormUrlEncoded.get("color").head
      val dl = DentalServiceList(id, name, code, sType, target, price, color)

      if (ServicesService.updateDentalService(dl) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }


  def submitStaffAddForm = Action {
    implicit request =>
      val id = ""
      val firstName = request.body.asFormUrlEncoded.get("first_name").head
      val middleName = request.body.asFormUrlEncoded.get("middle_name").head
      val lastName = request.body.asFormUrlEncoded.get("last_name").head
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").head
      val address = request.body.asFormUrlEncoded.get("address").head
      val position = request.body.asFormUrlEncoded.get("position").head
      val userName = request.body.asFormUrlEncoded.get("user_name").head
      val password = request.body.asFormUrlEncoded.get("password").head
      val s = StaffList("", firstName, middleName, lastName, contactNo, address, position, userName, password)

      if (StaffService.addStaff(s) >= 1) {
        Redirect("/staffs")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }


  def submitStaffUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
      val firstName = request.body.asFormUrlEncoded.get("first_name").head
      val middleName = request.body.asFormUrlEncoded.get("middle_name").head
      val lastName = request.body.asFormUrlEncoded.get("last_name").head
      val contactNo = request.body.asFormUrlEncoded.get("contact").head
      val address = request.body.asFormUrlEncoded.get("address").head
      val position = request.body.asFormUrlEncoded.get("position").head
      val userName = request.body.asFormUrlEncoded.get("user_name").head
      val password = request.body.asFormUrlEncoded.get("password").head
      val s = StaffList(id, firstName, middleName, lastName, contactNo, address, position, userName, "")

      if (StaffService.updateStaff(s) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  def getRowCountOfTable(tableName: String) = Action {
    implicit request =>
      Ok(PatientService.getRowCountOfTable(tableName).toString)
  }

  def submitAppointmentsAddForm = Action {
    implicit request =>
      val id = ""
      val description = request.body.asFormUrlEncoded.get("description").head
      val firstName = request.body.asFormUrlEncoded.get("first_name").head
      val middleName = request.body.asFormUrlEncoded.get("middle_name").head
      val lastName = request.body.asFormUrlEncoded.get("last_name").head
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").head
      val address = request.body.asFormUrlEncoded.get("address").head
      val dateStart = request.body.asFormUrlEncoded.get("date_start").head
      val dateEnd = request.body.asFormUrlEncoded.get("date_end").head
      val dentistId = request.body.asFormUrlEncoded.get("dentist_id").head
      val status = request.body.asFormUrlEncoded.get("status").head.toInt
      val pl = AppointmentList("", description,firstName, middleName, lastName, dentistId, contactNo, address,  status, dateStart, dateEnd)

      if (AppointmentService.addAppointment(pl) >= 1) {
        Redirect("/scheduler")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }

  def getAllAppointments = Action {
    implicit request =>
      Ok(JsObject(Seq("AppointmentList" -> toJson(AppointmentService.getAllAppointments))))
  }

}
