package ws.services

import anorm._
import anorm.SqlParser._
import anorm.RowParser._
import anorm.ResultSetParser._
import play.api.db._
import play.api.Play.current


/**
 * Created with IntelliJ IDEA.
 * User: cindy
 * Date: 12/6/12
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */
case class UserList(var username:String, password:String, role: Int, question: String, answer: String)

object LoginService {

  val userList = {
    get[String]("user_name") ~
    get[String]("password") ~
    get[Int]("role") ~
    get[String]("question") ~
    get[String]("answer")  map {
      case user_name~password~role~question~answer=>
        UserList(user_name, password, role, question, answer)
    }
  }


  def findByUserName(user_name:String): Option[UserList] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          |select
          |user_name,
          |password,
          |role,
          |question,
          |sq.question,
          |answer
          |from users
          |left outer join security_questions sq ON question = sq.id
        """.stripMargin)
        .on('user_name -> user_name).as(LoginService.userList.singleOpt)
    }
  }


  def authenticate(user_name: String, password: String): Option[UserList] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          |select
          |u.user_name,
          |u.password,
          |u.role,
          |u.question,
          |sq.question,
          |u.answer
          |from users u
          |left outer join security_questions sq ON u.question = sq.id
          |where user_name = {user_name} and password = {password}
          |and status = {status}
        """.stripMargin)
      .on(
        'user_name -> user_name,
        'password -> password,
        'status -> 1
      ).as(LoginService.userList.singleOpt)
    }
  }

  def securityQuestionAuthenticate(userName: String, question: String, answer: String): Option[UserList] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          |select
          |u.user_name,
          |u.password,
          |u.role,
          |u.question,
          |sq.question,
          |u.answer
          |from users u
          |left outer join security_questions sq ON u.question = sq.id
          |where u.user_name = {user_name} and sq.question = {question} and u.answer = {answer}
        """.stripMargin)
        .on(
        'user_name -> userName,
        'question -> question,
        'answer -> answer
      ).as(LoginService.userList.singleOpt)
    }
  }

}