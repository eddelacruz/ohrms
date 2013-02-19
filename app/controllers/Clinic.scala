package controllers

import play.api._
import cache.Cache
import play.api.Play.current
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{clinic, modal}
import ws.services.{ClinicList, ClinicService}
import ws.delegates.ClinicDelegate
import ws.generator.UUIDGenerator
import controllers.Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 1/22/13
 * Time: 6:32 PM
 * To change this template use File | Settings | File Templates.
 */

object Clinic extends Controller with Secured {

  def searchClinicList(start: Int, count: Int, filter: String) = Action {
    println("start "+start+" count"+count);
    Ok(clinic.list(ClinicDelegate.searchClinicList(start,count,filter)))
  }

  def getList(start: Int, count: Int) = IsAuthenticated {
    username =>
      implicit request =>
    Ok(clinic.list(ClinicDelegate.getClinicList(start,count)))
  }

  def getClinicListById(id: String) = IsAuthenticated {
    username =>
      implicit request =>
    Ok(clinic.clinic_info(ClinicDelegate.getClinicById(id)))
  }


  def getUpdateForm(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(clinic.update(ClinicService.getClinicListById(id)))
          case _ => Redirect("/clinic/"+id)
        }
  }

  def submitUpdateForm = Action {
    implicit request =>
      ClinicDelegate._clinicProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        clinic => {
          val params = request.body.asFormUrlEncoded.get
          val id = request.body.asFormUrlEncoded.get("id").head
          ClinicDelegate.submitUpdateClinicForm(params)
          Redirect("/clinic/"+id)
        }
      )

  }


  def getAddForm = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(clinic.add())
          case _ => Redirect("/clinic")
        }

  }

  def submitAddForm = Action {
    implicit request =>
      ClinicDelegate._clinicProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        clinic => {
          var params = request.body.asFormUrlEncoded.get
          ClinicDelegate.submitAddClinicForm(params)
          Redirect("/clinic")
        }
      )
  }
}

