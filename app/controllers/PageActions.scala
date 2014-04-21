package controllers

import scalikejdbc.DB
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.{Boards, Threads, Comments, NewComment}

object PageActions extends PageActions(Boards,Threads,Comments)
class PageActions(Boards:Boards,Threads:Threads,Comments:Comments) extends Controller {

  val deleteForm = Form(
    mapping(
      "id" -> longNumber
    )((id) => id)((id: Long) => Some(id))
  )

  val commentForm = Form(
    mapping(
      "name" -> text(),
      "comment" -> nonEmptyText(maxLength = 255)
    )(NewComment.apply)(NewComment.unapply)
  )

  def read(boardId:Long,threadId: Long) = Action {
    implicit request =>
      Boards.findById(boardId).flatMap{ b =>
        Threads.findById(threadId).map { t =>
          val comments = Comments.findBy(t)
          Ok(views.html.thread.page(b,t, comments, commentForm))
        }
      }.getOrElse(NotFound)
  }

  def delete(boardId: Long,threadId:Long,commentId: Long) = Action {
    implicit request =>
      deleteForm.bindFromRequest().fold(
        errors => Redirect(routes.Application.index()),
        id => {
          Comments.delete(commentId)
          Redirect(routes.Application.index())
        }
      )
  }

  def create(boardId: Long,threadId:Long) = Action {
    implicit request =>
      commentForm.bindFromRequest().fold(
        errors => Redirect(routes.Application.index()).flashing("errors" -> "error"),
        comment => {
          Threads.findById(threadId).map{ t =>
            t.comment(comment)
            Redirect(routes.PageActions.read(boardId,threadId))
          }.getOrElse(NotFound)
        }
      )
  }

}
