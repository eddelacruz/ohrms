package controllers

import play.api._
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views._
import ws.services.{AppointmentList, AppointmentService}
import ws.delegates.{DentistDelegate, AppointmentDelegate, DentalServiceDelegate}
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
object Appointments extends Controller with Secured {

  def submitAddForm = Action {
    implicit request =>
      AppointmentDelegate._appointmentProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        scheduler => {
          val params = request.body.asFormUrlEncoded.get
          AppointmentDelegate.submitAddAppointmentsForm(params)
          Redirect("/scheduler")
        }
      )
  }

  def getAppointmentListById(id: String) = IsAuthenticated {
    username =>
      implicit request =>
    Ok(html.modal._appointment_info(AppointmentDelegate.getAppointmentById(id)))
  }


  def getUpdateForm(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(html.modal._update_appointment(AppointmentService.getAppointmentById(id),DentistDelegate.getAllDentists(), DentalServiceDelegate.getAllDentalServiceList()))
  }

  def submitUpdateForm = Action {
    implicit request =>
      AppointmentDelegate._appointmentProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        appointment => {
          val params = request.body.asFormUrlEncoded.get
          val id = request.body.asFormUrlEncoded.get("id").head
          AppointmentDelegate.submitUpdateAppointmentForm(params)
          Redirect("/scheduler/"+id)
        }
      )

  }



}
