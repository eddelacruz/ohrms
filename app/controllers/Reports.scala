package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views._
import ws.delegates._
import ws.services.PaymentService

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 12/18/12
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
object Reports {
   def _patientList(start: Int, count: Int): Result = {
    return PDF.ok(html.reports._patientList.render(PatientDelegate.getPatientList(start, count)))
  }
  def _dentistList(start: Int, count: Int): Result = {
    return PDF.ok(html.reports._dentistList.render(DentistDelegate.getDentistList(start, count)))
  }
  def _serviceList(start: Int, count: Int): Result = {
    return PDF.ok(html.reports._serviceList.render(DentalServiceDelegate.getDentalServiceList(start, count)))
  }
  def _staffList(start: Int, count: Int): Result = {
    return PDF.ok(html.reports._staffList.render(StaffDelegate.getStaffList(start, count)))
  }

  def _dentalCertificate(patientId: String): Result = {
    return PDF.ok(html.reports._dental_certificate.render(patientId))
  }

    def _paymentReceipt(id: String, start: Int, count: Int): Result = {
      return PDF.ok(html.reports._payment_receipt.render(PaymentDelegate.getPaymentById(id), ClinicDelegate.getClinicList(start,count), PaymentService.getPaymentBalance(id)))
    }

  def _individualPatientReport(id: String, start: Int, count: Int): Result = {
    return PDF.ok(html.reports._individual_patient.render(PatientDelegate.getPatientListById(id), TreatmentPlanDelegate.getTreatmentPlan(id,start,count)))
  }

}

