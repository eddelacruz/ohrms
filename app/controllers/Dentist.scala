package controllers

import play.api.mvc.{Action, Controller}
import views.html.dentist
import ws.delegates.DentistDelegate

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
}