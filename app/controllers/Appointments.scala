package controllers

import play.api._
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views._
import ws.services.{AppointmentList, AppointmentService}
import ws.delegates.{AppointmentDelegate}
import ws.generator.UUIDGenerator
import ws.services
import Application.Secured
/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/16/13
 * Time: 8:15 AM
 * To change this template use File | Settings | File Templates.
 */
object Appointments extends Controllers with Secured {

  def getAddForm = IsAuthenticated {
    username =>
      implicit request =>
        Ok(modal._add_appointment())
  }

  def submitAddForm = Action {
    implicit request =>
      AppointmentDelegate._appointmentProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        appointment => {
          var params = request.body.asFormUrlEncoded.get
          AppointmentDelegate.submitAddPatientForm(params)
          Redirect("/appointments")
        }
      )
  }



}
