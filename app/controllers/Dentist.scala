package controllers

import play.api.mvc._
import views.html._
import ws.delegates.{DentistDelegate}
import ws.services.{DentistService}
import play.api.data
import data.Forms._
import Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/10/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */

object Dentist extends Controller with Secured{

  def searchDentistList(start: Int, count: Int, filter: String) = Action {
    println("start "+start+" count"+count);
    Ok(dentist.list(DentistDelegate.searchDentistList(start,count,filter)))
  }

  def getList(start: Int, count: Int) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(dentist.list(DentistDelegate.getDentistList(start,count)))
  }

  def getDentistInformationById(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(dentist.dentist_information(DentistDelegate.getDentistInformationById(id)))
  }


  def getUpdateForm(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(dentist.update(DentistService.getDentistListById(id)))
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
        Ok(dentist.add())
  }

  def submitAddForm = Action {
    implicit request =>
      DentistDelegate._dentistProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        dentist => {
          var params = request.body.asFormUrlEncoded.get
          DentistDelegate.submitAddDentistForm(params)
          Redirect("/patients")
        }
      )
  }

  def deleteInformation(id: String) = Action {
    implicit request =>
      val params = Map("id" -> Seq(id))
      DentistDelegate.deleteInformation(params)
      Redirect("/dentists")
  }

  def getAll() = IsAuthenticated {
    username =>
      implicit request =>
        Ok(dentist.list(DentistDelegate.getAllDentists()))
  }

}