package controllers

import models.UserId
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller}
import repository.UserRepo

object UserController extends Controller {
  def get(_id: Long) = Action { request =>
    val id = UserId(_id.toLong)
    val repo = new UserRepo()
    val user = repo.get(id)

    val jsValue: JsValue = Json.obj(
      "id" -> user.id.value,
      "handle" -> user.handle,
      "name" -> user.name,
      "joined" -> user.joined.toEpochMilli)
    Ok(jsValue)
  }
}
