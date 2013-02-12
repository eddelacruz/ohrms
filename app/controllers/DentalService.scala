package controllers

import play.api._
import cache.Cache
import play.api.Play.current
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{service, modal}
import ws.services.{DentalServiceList, ServicesService}
import ws.delegates.DentalServiceDelegate
import ws.generator.UUIDGenerator
import controllers.Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 12/18/12
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
object DentalService extends Controller with Secured{


  def searchServiceList(start: Int, count: Int, filter: String) = Action {
    Ok(service.list(DentalServiceDelegate.searchServiceList(start,count,filter)))
  }

  def getList(start: Int, count: Int) = IsAuthenticated {
    username =>
     implicit request =>
         Ok(service.list(DentalServiceDelegate.getDentalServiceList(start,count)))
  }

  def getDentalServiceInformationById(id: String) =IsAuthenticated {
     username =>
      implicit request =>
        Ok(service.services_information(DentalServiceDelegate.getDentalServiceInformationById(id)))
  }

  def getUpdateForm(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(service.update(DentalServiceDelegate.getDentalServiceInformationById(id)))
          case _ => Redirect("/dental_services")
        }
  }

  def submitUpdateForm = Action {
    implicit request =>
      DentalServiceDelegate._dentalServiceProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        dentalService => {
          val params = request.body.asFormUrlEncoded.get
          val id = request.body.asFormUrlEncoded.get("id").head
          DentalServiceDelegate.submitUpdateDentalServiceForm(params)
          Redirect("/dental_services/"+id+"/information")
        }
      )

  }


  def getAddForm = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(service.add())
          case _ => Redirect("/dental_services")
        }
  }

  def submitAddForm = Action {
    implicit request =>
      DentalServiceDelegate._dentalServiceProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        dentalService => {
          val params = request.body.asFormUrlEncoded.get
          DentalServiceDelegate.submitAddDentalServiceForm(params)
          Redirect("/dental_services")
        }
      )
  }

  def deleteInformation(id: String) = Action {
    implicit request =>
      Cache.get("role") match {
        case Some(0) =>
          val params = Map("id" -> Seq(id))
          DentalServiceDelegate.deleteInformation(params)
          Redirect("/dental_services")
        case _ => Redirect("/dental_services")
      }
  }

}
