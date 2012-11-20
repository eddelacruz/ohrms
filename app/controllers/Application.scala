package controllers

import play.api._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views._

object Application extends Controller {
  
  def index = Action {
    Ok(html.index("Your new application is ready."))
  }

  def samplePdf(id: String): Result = {
    return PDF.ok(html.samplePdf.render)
  }
}