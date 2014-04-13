package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.{NewComment, Comments, Threads, NewThread}
import scalikejdbc._

object ThreadActions extends Controller {

  val newThreadForm = Form(
    mapping(
      "name" -> nonEmptyText()
    )(NewThread.apply)(NewThread.unapply)
  )

  def list = Action {
    implicit request =>
      val threads = DB readOnly {
        implicit session => Threads.findAll()
      }
      Ok(views.html.thread.list(threads, newThreadForm))
  }

  def create = Action {
    implicit request =>
      newThreadForm.bindFromRequest.fold(
        errors => Redirect(routes.ThreadActions.list()),
        newThread => {
          DB localTx {
            implicit session => Threads.create(newThread)
          }
          Redirect(routes.ThreadActions.list())
        }
      )
  }

}
