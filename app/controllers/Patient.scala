package controllers

import play.api._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views._
import ws.PatientService

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/5/12
 * Time: 3:46 AM
 * To change this template use File | Settings | File Templates.
 */
object Patient extends Controller {

  def getList = Action {
    Ok(html.patient.list(PatientService.getPatientList))
  }

}


