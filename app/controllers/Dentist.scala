package controllers

import play.api.mvc._
import views.html._
import ws.delegates.{DentistDelegate}
import ws.services.{DentistService}
import play.api.{cache, data}
import data.Forms._
import Application.Secured
import cache.Cache
import play.api.Play.current

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/10/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */

object Dentist extends Controller with Secured{

  def searchDentistList(start: Int, count: Int, filter: String) = Action {
    Ok(dentist.list(DentistDelegate.searchDentistList(start,count,filter),DentistService.getAllSecurityQuestion()))
  }

  def getList(start: Int, count: Int) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(dentist.list(DentistDelegate.getDentistList(start,count),DentistService.getAllSecurityQuestion()))
  }

  def getDentistInformationById(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(dentist.dentist_information(DentistDelegate.getDentistInformationById(id)))
  }


  def getUpdateForm(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(dentist.update(DentistService.getDentistListById(id)))
          case Some(1) => Ok(dentist.update(DentistService.getDentistListById(id)))
          case _ => Redirect("/dentists/"+id+"/information")
        }
  }

  def submitUpdateForm = Action {
    implicit request =>
      DentistDelegate._dentistProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        dentist => {
          val params = request.body.asFormUrlEncoded.get
          val id = request.body.asFormUrlEncoded.get("id").head
          DentistDelegate.submitUpdateDentistForm(params)
          Redirect("/dentists/"+id+"/information")
        }
      )

  }


  def getAddForm = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(dentist.add())
          case _ => Redirect("/dentists")
        }

  }

  def submitAddForm = Action {
    implicit request =>
      DentistDelegate._dentistProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        dentist => {
          val params = request.body.asFormUrlEncoded.get
          DentistDelegate.submitAddDentistForm(params)
          Redirect("/dentists")
        }
      )
  }

  def deleteInformation(id: String) = Action {
    implicit request =>
      Cache.get("role") match {
        case Some(0) =>
          val params = Map("id" -> Seq(id))
          DentistDelegate.deleteInformation(params)
          Redirect("/dentists")
        case _ => Redirect("/dentists")
      }
  }

  def getAll() = IsAuthenticated {
    username =>
      implicit request =>
        Ok(dentist.list(DentistDelegate.getAllDentists(),DentistService.getAllSecurityQuestion()))
  }

}