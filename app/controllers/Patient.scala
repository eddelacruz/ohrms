package controllers

import play.api._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{patient, modal}
import ws.services.PatientService

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/5/12
 * Time: 3:46 AM
 * To change this template use File | Settings | File Templates.
 */
object Patient extends Controller {

  def getTreatmentPlan(id: String) = Action {
    Ok(patient.treatment_plan(PatientService.getPatientListById(id)))
  }

  def getList = Action {
    Ok(patient.list(PatientService.getPatientList))
  }

  def getAddForm = Action {
    Ok("add form")
  }

}


