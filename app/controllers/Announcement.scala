package controllers

import play.api._
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{announcement, modal}
import ws.services.{AnnouncementList, AnnouncementService}
import ws.delegates.{AnnouncementDelegate}
import ws.generator.UUIDGenerator
import controllers.Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 1/22/13
 * Time: 6:32 PM
 * To change this template use File | Settings | File Templates.
 */

object Announcement extends Controller with Secured {

  def searchAnnouncementList(start: Int, count: Int, filter: String) = Action {
    println("start "+start+" count"+count);
    Ok(announcement.list(AnnouncementDelegate.searchAnnouncementList(start,count,filter)))
  }

  def getList(start: Int, count: Int) = IsAuthenticated {
    username =>
      implicit request =>
    Ok(announcement.list(AnnouncementDelegate.getAnnouncementList(start,count)))
  }

  def getAnnouncementListById(id: String) = IsAuthenticated {
    username =>
      implicit request =>
    Ok(announcement.announcement_info(AnnouncementDelegate.getAnnouncementById(id)))
  }

  def getUpdateForm(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(announcement.update(AnnouncementService.getAnnouncementListById(id)))
  }

  def submitUpdateForm = Action {
    implicit request =>
      AnnouncementDelegate._announcementProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        announcement => {
          val params = request.body.asFormUrlEncoded.get
          val id = request.body.asFormUrlEncoded.get("id").head
          AnnouncementDelegate.submitUpdateAnnouncementForm(params)
          Redirect("/reminders/"+id)
        }
      )

  }


  def getAddForm(start: Int, count: Int) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(announcement.add(AnnouncementDelegate.getAnnouncementList(start,count)))
  }

  def submitAddForm = Action {
    implicit request =>
      AnnouncementDelegate._announcementProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        announcement => {
          var params = request.body.asFormUrlEncoded.get
          AnnouncementDelegate.submitAddAnnouncementForm(params)
          Redirect("/reminders")
        }
      )
  }

  def deleteInformation(id: String) = Action {
    implicit request =>
      val params = Map("id" -> Seq(id))
      AnnouncementDelegate.deleteInformation(params)
      Redirect("/announcements")
  }

}

