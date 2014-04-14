package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.{Boards, Board, NewBoard}
import scalikejdbc._

object BoardActions extends Controller {

  val newBoardForm = Form(
    mapping(
      "name" -> nonEmptyText()
    )(NewBoard.apply)(NewBoard.unapply)
  )

  def list = Action {
    implicit request =>
      val boards = Boards.findAll()
      Ok(views.html.board.list(boards, newBoardForm))
  }

  def create = Action {
    implicit request =>
      newBoardForm.bindFromRequest.fold(
        errors => Redirect(routes.Application.index()),
        newBoard => {
          Boards.create(newBoard)
          Redirect(routes.BoardActions.list())
        }
      )
  }


}
