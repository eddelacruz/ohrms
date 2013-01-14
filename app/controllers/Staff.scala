package controllers

import play.api._
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{staff, modal}
import ws.services.{StaffService}
import ws.delegates.StaffDelegate
import ws.generator.UUIDGenerator

/**
 * Created with IntelliJ IDEA.
 * User: joh
 * Date: 12/19/12
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
object Staff extends Controller {

  def searchStaffList(start: Int, count: Int, filter: String) = Action {
    println("start "+start+" count"+count);
    Ok(staff.list(StaffDelegate.searchStaffList(start,count,filter)))
  }

  def getList(start: Int, count: Int) = Action {
    Ok(staff.list(StaffDelegate.getStaffList(start,count)))
  }

  def getStaffById(id: String) = Action {
    Ok(staff.staff_information(StaffDelegate.getStaffById(id)))
  }

  def getUpdateForm(id: String) = Action {
    Ok(staff.update(StaffService.getStaffListById(id)))
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


  def getAddForm = Action {
    Ok(staff.add())
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


}
