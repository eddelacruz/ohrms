package ws.helper

import play.api.Play
import play.api.libs.ws.WS
import play.api.libs.ws._
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.concurrent.Promise

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/10/12
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
trait WsHelper {

  private val ohrmsUrl = Play.current.configuration.getString("ohrms.url")

  def doGet(endpoint: String): Promise[Response] = {
    val rest = ohrmsUrl.get + endpoint
    //println("GETTING: >>>>>>>>"+rest)
    WS.url(rest).get
  }

  def doGet(endPoint: String, params: Map[String, String]): Promise[Response] = {
    var rest = ohrmsUrl.get + endPoint
    params.map { p =>
      rest += "&" + p._1 + "=" + p._2
    }
    //println("GETTING: >>>>>>>>"+rest)
    WS.url(rest).get
  }

  def doPost(endPoint: String, params: Map[String, Seq[String]]) = {
    var rest = ohrmsUrl.get + endPoint
    println("POSTING: >>>>>>>>"+rest)
    WS.url(rest).post(params).await.get
  }

  def doPost(endPoint: String, params: JsValue) = {
    var rest = ohrmsUrl.get + endPoint
    println("POSTING: >>>>>>>>"+rest)
    WS.url(rest).post(params).await.get
  }

  def doPut(endPoint: String, params: Map[String, Seq[String]]) = {
    var rest = ohrmsUrl.get + endPoint
    println("PUTTING: >>>>>>>>"+rest)
    WS.url(rest).put(params).await.get
  }

  def doDelete(endPoint: String, params: Map[String, Seq[String]]) = {
    var rest = ohrmsUrl.get + endPoint
    println("DELETING: >>>>>>>>"+rest)
    params.map { p =>
      rest += "&" + p._1 + "=" + p._2(0)
    }
    WS.url(rest).delete.await.get
  }

}
