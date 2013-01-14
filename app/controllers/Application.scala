package controllers

import play.api._
import cache.Cache
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.mvc.Result._
import play.api.mvc.RequestHeader
import play.api.mvc.Security.Authenticated
import util.pdf.PDF
import views._
import play.api.Play.current
import ws.services.LoginService
import views.html.patient
import ws.delegates.PatientDelegate

object Application extends Controller {

  val loginForm = Form(
    tuple(
      "user_name" -> text,
      "password" -> text
    ) verifying("Invalid username or password", result => result match {
      case (user_name, password) => {
        println("<><><><><><><><><><><><><><><><><>" + LoginService.authenticate(user_name, password))
        LoginService.authenticate(user_name, password).isDefined
      }
    })
  )

  def login = Action {
    implicit request =>
      Ok(views.html.login(loginForm))
  }

  def authenticate = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.login(formWithErrors)),
        userList => {
          Cache.set("user_name", userList._1)
          println("user_name is: " + userList._1)
          Redirect(routes.Application.dashboard())
        }
      )
  }



  //private def username(request: RequestHeader) = request.session.get("user_name")

  def dashboard = IsAuthenticated {
    username =>
      implicit request =>
        val start = 0
        val count = 5
        Ok(html.dboard(PatientDelegate.getPatientList(start,count)))
  }

  def logout = Action {
    Cache.set("user_name", null)
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }



  private def username(request: RequestHeader) = {
    Cache.getAs[String]("user_name")
  }

  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login())

  def IsAuthenticated(r: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) {
      user =>
        Action {
          request =>
            r(user)(request)
        }
    }
  }

  trait Secured {
    private def username(request: RequestHeader) = {
      Cache.getAs[String]("user_name")
    }

    private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login())

    def IsAuthenticated(r: => String => Request[AnyContent] => Result) = {
      Security.Authenticated(username, onUnauthorized) {
        user =>
          Action {
            request =>
              r(user)(request)
          }
      }
    }
  }

  /*  def samplePdf(id: String): Result = {
    return PDF.ok(html.samplePdf.render)
  }*/

}