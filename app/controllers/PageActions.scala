package controllers

import scalikejdbc.DB
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.{Threads, Comments, NewComment}

object PageActions extends PageActions(Threads,Comments)
class PageActions(Threads:Threads,Comments:Comments) extends Controller {

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

  def read(id: Long) = Action {
    implicit request =>
      Threads.findById(id) match {
        case Some(t) => {
          val comments = Comments.findBy(t)
          Ok(views.html.thread.page(t, comments, commentForm))
        }
        case None => NotFound
      }
  }

  def delete(id: Long) = Action {
    implicit request =>
      deleteForm.bindFromRequest().fold(
        errors => Redirect(routes.Application.index()),
        id => {
          Comments.delete(id)
          Redirect(routes.Application.index())
        }
      )
  }

  def create(id: Long) = Action {
    implicit request =>
      commentForm.bindFromRequest().fold(
        errors => Redirect(routes.Application.index()).flashing("errors" -> "error"),
        comment => {
          Threads.findById(id) match {
            case Some(t) => {
              t.comment(comment)
              Redirect(routes.PageActions.read(id))
            }
            case None => NotFound
          }
        }
      )
  }

}
