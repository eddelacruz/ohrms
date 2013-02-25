package controllers

import play.api._
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{patient, modal}
import ws.services.{TreatmentPlanService, PatientList, PatientService, PaymentService}
import ws.delegates._
import ws.generator.UUIDGenerator
import ws.services
import Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/5/12
 * Time: 3:46 AM
 * To change this template use File | Settings | File Templates.
 */
object Patient extends Controller with Secured{

  def getTreatmentPlan(id: String, start: Int, count: Int) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(patient.treatment_plan(PatientService.getPatientListById(id), TreatmentPlanDelegate.getTreatmentPlan(id, start, count), PaymentDelegate.getPaymentsByPatientId(start, count, id), PaymentService.getTotalPayments(id), PaymentService.getTotalPrices(id), PaymentService.getPaymentBalance(id))) //Todo make PatientService to delegate
  }

  def search(start: Int, count: Int, filter: String) = Action {
    println("start "+start+" count"+count);
    Ok(patient.list(PatientDelegate.searchPatientLastVisit(start,count,filter)))
  }

  def getList(start: Int, count: Int) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(patient.list(PatientDelegate.getPatientLastVisit(start, count)))
  }

  def getAddForm = IsAuthenticated {
    username =>
      implicit request =>
        println(PatientService.getPatientLastVisit(0, 25))
        Ok(patient.add())
  }

  def submitAddForm = Action {
    implicit request =>
      PatientDelegate._patientProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        patient => {
          val params = request.body.asFormUrlEncoded.get
          PatientDelegate.submitAddPatientForm(params)
          Redirect("/patients")
        }
      )
  }


  def getUpdateForm(id: String, start: Int, count: Int) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(patient.update(PatientService.getPatientListById(id), TreatmentPlanDelegate.getTreatmentPlan(id, start, count), DentalServiceDelegate.getAllDentalServiceList(), DentistDelegate.getAllDentists())) //Todo make PatientService to delegate
  }

  def submitUpdateForm = Action {
     implicit request =>
       PatientDelegate._patientProfileForm.bindFromRequest.fold(
         formWithErrors => {
           println("Form errors: "+formWithErrors.errors)
           BadRequest
         },
         patient => {
           val params = request.body.asFormUrlEncoded.get
           val id = request.body.asFormUrlEncoded.get("id").head
           PatientDelegate.submitUpdatePatientForm(params)
           Redirect("/patients/"+id+"/treatment_plan")
         }
       )
  }

  def deleteInformation(id: String) = Action {
    implicit request =>
      val params = Map("id" -> Seq(id))
      PatientDelegate.deleteInformation(params)
      Redirect("/patients")
  }

}


