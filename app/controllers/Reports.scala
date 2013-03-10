package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.mvc.Result
import util.pdf.PDF
import views._
import ws.delegates._
import ws.services.{AuditLogService, PaymentService}
import play.mvc.Result
import util.pdf.PDF
import views.html.reports
import controllers.Application.Secured

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 12/18/12
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
object Reports extends Controller with Secured{
  def _patientList(start: Int, count: Int): Result = {
    return PDF.ok(html.reports._patientList.render(PatientDelegate.getPatientList(start, count), "", ""))
  }
  def _patientListByDateRange(startDate: String, endDate: String): Result = {
    return PDF.ok(html.reports._patientList.render(PatientDelegate.getPatientListByDateRange(startDate, endDate), startDate, endDate))
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

  def _monthlyIncomeReport(year: Int, month: Int): Result = {
    return PDF.ok(html.reports._monthly_income.render(PaymentDelegate.getMonthlyIncome(year, month), year, month, PaymentService.getTotalPricesByDateRange(year, month)))
  }

  def _auditLogReport(module: String, dateStart: String, dateEnd: String): Result = {
    return PDF.ok(html.reports._audit_logs.render(AuditLogService.getAuditLogReport(module, dateStart, dateEnd), dateStart, dateEnd))
  }

  def getReportsList() = IsAuthenticated {
    username =>
      implicit request =>
        Ok(reports.reports())
  }

}

