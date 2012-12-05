package controllers

import play.api._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views._
import ws.PatientService

object Application extends Controller {
  
  def dashboard = Action {
    Ok(html.dboard("Your new application is ready."))
  }

  def treatmentPlan = Action {
    Ok(html.treatment_plan())
  }

  def samplePdf(id: String): Result = {
    return PDF.ok(html.samplePdf.render)
  }
}