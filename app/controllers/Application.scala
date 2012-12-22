package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.mvc.Result._
import util.pdf.PDF
import views._
import ws.services.LoginService

object Application extends Controller {

  def IsAuthenticated(r: Request[AnyContent] => Result): Action[AnyContent] = {
    Action { implicit request =>
      println("LoggingAction")
      r(request)
    }
  }

  val loginForm = Form(
    tuple(
      "user_name" -> text,
      "password" -> text
    ) verifying ("Invalid username or password", result => result match {
      case (user_name, password) => LoginService.authenticate(user_name, password).isDefined
    })
  )

  def login = IsAuthenticated { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      userList => Redirect(routes.Application.dashboard()).withSession("user_name" -> userList._1)
    )
  }


  private def username(request: RequestHeader) = request.session.get("user_name")
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login())


 /** def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { userList =>
    Action(request => f(userList)(request))
  }  */


  def dashboard = Action {
    Ok(html.dboard("Your new application is ready."))
  }

/*  def samplePdf(id: String): Result = {
    return PDF.ok(html.samplePdf.render)
  }*/

}