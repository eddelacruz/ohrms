package controllers

import play.api._
import cache.Cache
import play.api.Play.current
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{staff, modal}
import ws.services.{StaffService, DentistService}
import ws.delegates.StaffDelegate
import ws.generator.UUIDGenerator
import controllers.Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 12/19/12
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
object Staff extends Controller with Secured{

  def searchStaffList(start: Int, count: Int, filter: String) = Action {
    Ok(staff.list(StaffDelegate.searchStaffList(start,count,filter),DentistService.getAllSecurityQuestion()))
  }

  def getList(start: Int, count: Int) =  IsAuthenticated{
    username =>
      implicit request =>
       Ok(staff.list(StaffDelegate.getStaffList(start,count),DentistService.getAllSecurityQuestion()))
  }

  def getStaffById(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(staff.staff_information(StaffDelegate.getStaffById(id)))
  }

  def getUpdateForm(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(staff.update(StaffService.getStaffListById(id)))
          case Some(2) => Ok(staff.update(StaffService.getStaffListById(id)))
          case _ => Redirect("/staffs/"+id)
        }
  }

  def submitUpdateForm = Action {
    implicit request =>
      StaffDelegate._staffProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        staff => {
          val params = request.body.asFormUrlEncoded.get
          val id = request.body.asFormUrlEncoded.get("id").head
          StaffDelegate.submitUpdateStaffForm(params)
          Redirect("/staffs/"+id)
        }
      )

  }


  def getAddForm = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(staff.add())
          case _ => Redirect("/staffs")
        }
  }

  def submitAddForm = Action {
    implicit request =>
      StaffDelegate._staffProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        staff => {
          val params = request.body.asFormUrlEncoded.get
          StaffDelegate.submitAddStaffForm(params)
          Redirect("/staffs")
        }
      )
  }

  def deleteInformation(id: String) = Action {
    implicit request =>
      Cache.get("role") match {
        case Some(0) =>
          val params = Map("id" -> Seq(id))
          StaffDelegate.deleteInformation(params)
          Redirect("/staffs")
        case _ => Redirect("/staffs/"+id)
      }
  }


}
