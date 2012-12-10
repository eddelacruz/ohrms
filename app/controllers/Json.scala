package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import views.html.{patient, modal}
import ws.services.{PatientList, PatientService}
import ws.generator.UUIDGenerator
import ws.helper.WsHelper
import ws.deserializer.json.PatientListDeserializer


/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/10/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
object Json extends Controller with WsHelper with PatientListDeserializer {

  def getPatientList = Action {
    Ok(JsObject(Seq("PatientList" -> toJson(PatientService.getPatientList))))
  }

  def getPatientTreatmentPlan(id: String) = Action {
    Ok(JsObject(Seq("PatientList" -> toJson(PatientService.getPatientListById(id)))))
  }

}
