package models

import scalikejdbc._
import scalikejdbc.SQLInterpolation._

case class NewBoard(name:String)
case class Board(id:Long,name:String) {

  def create(thread:NewThread) = {
    Threads.create(this,thread)
  }

}

object Boards {

  val * = (rs:WrappedResultSet) => Board(
    id = rs.long("id"),
    name = rs.string("name")
  )

  def create(board:NewBoard)(implicit session:DBSession = AutoSession) = {
    val now = new java.util.Date()
    sql"insert into boards (name,created_at) values (${board.name},${now})"
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
