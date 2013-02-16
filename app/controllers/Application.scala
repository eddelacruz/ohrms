package controllers

import play.api._
import cache.Cache
import mvc.MultipartFormData.FilePart
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.mvc.Result._
import play.api.mvc.RequestHeader
import play.api.mvc.Security.Authenticated
import util.pdf.PDF
import views._
import ws.services.LoginService
import views.html.patient
import ws.delegates.{AnnouncementDelegate, PatientDelegate, AppointmentDelegate}
import org.reflections.vfs.Vfs.File
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import scala.util.Random
import org.apache.commons.codec.binary.Base64

object Application extends Controller{

  val loginForm = Form(
    tuple(
      "user_name" -> text,
      "password" -> text
    ) verifying("Invalid username or password", result => result match {
      case (user_name, password) => {
        LoginService.authenticate(user_name, password).isDefined
      }
    })
  )

  def billySample = Action{
    implicit request =>
      Ok(html.reports.billy())
  }


  def login = Action {
    implicit request =>
      //val f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
      //val salt = Random.nextString(8)
      //val salt = hash("elizer")
      //val key = f.generateSecret(PB)
      //println(">>>>>>>>>>>>Secret Key Factory "+salt)
      println(Cache.getAs[String]("wait"))
      if(Cache.getAs[String]("wait") == Some("yes")){
        println(">>>>>>>>>>>>>> tae")
        tries += 1
        Ok(views.html.login_wait(loginForm))
      } else if(Cache.getAs[String]("wait") == Some("why")){
        Ok(views.html.login_question(loginForm))
      } else {
        Ok(views.html.login(loginForm))
      }
  }

  def countdown = Action {
    implicit request =>
      Ok(views.html.login_wait(loginForm))
  }

  def question = Action {
    implicit request =>
      Ok(views.html.login_question(loginForm))
  }

  var tries = 0

  def authenticate = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => {
          tries += 1
          println(">>>>>>>>>>>>>>>>>"+tries)

          if(tries == 3){
            Cache.set("wait", "yes")
            Redirect(routes.Application.login())
          } else if(tries >= 5){
            Cache.set("wait", "why")
            Redirect(routes.Application.login())
          } else {
            BadRequest(views.html.login(formWithErrors))
          }

        }, userList => {
          tries = 0
          Cache.set("wait", "")
          val usrList = LoginService.authenticate(userList._1, userList._2).get
          Cache.set("user_name", usrList.username)
          Cache.set("role", usrList.role)
          println(">>> Successfully logged in: " + Cache.getAs[String]("user_name").toString)
          Redirect(routes.Application.dashboard()).withSession(Security.username -> usrList.username)
        }
      )
  }

  def dashboard = IsAuthenticated {
    username =>
      implicit request =>
        val start = 0
        val count = 5
        Ok(html.dboard(PatientDelegate.getPatientList(start,count), AppointmentDelegate.getAppointmentsToday, AnnouncementDelegate.getAnnouncementsToday()))
  }

  def logout = Action {
    Cache.set("user_name", null)
    Cache.set("role", null)
    Redirect(routes.Application.login).withNewSession
  }

  def imageForm = Action {
    implicit request =>
      Ok(html.image_upload())
  }

  def upload = Action {
    implicit request =>
      println(request.body.asRaw.get.asFile)
      /*map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      picture.ref.moveTo(new File("/tmp/picture"))
      Ok("File uploaded")
      }.getOrElse {
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+r)
        Redirect("/")
      }*/
      Ok("file uploaded")
  }

  private def username(request: RequestHeader) = {
    //Cache.getAs[String]("user_name")
    request.session.get(Security.username)
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
      //Cache.getAs[String]("user_name")
      request.session.get(Security.username)
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
