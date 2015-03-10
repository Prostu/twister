package controllers

import play.api._
import play.api.mvc._

// TODO(stefan): package should be com.sfilip.twister

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}