package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views._

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 12/18/12
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
object Reports {
  def _patientList(id: String): Result = {
    return PDF.ok(html.reports._patientList.render)
  }
}

