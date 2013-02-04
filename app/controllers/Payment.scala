package controllers

import play.api._
import play.api.mvc.Controller
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{payment, modal}
import ws.services.{TreatmentPlanService, PatientList, PatientService}
import ws.delegates.{DentistDelegate, DentalServiceDelegate, PatientDelegate, TreatmentPlanDelegate}
import ws.generator.UUIDGenerator
import ws.services
import Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 2/5/13
 * Time: 2:16 AM
 * To change this template use File | Settings | File Templates.
 */
object Payment extends Controller with Secured{

  def getPaymentsByPatientId(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(payment.counter())
  }

}



