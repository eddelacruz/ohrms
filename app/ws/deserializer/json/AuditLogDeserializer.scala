package ws.deserializer.json

import ws.services.AuditLog
import play.api.libs.json._

/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 12/10/12
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
trait AuditLogDeserializer {

  implicit object AuditLogFormat extends Format[AuditLog]{
    def reads(j: JsValue): AuditLog = AuditLog(
      (j \ "id").as[String],
      (j \ "task").as[String],
      (j \ "description").as[String],
      (j \ "dateCreated").as[String],
      (j \ "author").as[String]
    )

    def writes(a: AuditLog): JsValue = JsObject(
      Seq(
        "id" -> JsString(a.id),
        "task" -> JsString(a.task),
        "description" -> JsString(a.description),
        "dateCreated" -> JsString(a.dateCreated),
        "author" -> JsString(a.author)
      )
    )
  }

}
