package controllers

import models.{Iterator, User}
import play.api.libs.json._

object JsonConverter {
  def user(u: User): JsValue = Json.obj(
    "id" -> u.id.value,
    "handle" -> u.handle,
    "name" -> u.name,
    "joined" -> u.joined.toEpochMilli)

  /*
   * Quite annoying.
   * The reason for all this weirdness is omitting the field for an empty array or for non existing continuation
   */
  def iterator[T](it: Iterator[T])(converter: T => JsValue) = JsObject(
    Seq[(String, JsValue)]() ++
      (if (it.values.isEmpty) None else Some("values" -> JsArray(it.values.map(converter)))) ++
      it.continuation.map(c => "continuation" -> JsNumber(c)))
}
