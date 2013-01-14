package controllers

import play.api._
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{patient, modal}
import ws.services.{TreatmentPlanService, PatientList, PatientService}
import ws.delegates.{PatientDelegate, TreatmentPlanDelegate}
import ws.generator.UUIDGenerator
import ws.services

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/5/12
 * Time: 3:46 AM
 * To change this template use File | Settings | File Templates.
 */
object Patient extends Controller {

  def getTreatmentPlan(id: String) = Action {
    Ok(patient.treatment_plan(PatientDelegate.getPatientListById(id)))
  }

  def search(start: Int, count: Int, filter: String) = Action {
    println("start "+start+" count"+count);
    Ok(patient.list(PatientDelegate.searchPatientListByLastName(start,count,filter),TreatmentPlanDelegate.getTreatmentPlan(start,count)))
  }

  def getList(start: Int, count: Int) = Action {
    Ok(patient.list(PatientDelegate.getPatientList(start,count),TreatmentPlanDelegate.getTreatmentPlan(start,count)))
  }

  def getAddForm = Action {
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
          var params = request.body.asFormUrlEncoded.get
          PatientDelegate.submitAddPatientForm(params)
          Redirect("/patients")
        }
      )
  }



  def getUpdateForm(id: String, start: Int, count: Int) = Action {
    Ok(patient.update(PatientService.getPatientListById(id), TreatmentPlanDelegate.getTreatmentPlan(start, count))) //Todo make PatientService to delegate
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


