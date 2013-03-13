package controllers

import play.api._
import libs.json.JsObject
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import ws.services._
import ws.generator.UUIDGenerator
import ws.helper.{DateWithTime, WsHelper}
import ws.deserializer.json.{SupplyDeserializer, PaymentListDeserializer, SpecializationListDeserializer,AnnouncementListDeserializer, AuditLogDeserializer,ClinicListDeserializer, PatientListDeserializer, DentistListDeserializer, DentalServiceListDeserializer, StaffListDeserializer, TreatmentPlanDeserializer, AppointmentDeserializer}
import collection.mutable.ListBuffer
import ws.services.{PatientList, SupplyList, PaymentList, DentistList, SpecializationList, StaffList, ClinicList, AnnouncementList, PatientLastVisit}
import controllers.Application.hash
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/10/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
object Json extends Controller with WsHelper with SupplyDeserializer with PaymentListDeserializer with AnnouncementListDeserializer with PatientListDeserializer with AuditLogDeserializer with DentistListDeserializer with DentalServiceListDeserializer with StaffListDeserializer with TreatmentPlanDeserializer with AppointmentDeserializer with ClinicListDeserializer with SpecializationListDeserializer{

  def getPatientList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("PatientList" -> toJson(PatientService.getPatientList(start, count)))))
  }

  def searchPatientListByLastName(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("PatientList" -> toJson(PatientService.searchPatientListByLastName(start, count, filter)))))
  }

  def getPatientLastVisit(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("PatientList" -> toJson(PatientService.getPatientLastVisit(start, count)))))
  }

  def searchPatientLastVisit(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("PatientList" -> toJson(PatientService.searchPatientLastVisit(start, count, filter)))))
  }

  def getAllPatients = Action {
    implicit request =>
      Ok(JsObject(Seq("PatientList" -> toJson(PatientService.getAllPatients))))
  }

  def getAllPatientNames = Action {
    implicit request =>
      Ok(toJson(PatientService.getAllPatientNames))
  }

  def getPatientVisitsByYear(year: Int) = Action {
    implicit request =>
      var pl = ListBuffer[Long]()
      for( x <- 1 to 12){
        pl += PatientService.getPatientVisitsByYear(year, x)
      }
      Ok(toJson(pl.toList))
  }

  def getPatientVisitsByMonth(year: Int, month: Int) = Action {
    implicit request =>
      var pl = ListBuffer[Long]()
      val d = DateWithTime.getNumberOfDays(year, month)
      for( x <- 1 to d){
        pl += PatientService.getPatientVisitsByMonth(year, month, x)
      }
      Ok(toJson(pl.toList))
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

  def getAllAnnouncementsToday = Action {
    implicit request =>
      Ok(JsObject(Seq("AnnouncementList" -> toJson(AnnouncementService.getAnnouncementsToday))))
  }

  def searchAnnouncementList(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("AnnouncementList" -> toJson(AnnouncementService.searchAnnouncementList(start, count, filter)))))
  }

  def getAnnouncementListById(id: String) = Action {
    Ok(JsObject(Seq("AnnouncementList" -> toJson(AnnouncementService.getAnnouncementListById(id)))))
  }


  def submitAnnouncementAddForm = Action {
    implicit request =>
      val description = request.body.asFormUrlEncoded.get("description").headOption
      val dateCreated = request.body.asFormUrlEncoded.get("date_created").headOption
      val pl = AnnouncementList("", Some(""), description, dateCreated)

      val df: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

      if(df.parseDateTime(dateCreated.get+":00").isAfter(df.parseDateTime(DateWithTime.dateNow))){
        if (AnnouncementService.addAnnouncement(pl) >= 1 ) {
          Redirect("/reminders")
          Status(200)
        } else {
          BadRequest
          Status(500)
        }
      } else {
        println("Cannot add reminder on previous dates.")
        BadRequest
        Status(500)
      }


  }

  def submitAnnouncementUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
      val userName = request.body.asFormUrlEncoded.get("user_name").headOption
      val description = request.body.asFormUrlEncoded.get("description").headOption
      val dateCreated = request.body.asFormUrlEncoded.get("date_created").headOption
      val pl = AnnouncementList(id, userName, description, dateCreated)

      if (AnnouncementService.updateAnnouncement(pl) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  def submitPatientAddForm = Action {
    implicit request =>
      println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>."+request.body)
      val id = ""
      val firstName = request.body.asFormUrlEncoded.get("first_name").headOption
      val middleName = request.body.asFormUrlEncoded.get("middle_name").headOption
      val lastName = request.body.asFormUrlEncoded.get("last_name").headOption
      val address = request.body.asFormUrlEncoded.get("address").headOption
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").headOption
      val dateOfBirth = request.body.asFormUrlEncoded.get("date_of_birth").headOption
      val medicalHistory = request.body.asFormUrlEncoded.get("medical_history").headOption
      val gender = request.body.asFormUrlEncoded.get("gender").head
      val pl = PatientList("", firstName, middleName, lastName, address, contactNo, dateOfBirth, medicalHistory, gender)

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
      val clinicName = request.body.asFormUrlEncoded.get("clinic_name").headOption
      val address = request.body.asFormUrlEncoded.get("address").headOption
      val image = request.body.asFormUrlEncoded.get("image").headOption
      val userName = request.body.asFormUrlEncoded.get("user_name").headOption
      val contactNumber = request.body.asFormUrlEncoded.get("contact_number").headOption

      val pl = ClinicList("", clinicName, address, image, userName, contactNumber)

      if (ClinicService.addClinic(pl) >= 1) {
        //Redirect("/clinic")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }

  def submitClinicUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
      val clinicName = request.body.asFormUrlEncoded.get("clinic_name").headOption
      val address = request.body.asFormUrlEncoded.get("address").headOption
      val image = request.body.asFormUrlEncoded.get("imaging").headOption
      val userName = request.body.asFormUrlEncoded.get("user_name").headOption
      val contactNumber = request.body.asFormUrlEncoded.get("contact_number").headOption
      val pl = ClinicList(id, clinicName, address, image, userName, contactNumber)

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
      val dentalServiceId = request.body.asFormUrlEncoded.get("dental_service_id").headOption
      val firstName = request.body.asFormUrlEncoded.get("first_name").headOption
      val middleName = request.body.asFormUrlEncoded.get("middle_name").headOption
      val lastName = request.body.asFormUrlEncoded.get("last_name").headOption
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").headOption
      val address = request.body.asFormUrlEncoded.get("address").headOption
      val dateStart = request.body.asFormUrlEncoded.get("date_start").headOption
      val dateEnd = request.body.asFormUrlEncoded.get("date_end").headOption
      val dentistId = request.body.asFormUrlEncoded.get("dentist_id").headOption
      val al = AppointmentList(id, dentalServiceId,firstName, middleName, lastName, dentistId, contactNo, address, dateStart, dateEnd)

      if (AppointmentService.checkIfDentistIsAvailable(dentistId.get, dateStart.get, dateEnd.get) == 0 ){
        if (AppointmentService.updateAppointment(al) >= 1) {
          Status(200)
        } else {
          BadRequest
          Status(500)
        }
      } else {
        println("Dentist not available at this time.")
        BadRequest
        Status(500)
      }
  }

  def submitPatientUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
      val firstName = request.body.asFormUrlEncoded.get("first_name").headOption
      val middleName = request.body.asFormUrlEncoded.get("middle_name").headOption
      val lastName = request.body.asFormUrlEncoded.get("last_name").headOption
      val address = request.body.asFormUrlEncoded.get("address").headOption
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").headOption
      val dateOfBirth = request.body.asFormUrlEncoded.get("date_of_birth").headOption
      val medicalHistory = request.body.asFormUrlEncoded.get("medical_history").headOption
      val gender = request.body.asFormUrlEncoded.get("gender").head
      val pl = PatientList(id, firstName, middleName, lastName, address, contactNo, dateOfBirth, medicalHistory, gender)

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
      val firstName = request.body.asFormUrlEncoded.get("first_name").headOption
      val middleName = request.body.asFormUrlEncoded.get("middle_name").headOption
      val lastName = request.body.asFormUrlEncoded.get("last_name").headOption
      val address = request.body.asFormUrlEncoded.get("address").headOption
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").headOption
      val prcNo = request.body.asFormUrlEncoded.get("prc_no").headOption
      val sn = request.body.asFormUrlEncoded.get("specialization_name").headOption
      val password = request.body.asFormUrlEncoded.get("password").headOption
      var specializationName: Option[Seq[String]] = Option(Seq(sn.get))
      val dl = DentistList(id, firstName, middleName, lastName, address, contactNo, prcNo, Some(""), Some(hash(password.get)), Some(specializationName.get.toSeq), Some(""), Some(""))

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
      val firstName = request.body.asFormUrlEncoded.get("first_name").headOption
      val middleName = request.body.asFormUrlEncoded.get("middle_name").headOption
      val lastName = request.body.asFormUrlEncoded.get("last_name").headOption
      val address = request.body.asFormUrlEncoded.get("address").headOption
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").headOption
      val prcNo = request.body.asFormUrlEncoded.get("prc_no").headOption
      val userName = request.body.asFormUrlEncoded.get("user_name").headOption
      val password = request.body.asFormUrlEncoded.get("password").headOption
      val question = request.body.asFormUrlEncoded.get("question").headOption
      val answer = request.body.asFormUrlEncoded.get("answer").headOption
      val dl = DentistList(id, firstName, middleName, lastName, address, contactNo, prcNo, userName, Some(hash(password.get)), Some(specializationList), question, Some(hash(answer.get)))

      var index = 0
      if (DentistService.addDentist(dl) >= 1) {
        try{
          while (request.body.asFormUrlEncoded.get("specializationName["+index+"]") != null) {
            val specializationName = request.body.asFormUrlEncoded.get("specializationName["+index+"]").headOption.get

            if (specializationName.length > 1){
              val sl = SpecializationList("", dl.id, specializationName)
              DentistService.addSpecialization(sl)
            }
            index += 1
          }
        } catch {
          case e: Exception =>
            println("----->>>>> (END OF ITERATION OF "+index+" SPECIALIZATION) <<<<<-----")
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

  def addTreatmentPlan = Action {
    implicit request =>
      val treatmentPlan = request.body.asFormUrlEncoded.get; //request.body.asJson.get.\("Treatment_Plan")
      val df: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
      var index = 0

      try{
        while (treatmentPlan.get("Treatment_Plan["+index+"][service_id]").get.head != null) {
          val serviceId = treatmentPlan.get("Treatment_Plan["+index+"][service_id]").get.headOption
          val servicePrice = treatmentPlan.get("Treatment_Plan["+index+"][service_price]").get.headOption
          val datePerformed = treatmentPlan.get("Treatment_Plan["+index+"][date_performed]").get.headOption
          val teethId = treatmentPlan.get("Treatment_Plan["+index+"][teeth_name]").get.headOption
          val patientId = treatmentPlan.get("Treatment_Plan["+index+"][patient_id]").get.headOption
          val dentistId = treatmentPlan.get("Treatment_Plan["+index+"][dentist_id]").get.headOption
          val image = treatmentPlan.get("Treatment_Plan["+index+"][image]").get.headOption

          val tp = TreatmentPlanType("", serviceId, Some(""), Some(""), Some(""),Some(""), servicePrice, Some(""), datePerformed, teethId, Some(""), Some(""), Some(""),Some(""), patientId, dentistId, Some(""), image, Some(""))

          tp.image = Some("")

          val abc = teethId.get.charAt(0)
          println(tp.teethId.get)

          if( tp.teethId.get == "ALLA" && TreatmentPlanService.checkDentalServiceToolType(serviceId.get) == 4 && (df.parseDateTime(datePerformed.get+":00").isBefore(df.parseDateTime(DateWithTime.dateNow)))) {
            tp.image = Some("")
            TreatmentPlanService.addTreatment(tp)
          } else if( (tp.teethId.get == "UPA" || tp.teethId.get == "LOWA" || tp.teethId.get == "ALLA" ) && TreatmentPlanService.checkDentalServiceToolType(serviceId.get) == 3 && (df.parseDateTime(datePerformed.get+":00").isBefore(df.parseDateTime(DateWithTime.dateNow)))) {
            tp.image = Some("")
            TreatmentPlanService.addTreatment(tp)
          } else if(abc == 'F' && TreatmentPlanService.checkDentalServiceToolType(serviceId.get) == 2 && (df.parseDateTime(datePerformed.get+":00").isBefore(df.parseDateTime(DateWithTime.dateNow))) ) {
            tp.image = Some("")
            TreatmentPlanService.addTreatment(tp)
          } else if(TreatmentPlanService.checkDentalServiceToolType(serviceId.get) == 1 && (df.parseDateTime(datePerformed.get+":00").isBefore(df.parseDateTime(DateWithTime.dateNow))) ){
            TreatmentPlanService.addTreatment(tp)
          }
          index+=1
        }
      } catch {
        case e: Exception =>
          println("----->>>>> (END OF ITERATION OF "+index+" TREATMENT_PLAN) <<<<<-----")
      }
      Status(200)
 }

  def getTreatmentPlan(patientId: String, start: Int, count: Int) = Action {
    Ok(JsObject(Seq("TreatmentPlan" -> toJson(TreatmentPlanService.getTreatmentPlan(patientId, start, count)))))
  }

  def submitDentalServiceAddForm = Action {
    implicit request =>
      println(request.body)
      var index = 0
      val id = UUIDGenerator.generateUUID("dental_services")
      val name = request.body.asFormUrlEncoded.get("name").headOption
      val code = request.body.asFormUrlEncoded.get("code").headOption
      val sType = request.body.asFormUrlEncoded.get("type").headOption
      val target = request.body.asFormUrlEncoded.get("target").headOption
      val price = request.body.asFormUrlEncoded.get("price").headOption
      val color = request.body.asFormUrlEncoded.get("color").headOption
      val imageTemplate = request.body.asFormUrlEncoded.get("image_template").headOption
      val dl = DentalServiceList(id, name, code, sType, Some(target.get.toInt), price, color, imageTemplate)

      try {
        while (request.body.asFormUrlEncoded.get("banned_service["+index+"]").head != null) {
          val b = request.body.asFormUrlEncoded.get("banned_service["+index+"]").head
          ServicesService.addBannedService(id, b)
          index+=1
        }
      } catch {
        case e: Exception =>
          println("----->>>>> (END OF ITERATION OF "+index+" BANNED_SERVICE) <<<<<-----")
      }

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
      val name = request.body.asFormUrlEncoded.get("name").headOption
      val code = request.body.asFormUrlEncoded.get("code").headOption
      val sType = request.body.asFormUrlEncoded.get("type").headOption
      val target = request.body.asFormUrlEncoded.get("target").headOption
      val price = request.body.asFormUrlEncoded.get("price").headOption
      val color = request.body.asFormUrlEncoded.get("color").headOption
      val imageTemplate = request.body.asFormUrlEncoded.get("color").headOption
      val dl = DentalServiceList(id, name, code, sType, Some(target.get.toInt), price, color, imageTemplate)

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
      val firstName = request.body.asFormUrlEncoded.get("first_name").headOption
      val middleName = request.body.asFormUrlEncoded.get("middle_name").headOption
      val lastName = request.body.asFormUrlEncoded.get("last_name").headOption
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").headOption
      val address = request.body.asFormUrlEncoded.get("address").headOption
      val position = request.body.asFormUrlEncoded.get("position").headOption
      val userName = request.body.asFormUrlEncoded.get("user_name").headOption
      val password = request.body.asFormUrlEncoded.get("password").headOption
      val question = request.body.asFormUrlEncoded.get("question").headOption
      val answer = request.body.asFormUrlEncoded.get("answer").headOption
      val s = StaffList("", firstName, middleName, lastName, contactNo, address, position, userName, Some(hash(password.get)), question, Some(hash(answer.get)))

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
      val firstName = request.body.asFormUrlEncoded.get("first_name").headOption
      val middleName = request.body.asFormUrlEncoded.get("middle_name").headOption
      val lastName = request.body.asFormUrlEncoded.get("last_name").headOption
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").headOption
      val address = request.body.asFormUrlEncoded.get("address").headOption
      val position = request.body.asFormUrlEncoded.get("position").headOption
      val password = request.body.asFormUrlEncoded.get("password").headOption
      val s = StaffList(id, firstName, middleName, lastName, contactNo, address, position, Some(""), Some(hash(password.get)), Some(""), Some(""))

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
      val dentalServiceId = request.body.asFormUrlEncoded.get("dental_service_id").headOption
      val firstName = request.body.asFormUrlEncoded.get("first_name").headOption
      val middleName = request.body.asFormUrlEncoded.get("middle_name").headOption
      val lastName = request.body.asFormUrlEncoded.get("last_name").headOption
      val contactNo = request.body.asFormUrlEncoded.get("contact_no").headOption
      val address = request.body.asFormUrlEncoded.get("address").headOption
      val dateStart = request.body.asFormUrlEncoded.get("date_start").headOption
      val dentistId = request.body.asFormUrlEncoded.get("dentist_id").headOption
      val dateEnd = request.body.asFormUrlEncoded.get("date_end").headOption
      val pl = AppointmentList("", dentalServiceId, firstName, middleName, lastName, dentistId, contactNo, address, dateStart, dateEnd)

      //println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> bilang ng mga rows dapat "+AppointmentService.checkIfDentistIsAvailable(dentistId.get, dateStart.get, dateEnd.get))

      println(dateStart)
      println(dateEnd)

      if (AppointmentService.checkIfDentistIsAvailable(dentistId.get, dateStart.get, dateEnd.get) == 0 ){
        if (AppointmentService.addAppointment(pl) >= 1) {
          Redirect("/scheduler")
          Status(200)
        } else {
          BadRequest
          Status(500)
        }
      } else {
        println("Dentist not available at this time.")
        BadRequest
        Status(500)
      }
  }

  def deleteStaffInformation = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head

      if (StaffService.deleteStaff(id) >= 1) {
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
      Ok(JsObject(Seq("DentalServiceList" -> toJson(ServicesService.getBannedServicesByServiceCode(serviceCode)))))
  }

  def submitBannedServicesAddForm = Action {
    implicit request =>
      val a:Seq[String] = request.body.asFormUrlEncoded.get.get("Banned_Services[]").head
      Status(200)
  }

  def getAllDentalServices = Action {
    Ok(JsObject(Seq("DentalServiceList" -> toJson(ServicesService.getAllDentalServices()))))
  }

  def getAllDentists = Action {
    Ok(JsObject(Seq("DentistList" -> toJson(DentistService.getAllDentist()))))
  }


  def getSpecializationList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("SpecializationList" -> toJson(DentistService.getSpecializationList(start, count)))))
  }

  def searchSpecializationList(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("SpecializationList" -> toJson(DentistService.searchSpecializationList(start, count, filter)))))
  }

  def getSpecializationById(id: String) = Action {
    Ok(JsObject(Seq("SpecializationList" -> toJson(DentistService.getSpecializationById(id)))))
  }


  def submitSpecializationAddForm = Action {
    implicit request =>
      val id = ""
      val dentistId = request.body.asFormUrlEncoded.get("dentist_id").head
      val name = request.body.asFormUrlEncoded.get("name").head
      val sl = SpecializationList("", dentistId, name)

      if (DentistService.addSpecialization(sl) >= 1) {
        Redirect("/specializations")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }

  def submitSpecializationUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head
      val dentistId = request.body.asFormUrlEncoded.get("dentist_id").head
      val name = request.body.asFormUrlEncoded.get("name").head
      val sl = SpecializationList(id, dentistId, name)

      if (DentistService.updateSpecialization(sl) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  def deleteSpecializationInformation = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").head

      if (DentistService.deleteSpecialization(id) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  def getPaymentsByPatientId(start: Int, count: Int, patientId: String) = Action {
    Ok(JsObject(Seq("PaymentList" -> toJson(PaymentService.getPaymentsByPatientId(start,count,patientId)))))
  }

  def getPaymentById(id: String) = Action {
    Ok(JsObject(Seq("PaymentList" -> toJson(PaymentService.getPaymentById(id)))))
  }

  def getTotalPayments(patientId: String) = Action {
    Ok(toJson(PaymentService.getTotalPayments(patientId)))
  }

  def getTotalPrices(patientId: String) = Action {
    Ok(toJson(PaymentService.getTotalPrices(patientId)))
  }

  def getTotalPricesByDateRange(year: Int, month: Int) = Action {
    Ok(toJson(PaymentService.getTotalPricesByDateRange(year, month)))
  }

  def getPaymentBalance(patientId: String) = Action {
    Ok(toJson(PaymentService.getPaymentBalance(patientId)))
  }

  def submitPaymentUpdateForm = Action {
    implicit request =>
      val id =  request.body.asFormUrlEncoded.get("id").headOption
      val patientId = request.body.asFormUrlEncoded.get("patient_id").headOption
      val payment = request.body.asFormUrlEncoded.get("payment").headOption
      val paymentDate = request.body.asFormUrlEncoded.get("date_of_payment").headOption
      val userName = request.body.asFormUrlEncoded.get("user_name").headOption
      val dl = PaymentList("", patientId, "", "", payment, paymentDate, userName)

      if (PaymentService.updatePayment(dl) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  def submitPaymentAddForm = Action {
    implicit request =>
      val id = ""
      val patientId = request.body.asFormUrlEncoded.get("patient_id").headOption
      val payment = request.body.asFormUrlEncoded.get("payment").headOption
      val paymentDate = request.body.asFormUrlEncoded.get("date_of_payment").headOption
      val userName = request.body.asFormUrlEncoded.get("user_name").headOption
      val dl = PaymentList("", patientId, "", "", payment, paymentDate, userName)

      if (PaymentService.addPayment(dl) >= 1) {
        Redirect("/patients")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  def getTeethByPositionAndType(position: String, tType: String) = Action {
    implicit request =>
      Ok(toJson(TreatmentPlanService.getTeethByPositionAndType(position, tType)))
  }

  def getAllDentalServiceTypes = Action {
    implicit request =>
      Ok(toJson(ServicesService.getAllDentalServiceTypes))
  }

  def getAllSpecializationNames = Action {
    implicit request =>
      Ok(toJson(DentistService.getAllSpecializationNames))
  }

  def getAuditLogReport(module: String, dateStart: String, dateEnd: String) = Action {
    implicit request =>
      Ok(JsObject(Seq("AuditLog" -> toJson(AuditLogService.getAuditLogReport(module, dateStart, dateEnd)))))
  }

  def getToothName(toothId: String) = Action {
    implicit request =>
      Ok(toJson(ServicesService.getToothName(toothId)))
  }

  def getSupplyListById(id: String) = Action {
    Ok(JsObject(Seq("SupplyList" -> toJson(SupplyService.getSupplyListById(id)))))
  }

  def getSupplyList(start: Int, count: Int) = Action {
    Ok(JsObject(Seq("SupplyList" -> toJson(SupplyService.getSupplyList(start, count)))))
  }

  def searchSupplyList(start: Int, count: Int, filter: String) = Action {
    Ok(JsObject(Seq("SupplyList" -> toJson(SupplyService.searchSupplyList(start, count, filter)))))
  }

  def submitSupplyAddForm = Action {
    implicit request =>
      val id = ""
      val name = request.body.asFormUrlEncoded.get("name").headOption
      val description = request.body.asFormUrlEncoded.get("description").headOption
      val price = request.body.asFormUrlEncoded.get("price").headOption
      val quantity = request.body.asFormUrlEncoded.get("quantity").headOption
      val patientId = request.body.asFormUrlEncoded.get("patient_id").headOption
      val pl = SupplyList("", patientId, name, description, quantity, price)

      if (SupplyService.addSupply(pl) >= 1) {
        //Redirect("/dental_supplies")
        Status(200)
      } else {
        BadRequest
        Status(500)
      }

  }

  def submitSupplyUpdateForm = Action {
    implicit request =>
      val id = request.body.asFormUrlEncoded.get("id").head
      val name = request.body.asFormUrlEncoded.get("name").headOption
      val description = request.body.asFormUrlEncoded.get("description").headOption
      val price = request.body.asFormUrlEncoded.get("price").headOption
      val quantity = request.body.asFormUrlEncoded.get("quantity").headOption
      val patientId = request.body.asFormUrlEncoded.get("patient_id").headOption
      val pl = SupplyList(id, patientId, name, description, quantity, price)

      if (SupplyService.updateSupply(pl) >= 1) {
        Status(200)
      } else {
        BadRequest
        Status(500)
      }
  }

  def submitTeethNamingUpdateForm = Action {
    implicit request =>
      //println(request.body.asFormUrlEncoded)
      var index = 0
      try {
        while (request.body.asFormUrlEncoded.get("Teeth["+index+"][]").head != null) {
          val teethId = request.body.asFormUrlEncoded.get("Teeth["+index+"][]").headOption.get
          val teethName = request.body.asFormUrlEncoded.get("Teeth["+index+"][]").tail.headOption.get
          TreatmentPlanService.updateTeethNaming(teethId, teethName)
          index+=1
        }
      } catch {
        case e: Exception =>
          println("----->>>>> (END OF ITERATION OF "+index+" TEETH_NAME) <<<<<-----")
          Redirect("/settings/teeth_naming")
      }
      Status(200)
  }

  def getPatientsByDateRange(start: String, end: String) = Action {
    implicit request =>
      Ok(JsObject(Seq("PatientList" -> toJson(PatientService.getPatientsByDateRange(start, end)))))
  }

  def getMonthlyIncome(year: Int, month: Int) = Action {
    implicit request =>
      Ok(JsObject(Seq("IncomeList" -> toJson(PaymentService.getMonthlyIncome(year, month)))))
  }

}
