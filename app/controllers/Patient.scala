package controllers

import play.api._
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{patient, modal}
import ws.services.{PatientList, PatientService}
import ws.delegates.PatientDelegate
import ws.generator.UUIDGenerator

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

  def getList = Action {
    Ok(patient.list(PatientDelegate.getPatientList))
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

  def getUpdateForm(id: String) = Action {
    Ok(patient.update(PatientService.getPatientListById(id)))
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

  def getDeleteForm(id: String) = Action {
    Ok(patient.update(PatientService.getPatientListById(id)))
  }

  def submitDeleteForm = Action {
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

}


