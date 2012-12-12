package ws.services

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB

/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/11/12
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */

case class DentistList(var id: String, firstName: String, middleName: String, lastName: String, address: String, contactNo: String, prcNo: String, image: String, userName: String, name: String)

object DentistService {

  def getDentistList(start: Int, count: Int): List[DentistList] = {
    val status = 1
    DB.withConnection {
      implicit c =>
        val dentistList: List[DentistList] = SQL(
          """
            |select
            |d.id,
            |d.first_name,
            |d.middle_name,
            |d.last_name,
            |d.address,
            |d.contact_no,
            |d.prc_no,
            |d.image,
            |u.user_name,
            |s.name
            |from
            |specializations s
            |LEFT OUTER JOIN dentists d
            |ON s.dentist_id=d.id
            |LEFT OUTER JOIN users u
            |ON d.user_id=u.id
            |where d.status = {status}
            |LIMIT {start}, {count}
          """.stripMargin).on('status -> status, 'start -> start, 'count -> count).as {
          get[String]("id") ~
            get[String]("first_name") ~
            get[String]("middle_name") ~
            get[String]("last_name") ~
            get[String]("address") ~
            get[String]("contact_no") ~
            get[String]("prc_no") ~
            get[String]("image")~
            get[String]("user_name")~
            get[String]("name") map {
            case a ~ b ~ c ~ d ~ e ~ f ~ g ~ h ~ i ~ j => DentistList(a, b, c, d, e, f, g, h, i, j)
          } *
        }
        dentistList
    }
  }

}
