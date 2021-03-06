package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.{Boards, Board, NewBoard}
import scalikejdbc._

object BoardActions extends BoardActions(Boards)
class BoardActions(Boards:Boards) extends Controller {

  val newBoardForm = Form(
    mapping(
      "name" -> nonEmptyText(),
      "message" -> text(maxLength = 255)
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
        errors => Redirect(routes.BoardActions.list()),
        newBoard => {
          Boards.create(newBoard)
          Redirect(routes.BoardActions.list())
        }
      )
  }


}
