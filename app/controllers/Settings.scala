package controllers

import play.api.mvc._
import views._
import ws.delegates.AuditLogDelegate
import Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 12/10/12
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
object Settings extends Controller with Secured {

  def auditLog() = IsAuthenticated{
    username =>
      implicit request =>
        Ok(html.settings.audit_log(AuditLogDelegate.getAllLogs))
  }

}
