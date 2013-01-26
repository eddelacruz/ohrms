package controllers

import play.api._
import libs.json.JsObject
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import ws.services._
import ws.generator.UUIDGenerator
import ws.helper.WsHelper
import ws.deserializer.json.{AnnouncementListDeserializer, AuditLogDeserializer,ClinicListDeserializer, PatientListDeserializer, DentistListDeserializer, DentalServiceListDeserializer, StaffListDeserializer, TreatmentPlanDeserializer, AppointmentDeserializer}
import collection.mutable.ListBuffer
import ws.services.PatientList
import ws.services.DentistList
import ws.services.StaffList
import ws.services.ClinicList
import ws.services.AnnouncementList


/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/10/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
object Json extends Controller with WsHelper with AnnouncementListDeserializer with PatientListDeserializer with AuditLogDeserializer with DentistListDeserializer with DentalServiceListDeserializer with StaffListDeserializer with TreatmentPlanDeserializer with AppointmentDeserializer with ClinicListDeserializer{

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

  def getAppointmentById(id : String) = Action {
    Ok(JsObject(Seq("AppointmentList" -> toJson(AppointmentService.getAppointmentById(id)))))
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

  def getClinicListById(id: String) = Action {
    Ok(JsObject(Seq("ClinicList" -> toJson(ClinicService.getClinicListById(id)))))
  }

  def getClinicList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("ClinicList" -> toJson(ClinicService.getClinicList(start, count)))))
  }

  def searchClinicList(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("ClinicList" -> toJson(ClinicService.searchClinicList(start, count, filter)))))
  }

  def getAnnouncementList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("AnnouncementList" -> toJson(AnnouncementService.getAnnouncementList(start, count)))))
  }

  def searchAnnouncementList(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("AnnouncementList" -> toJson(AnnouncementService.searchAnnouncementList(start, count, filter)))))
  }

  def getAnnouncementListById(id: String) = Action {
    Ok(JsObject(Seq("AnnouncementList" -> toJson(AnnouncementService.getAnnouncementListById(id)))))
  }


  def submitAnnouncementAddForm = Action {
    implicit request =>
      val id = ""
      val userName = ""
      val announcement = request.body.asFormUrlEncoded.get("announcement").head
      val dateCreated = request.body.asFormUrlEncoded.get("date_created").head
      val pl = AnnouncementList("", "", announcement, dateCreated)

      if (AnnouncementService.addAnnouncement(pl) >= 1) {
        Redirect("/announcements")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }

  def submitAnnouncementUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
      val userName = request.body.asFormUrlEncoded.get("user_name").head
      val announcement = request.body.asFormUrlEncoded.get("announcement").head
      val dateCreated = request.body.asFormUrlEncoded.get("date_created").head
      val pl = AnnouncementList(id, userName, announcement, dateCreated)

      if (AnnouncementService.updateAnnouncement(pl) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
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
      val medicalHistory = request.body.asFormUrlEncoded.get("medical_history").head
      val pl = PatientList("", firstName, middleName, lastName, address, contactNo, dateOfBirth, image, medicalHistory)

      if (PatientService.addPatient(pl) >= 1) {
        Redirect("/patients")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }

  def submitClinicAddForm = Action {
    implicit request =>
      val id = ""
      val clinicName = request.body.asFormUrlEncoded.get("clinic_name").head
      val address = request.body.asFormUrlEncoded.get("address").head
      val image = request.body.asFormUrlEncoded.get("image").head
      val pl = ClinicList("", clinicName, address, image)

      if (ClinicService.addClinic(pl) >= 1) {
        Redirect("/clinic")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }

  def submitClinicUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
      val clinicName = request.body.asFormUrlEncoded.get("clinic_name").head
      val address = request.body.asFormUrlEncoded.get("address").head
      val image = request.body.asFormUrlEncoded.get("image").head
      val pl = ClinicList(id, clinicName, address, image)

      if (ClinicService.updateClinic(pl) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  def auditLog(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("AuditLog" -> toJson(AuditLogService.getAllLogs(start,count)))))
  }

  def submitAppointmentUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
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
      val pl = AppointmentList(id, description,firstName, middleName, lastName, dentistId, contactNo, address,  status, dateStart, dateEnd)


      if (AppointmentService.updateAppointment(pl) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
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
      val medicalHistory = request.body.asFormUrlEncoded.get("medical_history").head
      val pl = PatientList(id, firstName, middleName, lastName, address, contactNo, dateOfBirth, image, medicalHistory)

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

  def deleteAnnouncementInformation = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head

      if (AnnouncementService.deleteAnnouncement(id) >= 1) {
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

 /* def addTreatmentPlan = Action {
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
  def addTreatmentPlan = Action {
    implicit request =>
      val treatmentPlan = request.body.asFormUrlEncoded; //request.body.asJson.get.\("Treatment_Plan")

      treatmentPlan.zipWithIndex.map{ case(abc, i)  =>
        println(abc.get("Treatment_Plan["+i+"][service_id]").get.head)
        //println(abc.get("Treatment_Plan[0][service_price]").get.head)
        //println(abc.get("Treatment_Plan[0][date_performed]").get.head)
        //println(abc.get("Treatment_Plan[0][teeth_name]").get.head)
        //println(abc.get("Treatment_Plan[0][patient_id]").get.head)
        //println(abc.get("Treatment_Plan[0][dentist_id]").get.head)
        //println(abc.get("Treatment_Plan[0][image]").get.head)
        //println(abc+i)
      }

    //.apply("Treatment_Plan[0][service_price]")
      /*request.body.asJson.map { json =>
        (json \ "name").asOpt[String].map { name =>
          Ok("Hello " + name)
        }.getOrElse {
          BadRequest("Missing parameter [name]")
        }
      }.getOrElse {
        BadRequest("Expecting Json data")
      }*/
      Status(200)
 }

  def getTreatmentPlan(start: Int, count: Int) = Action {
    //Ok(JsObject(Seq("TreatmentPlan" -> toJson(TreatmentPlanService.getTreatmentPlan(start, count)))))
    Ok("get treatment plan")
  }

  //TODO donot delete for future use
  /*def addTeethAffected(treatmentPlanId: String, teethStructure: JsValue) = Action{
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

  def deleteServicesInformation = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head

      if (ServicesService.deleteServices(id) >= 1) {
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
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").head
      val address = request.body.asFormUrlEncoded.get("address").head
      val position = request.body.asFormUrlEncoded.get("position").head
      val userName = request.body.asFormUrlEncoded.get("user_name").head
      val password = request.body.asFormUrlEncoded.get("password").head
      val s = StaffList(id, firstName, middleName, lastName, contactNo, address, position, userName, password)

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

  def getAllAppointmentsToday = Action {
    implicit request =>
      Ok(JsObject(Seq("AppointmentList" -> toJson(AppointmentService.getAppointmentsToday))))
  }

  def getBannedServicesByServiceCode(serviceCode: String) = Action {
    implicit request =>
      Ok(JsObject(Seq("AppointmentList" -> toJson(ServicesService.getBannedServicesByServiceCode(serviceCode)))))
  }

  def getAllDentalServices = Action {
    Ok(JsObject(Seq("DentalServiceList" -> toJson(ServicesService.getAllDentalServices()))))
  }

}
