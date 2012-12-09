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
case class UserList(username:String, password:String)

object LoginService {

/** def getUserList: List[UserList] = {
    DB.withConnection {
      implicit c =>
        val userList: List[UserList] = SQL(
          """
            |select
            |user_name,
            |password
            |from
            |users
          """.stripMargin).as{
            get[String]("user_name") ~
            get[String]("password") map {
            case a~b => UserList(a,b)
          }*
        }
        userList
    }
  } */

  val userList = {
    get[String]("user_name") ~
    get[String]("password")  map {
      case user_name~password=>
        UserList(user_name, password)
    }
  }

  def findByUserName(user_name:String): Option[UserList] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          |select
          |user_name,
          |password
          |from
          |users
        """.stripMargin)
        .on('user_name -> user_name).as(LoginService.userList.singleOpt)
    }
  }


  def authenticate(user_name: String,password: String): Option[UserList] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          |select
          |user_name,
          |password
          |from
          |users
          |where user_name = {user_name} and password = {password}
        """.stripMargin)
      .on(
        'user_name -> user_name,
        'password -> password
      ).as(LoginService.userList.singleOpt)
    }
  }

}
