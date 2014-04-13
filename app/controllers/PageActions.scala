package controllers

import scalikejdbc.DB
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.{Threads, Comments, NewComment}

object PageActions extends Controller {

  val deleteForm = Form(
    mapping(
      "id" -> longNumber
    )((id) => id)((id:Long)=> Some(id))
  )

  val commentForm = Form(
    mapping(
      "name" -> text,
      "comment" -> nonEmptyText(maxLength = 255)
    )(NewComment.apply)(NewComment.unapply)
  )

  def read(id: Long) = Action {
    implicit request =>
      val (thread,comments)= DB readOnly {
        implicit session =>
          val thread = Threads.findById(id)
          thread match {
            case Some(t) => (Some(t),Comments.findBy(t))
            case None => (None,List())
          }
      }

      thread match {
        case Some(v) => Ok(views.html.thread.page(v,comments,commentForm))
        case None => NotFound
      }
  }

  def delete(id:Long) = Action { implicit request =>
    deleteForm.bindFromRequest().fold(
      errors => Redirect(routes.Application.index()),
      id =>{
        DB localTx {implicit session=>
          Comments.delete(id)
        }
        Redirect(routes.Application.index())
      }
    )
  }

  def create(id:Long) = Action {
    implicit request =>
      commentForm.bindFromRequest().fold(
        errors => Redirect(routes.Application.index()).flashing("errors" -> "error"),
        comment => {
          DB localTx {implicit session =>
              val thread = Threads.findById(id)
              thread map {_.comment(comment)}
          } match {
            case Some(n) => Redirect(routes.PageActions.read(id))
            case None => NotFound
          }
        }
      )
  }

}
