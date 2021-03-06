package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models._
import scalikejdbc._
import models.NewThread

object ThreadActions extends ThreadActions(Threads,Boards)
class ThreadActions(Threads:Threads,Boards:Boards) extends Controller {

  val newThreadForm = Form(
    mapping(
      "name" -> nonEmptyText(),
      "message" -> text(maxLength = 255)
    )(NewThread.apply)(NewThread.unapply)
  )

  def list(boardId: Long) = Action {
    implicit request =>
      Boards.findById(boardId) match {
        case Some(b) => {
          val threads = Threads.findBy(b)
          Ok(views.html.thread.list(b, threads, newThreadForm))
        }
        case None => NotFound
      }
  }

  def create(boardId: Long) = Action {
    implicit request =>
      newThreadForm.bindFromRequest.fold(
        errors => Redirect(routes.ThreadActions.list(boardId)),
        newThread => {
          Boards.findById(boardId).map{ b =>
            b.create(newThread)
            Redirect(routes.ThreadActions.list(boardId))
          }.getOrElse(NotFound)
        }
      )
  }

}
