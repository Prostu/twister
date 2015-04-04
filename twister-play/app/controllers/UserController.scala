package controllers

import models.UserId
import play.api.mvc.{Action, Controller}
import repository.UserRepo

object UserController extends Controller {
  def user(id: Long) = Action { request =>
    val repo = new UserRepo()
    val user = repo.get(UserId(id))

    Ok(JsonConverter.user(user))
  }

  def users(continuation: Option[Long], batchSize: Option[Long]) = Action { request =>
    val repo = new UserRepo()
    val iterator = repo.iterate(continuation, batchSize)

    Ok(JsonConverter.iterator(iterator)(JsonConverter.user))
  }
}
