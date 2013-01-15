package controllers

import ws.delegates.TreatmentPlanDelegate
import play.api.mvc.{Controller, Action}
import controllers.Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 12/17/12
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
object TreatmentPlan extends Controller with Secured{

  def addTreatment = IsAuthenticated {
    username =>
      implicit request =>
        val params = request.body.asJson.get
        TreatmentPlanDelegate.addTreatment(params)
        Status(200)
  }

}
