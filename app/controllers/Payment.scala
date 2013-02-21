package controllers

import play.api._
import cache.Cache
import play.api.Play.current
import play.api.mvc.Controller
import data.Form
import data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views.html.{payment, modal}
import ws.services.{PaymentList, PaymentService}
import ws.delegates._
import ws.generator.UUIDGenerator
import ws.services
import Application.Secured
import scala.Some

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 2/5/13
 * Time: 2:16 AM
 * To change this template use File | Settings | File Templates.
 */
object Payment extends Controller with Secured{

  /*def getPaymentsByPatientId(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(payment.counter())
  }*/

  def getList(start: Int, count: Int, patientId: String) = IsAuthenticated {
    username =>
      implicit request =>
        Ok(modal._add_payment(PaymentDelegate.getPaymentsByPatientId(start,count,patientId)))
  }

  /*def getUpdateForm(id: String) = IsAuthenticated {
    username =>
      implicit request =>
        val patientId = "aaa"
        Cache.get("role") match {
          case Some(0) => Ok(payment.update(PaymentService.getPaymentsByPatientIdById(patientId,id)))
          case Some(1) => Ok(payment.update(PaymentService.getPaymentsByPatientIdById(patientId,id)))
          case _ => Redirect("/payments/"+id)
        }
  }*/

  def submitUpdateForm = Action {
    implicit request =>
      PaymentDelegate._paymentProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        payment => {
          val params = request.body.asFormUrlEncoded.get
          val id = request.body.asFormUrlEncoded.get("id").head
          PaymentDelegate.submitUpdatePaymentForm(params)
          Redirect("/payments/"+id)
        }
      )

  }


  /*def getAddForm = IsAuthenticated {
    username =>
      implicit request =>
        Cache.get("role") match {
          case Some(0) => Ok(payment.add())
          case Some(1) => Ok(payment.add())
          case _ => Redirect("/payments")
        }

  }*/

  def submitAddForm = Action {
    implicit request =>
      PaymentDelegate._paymentProfileForm.bindFromRequest.fold(
        formWithErrors => {
          println("Form errors: "+formWithErrors.errors)
          BadRequest
        },
        payment => {
          var params = request.body.asFormUrlEncoded.get
          PaymentDelegate.submitAddPaymentForm(params)
          Redirect("/payments")
        }
      )
  }

}



