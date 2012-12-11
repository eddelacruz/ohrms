package controllers

import play.api.mvc.{Action, Controller}
import views.html._
import ws.delegates.DentistDelegate
import ws.services.{DentistService}
import play.api.data
import play.api.mvc._
import data.Forms._

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/10/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */

object Dentist extends Controller {

  def getList(start: Int, count: Int) = Action {
    Ok(dentist.list(DentistDelegate.getDentistList(start,count)))
  }

  def getDentistInformationById(id: String) = Action {
    Ok(dentist.dentist_information(DentistDelegate.getDentistInformationById(id)))
  }


  def getUpdateForm(id: String) = Action {
    Ok(dentist.update_dentist(DentistService.getDentistListById(id)))
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
}