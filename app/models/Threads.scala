package models

import scalikejdbc._
import scalikejdbc.SQLInterpolation._

case class NewThread(name:String,message:String)
case class Thread(id:Long,boardId:Long,name:String,message:String) {

  def comment(newComment:NewComment) = {
    Comments.create(this,newComment)
  }
}

object Threads {

  val * = (rs:WrappedResultSet) => Thread(
    id = rs.long("id"),
    boardId = rs.long("board_id"),
    name = rs.string("name"),
    message = rs.string("message")
  )

  def findById(id:Long)(implicit session:DBSession = AutoSession) = {
    sql"select * from threads where id = ${id}"
      .map(*).single.apply()
  }

  def findBy(board:Board)(implicit session:DBSession = AutoSession) = {
    sql"select * from threads where board_id = ${board.id}"
      .map(*).list.apply()
  }

  def findAll()(implicit session:DBSession = AutoSession) = {
    sql"select * from threads"
      .map(*).list.apply()
  }

  def create(board:Board,newThread:NewThread)(implicit session:DBSession = AutoSession) = {
    val now = new java.util.Date()
    sql"""insert into threads(name,message,board_id,created_at)
          values (${newThread.name},${newThread.message},${board.id},${now})"""
      .update.apply()
  }

}
