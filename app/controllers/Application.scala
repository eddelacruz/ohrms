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
import ws.services.{DentistService, LoginService}
import ws.services.{LoginService, ClinicService}
import views.html.patient
import ws.delegates.{AnnouncementDelegate, PatientDelegate, AppointmentDelegate}
import org.reflections.vfs.Vfs.File
import javax.crypto.{SecretKey,SecretKeyFactory}
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.PBEKeySpec
import org.apache.commons.codec.binary.Base64

object Application extends Controller{

  val loginForm = Form(
    tuple(
      "user_name" -> text,
      "password" -> text
    ) verifying("Invalid username or password", result => result match {
      case (user_name, password) => {
        LoginService.authenticate(user_name, hash(password)).isDefined
      }
    })
  )

  val questionForm = Form(
    tuple(
      "user_name" -> text,
      "question" -> text,
      "answer" -> text
    ) verifying("Not matched!", result => result match {
      case (user_name,question,answer) => {
        LoginService.securityQuestionAuthenticate(user_name, question, answer).isDefined
      }
    })
  )

  def billySample = Action{
    implicit request =>
      Ok(html.reports.billy())
  }

  def login = Action {
    implicit request =>
      //println(hash("admin"))
      val clinic = ClinicService.getClinic()
      Cache.set("clinic-id", ClinicService.getClinic().head.id)
      Cache.set("clinic-name", ClinicService.getClinic().head.clinicName)
      Cache.set("clinic-address", ClinicService.getClinic().head.address)
      Cache.set("clinic-contact_no", ClinicService.getClinic().head.contactNumber)
      if(Cache.getAs[String]("wait") == Some("yes")){
        println(">>>>>>>>>>>>>> tae")
        tries += 1
        Ok(views.html.login_wait(loginForm))
      } else if(Cache.getAs[String]("wait") == Some("why")){
        Ok(views.html.login_question(DentistService.getAllSecurityQuestion(),questionForm))
      } else {
        Ok(views.html.login(loginForm))
      }
  }

  def question = Action {
    implicit request =>
      if(Cache.getAs[String]("wait") == Some("yes")){
        println(">>>>>>>>>>>>>> tae")
        tries += 1
        Ok(views.html.login_wait(loginForm))
      } else {
        Ok(views.html.login_question(DentistService.getAllSecurityQuestion(),questionForm))
      }
  }

  def countdown = Action {
    implicit request =>
      Ok(views.html.login_wait(loginForm))
  }

  def hash(pass: String): String = {
    //random.nextBytes(salt);
    val salt = "ohrms"
    val spec = new PBEKeySpec(pass.toArray, salt.getBytes, 65536, 128)
    val f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    val hash = f.generateSecret(spec)
    val digest = Base64.encodeBase64(hash.getEncoded)
    //val result = new String(digest, "ASCII")
    new String(digest, "ASCII")
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
          val usrList = LoginService.authenticate(userList._1, hash(userList._2)).get
          Cache.set("user_name", usrList.username)
          Cache.set("role", usrList.role)
          println(">>> Successfully logged in: " + Cache.getAs[String]("user_name").toString)
          Redirect(routes.Application.dashboard()).withSession(Security.username -> usrList.username)
        }
      )
  }

  def questionAuthenticate = Action {
    implicit request =>
      questionForm.bindFromRequest.fold(
        formWithErrors => {
          tries += 1
          println(">>>>>>>>>>>>>>>>>"+tries)

          if(tries == 3){
            Cache.set("wait", "yes")
            Redirect(routes.Application.question())
          } else if(tries >= 5){
            Cache.set("wait", "why")
            Redirect(routes.Application.question())
          } else {
            BadRequest(views.html.login_question(DentistService.getAllSecurityQuestion(),formWithErrors))
          }

        }, userList => {
          tries = 0
          Cache.set("wait", "")
          val usrList = LoginService.securityQuestionAuthenticate(userList._1, userList._2, userList._3).get
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
        Ok(html.dboard(PatientDelegate.getPatientLastVisit(start, count), AppointmentDelegate.getAppointmentsToday, AnnouncementDelegate.getAnnouncementsToday()))
  }

  def logout = Action {
    Cache.set("user_name", "")
    Cache.set("role", "")
    Cache.set("clinic-id", "")
    Cache.set("clinic-name", "")
    Cache.set("clinic-address", "")
    Cache.set("clinic-contact_no", "")
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

}
