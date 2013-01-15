package controllers

import play.api.mvc._
import play.api.mvc.Action
import views.html.scheduler
import Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/14/12
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
object Scheduler extends Controller with Secured{

  def getCalendar = IsAuthenticated {
    username =>
      implicit request =>
      Ok(scheduler.calendar())
  }

}
