package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models._
import scalikejdbc._
import models.NewThread

object ThreadActions extends Controller {

  val newThreadForm = Form(
    mapping(
      "name" -> nonEmptyText(),
      "message" -> text(maxLength = 255)
    )(NewThread.apply)(NewThread.unapply)
  )

  def list(boardId:Long) = Action {
    implicit request =>
      val threads = Threads.findAll()
      val board = Boards.findById(boardId)

      board match {
        case Some(b) =>Ok(views.html.thread.list(b, threads, newThreadForm))
        case None => NotFound
      }
  }

  def create(boardId:Long) = Action {
    implicit request =>
      newThreadForm.bindFromRequest.fold(
        errors => Redirect(routes.ThreadActions.list(boardId)),
        newThread => {
          val board = Boards.findById(boardId)
          board map(_.create(newThread))

          Redirect(routes.ThreadActions.list(boardId))
        }
      )
  }

}
