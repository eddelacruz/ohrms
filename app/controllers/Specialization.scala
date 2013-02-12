package controllers

import play.api.mvc._
import views.html._
import ws.delegates.{SpecializationDelegate}
import ws.services.{DentistService}
import play.api.{cache, data}
import data.Forms._
import Application.Secured
import cache.Cache
import play.api.Play.current

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 12/10/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */

object Specialization extends Controller with Secured{

  def searchSpecializationList(start: Int, count: Int, filter: String) = Action {
    Ok(specialization.list(SpecializationDelegate.searchSpecializationList(start,count,filter)))
  }

  def getList(start: Int, count: Int) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(specialization.list(SpecializationDelegate.getSpecializationList(start,count)))
  }

  def getSpecializationById(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(specialization.specialization_information(SpecializationDelegate.getSpecializationById(id)))
  }


  def getUpdateForm(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(specialization.update(DentistService.getSpecializationById(id)))
          case _ => Redirect("/specializations/"+id)
        }
  }

  def submitUpdateForm = Action {
    implicit request =>
      SpecializationDelegate._specializationProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        specialization => {
          val params = request.body.asFormUrlEncoded.get
          val id = request.body.asFormUrlEncoded.get("id").head
          SpecializationDelegate.submitUpdateSpecializationForm(params)
          Redirect("/specializations/"+id)
        }
      )

  }


  def getAddForm = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(specialization.add())
          case _ => Redirect("/specializations")
        }

  }

  def submitAddForm = Action {
    implicit request =>
      SpecializationDelegate._specializationProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        specialization => {
          var params = request.body.asFormUrlEncoded.get
          SpecializationDelegate.submitAddSpecializationForm(params)
          Redirect("/specializations")
        }
      )
  }

  def deleteInformation(id: String) = Action {
    implicit request =>
      Cache.get("role") match {
        case Some(0) =>
          val params = Map("id" -> Seq(id))
         SpecializationDelegate.deleteInformation(params)
          Redirect("/specializations")
        case _ => Redirect("/specializations")
      }
  }


}