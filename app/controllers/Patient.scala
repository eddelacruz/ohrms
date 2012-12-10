package controllers

import play.api._
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{patient, modal}
import ws.services.PatientList
import ws.delegate.PatientDelegate
import ws.generator.UUIDGenerator

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/5/12
 * Time: 3:46 AM
 * To change this template use File | Settings | File Templates.
 */
object Patient extends Controller {

  val _patientProfileForm = Form(
    mapping(
      "id" -> text,
      "firstName" -> text,
      "middleName" -> text,
      "lastName" -> text,
      "medicalHistory" -> text,
      "address" -> text,
      "contactNo" -> text,
      "dateOfBirth" -> text,
      "image" -> text
    )(PatientList.apply)(PatientList.unapply)
  )

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
/*    implicit request =>
      _patientProfileForm.bindFromRequest.fold(
        formWithErrors => BadRequest
        patient => {
          println()
        }
      )*/
    Ok("ok")
  }
}


