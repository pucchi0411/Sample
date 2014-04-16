package models

import scalikejdbc._
import scalikejdbc.SQLInterpolation._

case class NewBoard(name:String,message:String)
case class Board(id:Long,name:String,message:String) {

  def create(thread:NewThread) = {
    Threads.create(this,thread)
  }

}

object Boards extends Boards
class Boards {

  val * = (rs:WrappedResultSet) => Board(
    id = rs.long("id"),
    name = rs.string("name"),
    message= rs.string("message")
  )

  def create(board:NewBoard)(implicit session:DBSession = AutoSession) = {
    val now = new java.util.Date()
    sql"insert into boards (name,message,created_at) values (${board.name},${board.message},${now})"
      .update.apply()
  }

  def findById(id:Long)(implicit session:DBSession = AutoSession):Option[Board] = {
    sql"select * from boards where id = ${id}"
      .map(*).single.apply()
  }

  def findAll()(implicit session:DBSession = AutoSession):List[Board] = {
    sql"select * from boards"
      .map(*).list.apply()
  }

}
