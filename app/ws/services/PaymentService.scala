package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.cache.{EhCachePlugin, Cache}
import ws.helper.DateWithTime
import collection.mutable.ListBuffer
import ws.generator.UUIDGenerator
import java.util.Date
import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 2/21/13
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */

case class PaymentList(var id: String, patientId : Option[String], payment: Option[String], dateOfPayment: Option[String], userName: Option[String])
case class PaymentDetails(p: PaymentList, totalPayment: Option[Double], balance: Option[Double], totalPrice: Option[Double])

object PaymentService {

  def getRowCountOfTable(tableName: String): Long = {
    DB.withConnection {
      implicit c =>
        val rowCount = SQL("""select count(*) as c from """+tableName+""" where status = '1' """).apply().head
        rowCount[Long]("c")
    }
  }


  def getPaymentsByPatientId(start: Int, count: Int, patientId: String): List[PaymentList] = {
    DB.withConnection {
      implicit c =>
        val paymentList: List[PaymentList] = SQL(
          """
            |select
            |pay.id,
            |pay.patient_id,
            |pay.user_name,
            |pay.date_of_payment,
            |pay.payment,
            |pat.first_name,
            |pat.middle_name,
            |pat.last_name,
            |pay.user_name
            |from payments pay
            |LEFT OUTER JOIN patients pat ON pay.patient_id=pat.id
            |where pay.patient_id = {patient_id}
            |ORDER BY pay.date_of_payment desc
            |LIMIT {start}, {count}
          """.stripMargin).on('start -> start, 'count -> count, 'patient_id -> patientId).as {
          get[String]("id") ~
            get[Option[String]]("patient_id") ~
            get[Option[String]]("payment")~
            get[Option[Date]]("date_of_payment") ~
            get[Option[String]]("user_name")map {
            case a ~ b ~ c  ~ d ~ e  => PaymentList(a, b, c, Some(d.toString.replace("Some", "").replace("(","").replace(".0)","")), e)
          } *
        }
        paymentList
    }
  }

  def getPaymentDetails(start: Int, count: Int,patientId: String) : List[PaymentDetails] ={
    DB.withConnection {
      implicit c =>
      val paymentDetails: List[PaymentDetails] = SQL(
        """
          |select
          |pay.id,
          |pay.patient_id,
          |pay.user_name,
          |pay.date_of_payment,
          |pay.payment,
          |pat.first_name,
          |pat.middle_name,
          |pat.last_name,
          |pay.user_name,
          |SUM(pay.payment) as total_payment,
          |SUM(tp.price) as total_price,
          |(SUM(tp.price) - SUM(pay.payment)) as balance
          |from
          |payments pay
          |LEFT OUTER JOIN patients pat ON pay.patient_id=pat.id
          |LEFT OUTER JOIN treatment_plan tp ON pay.patient_id=tp.patient_id
          |where pay.patient_id = {patient_id}
        """.stripMargin).on('start -> start, 'count -> count, 'patient_id -> patientId).as{
          get[Option[String]]("id") ~
          get[Option[String]]("patient_id") ~
          get[Option[String]]("payment")~
          get[Option[Date]]("date_of_payment") ~
          get[Option[String]]("user_name") ~
          get[Option[BigDecimal]]("total_payment")~
          get[Option[BigDecimal]]("balance") ~
          get[Option[BigDecimal]]("total_price")  map {
          case a ~ b ~ c  ~ d ~ e ~ f ~ g ~ h => PaymentDetails(PaymentList(a.toString, b, c, Some(d.toString.replace("Some(", "").replace(".0)","")), e), Some(f.toString().replace("Some(", "").replace(")","").replace("None","0").toDouble), Some(g.toString().replace("Some(", "").replace(")","").replace("None","0").toDouble), Some(h.toString().replace("Some(", "").replace(")","").replace("None","0").toDouble))
        }*
      }
        paymentDetails
    }
  }

  /*def getPaymentsByPatientIdById(patientId: String, id: String): List[PaymentList] = {
    DB.withConnection {
      implicit c =>
        val paymentList: List[PaymentList] = SQL(
          """
            |select
            |pay.id,
            |pay.patient_id,
            |pay.user_name,
            |pay.date_of_payment,
            |pay.payment,
            |pat.first_name,
            |pat.middle_name,
            |pat.last_name,
            |pay.user_name,
            |SUM(pay.payment) as total_payment,
            |SUM(tp.price) as total_price,
            |(SUM(tp.price) - SUM(pay.payment)) as balance
            |from payments pay
            |LEFT OUTER JOIN patients pat ON pay.patient_id=pat.id
            |where pay.patient_id = {patient_id}
            |ORDER BY pay.date_of_payment desc
            |LIMIT {start}, {count}
          """.stripMargin).on('patient_id -> patientId, 'id -> id).as {
          get[String]("id") ~
            get[Option[String]]("patient_id") ~
            get[Option[String]]("payment")~
            get[Option[Date]]("date_of_payment") ~
            get[Option[String]]("user_name")map {
            case a ~ b ~ c  ~ d ~ e  => PaymentList(a, b, c, Some(d.toString.replace("Some", "").replace("(","").replace(".0)","")), e)
          } *
        }
        paymentList
    }
  }
*/
  /*def getTotalPayment(patientId: String)  {
    DB.withConnection {
      implicit c =>
        val getTotalPayment = SQL(
          """
            |select
            |SUM(payment) as total_payment
            |from payments
            |where patient_id = {patient_id}
            |ORDER BY pay.date_of_payment desc
          """.stripMargin
        ).on('patient_id -> patientId).apply().head
        getTotalPayment[String]("total_payment")
    }
  }*/

  val username =  Cache.getAs[String]("user_name").toString.replace("Some", "").replace("(","").replace(")","")

  def addPayment(d: PaymentList): Long = {
    val currentUser = username
    val task = "Add"
    d.id = UUIDGenerator.generateUUID("payments")
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |INSERT INTO payments
            |VALUES
            |(
            |{id},
            |{patient_id},
            |{date_of_payment},
            |{payment},
            |{user_name})
          """.stripMargin).on(
          'id -> d.id,
          'patient_id -> d.patientId,
          'payment -> d.payment,
          'date_of_payment -> DateWithTime.dateNow,
          'user_name -> username
        ).executeUpdate()
        AuditLogService.logTaskPayment(d, currentUser, task)
    }
  }

  def updatePayment(p: PaymentList): Long = {
    val currentUser = username
    val task = "Update"
    DB.withConnection {
      implicit c =>
        SQL(
          """
            |UPDATE payments SET
            |patient_id = {patient_id},
            |payment = {payment},
            |date_of_payment = {date_of_payment},
            |user_name = {user_name}
            |WHERE id = {id}
          """.stripMargin).on(
          'id -> p.id,
          'patient_id -> p.patientId,
          'payment -> p.payment,
          'date_of_payment -> p.dateOfPayment,
          'user_name -> p.userName
        ).executeUpdate()
        AuditLogService.logTaskPayment(p, currentUser, task)
    }
  }

}
