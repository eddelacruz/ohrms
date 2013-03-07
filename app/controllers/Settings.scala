package controllers

import play.api.mvc._
import views._
import ws.delegates.{TreatmentPlanDelegate, AuditLogDelegate}
import Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 12/10/12
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
object Settings extends Controller with Secured {

  def auditLog(start: Int, count: Int) = IsAuthenticated{
    username =>
      implicit request =>
        Ok(html.settings.audit_log(AuditLogDelegate.getAllLogs(start,count)))
  }

  def searchAuditLog(start: Int, count: Int, filter: String) = Action {
    println("start "+start+" count"+count);
    Ok(html.settings.audit_log(AuditLogDelegate.searchAuditLog(start,count,filter)))
  }

  def settings = Action {
    implicit request =>
      Ok(html.settings.setting())
  }

  def teethNaming = Action {
    implicit request =>
      Ok(html.settings.teeth_naming())
  }

}
