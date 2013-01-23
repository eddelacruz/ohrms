package ws.deserializer.json

import play.api.libs.json._
import ws.services.AnnouncementList
import play.api.cache.{EhCachePlugin, Cache}
import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 1/22/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */

trait AnnouncementListDeserializer {

  implicit object AnnouncementListFormat extends Format[AnnouncementList]{
    def reads(json: JsValue): AnnouncementList = AnnouncementList(
      (json \ "id").as[String],
      (json \ "userName").as[String],
      (json \ "announcement").as[String],
      (json \ "date_created").as[String]
    )

    def writes(d: AnnouncementList): JsValue = JsObject(

      Seq(
        "id" -> JsString(d.id),
        "userName" -> JsString(d.userName),
        "announcement" -> JsString(d.announcement),
        "dateCreated" -> JsString(d.dateCreated)
      )
    )
  }

}


