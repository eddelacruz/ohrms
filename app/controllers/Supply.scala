package controllers

import play.api._
import cache.Cache
import play.api.Play.current
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views._
import ws.services.{SupplyList, SupplyService}
import ws.delegates.SupplyDelegate
import ws.generator.UUIDGenerator
import controllers.Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 3/6/13
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
object Supply extends Controller with Secured{

  def searchSupplyList(start: Int, count: Int, filter: String) = Action {
    Ok(views.html.settings.list(SupplyDelegate.searchSupplyList(start,count,filter)))
  }

  def getList(start: Int, count: Int) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(views.html.settings.list(SupplyDelegate.getSupplyList(start,count)))
  }

  def getSupplyListById(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(views.html.settings.supply_info(SupplyDelegate.getSupplyById(id)))
  }


  def getUpdateForm(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(views.html.settings.update(SupplyService.getSupplyListById(id)))
          case _ => Redirect("/dental_supplies/"+id)
        }
  }

  def submitUpdateForm = Action {
    implicit request =>
      SupplyDelegate._supplyProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        supply => {
          val params = request.body.asFormUrlEncoded.get
          val id = request.body.asFormUrlEncoded.get("id").head
          SupplyDelegate.submitUpdateSupplyForm(params)
          Redirect("/dental_supplies/"+id)
        }
      )

  }


  def getAddForm = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(views.html.settings.add())
          case _ => Redirect("/dental_supplies")
        }

  }

  def submitAddForm = Action {
    implicit request =>
      SupplyDelegate._supplyProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        supply => {
          var params = request.body.asFormUrlEncoded.get
          SupplyDelegate.submitAddSupplyForm(params)
          Redirect("/dental_supplies")
        }
      )
  }

}
