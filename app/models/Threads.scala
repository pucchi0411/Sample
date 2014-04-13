package models

import scalikejdbc._
import scalikejdbc.SQLInterpolation._

case class NewThread(name:String)
case class Thread(id:Long,name:String) {

  def comment(newComment:NewComment) = {
    Comments.create(this,newComment)
  }
}

object Threads {

  val * = (rs:WrappedResultSet) => Thread(
    id = rs.long("id"),
    name = rs.string("name")
  )

  def findById(id:Long)(implicit session:DBSession = AutoSession) = {
    sql"select * from threads where id = ${id}"
      .map(*).single.apply()
  }

  def findAll()(implicit session:DBSession = AutoSession) = {
    sql"select * from threads"
      .map(*).list.apply()
  }

  def create(newThread:NewThread)(implicit session:DBSession = AutoSession) = {
    sql"insert into threads(name) values (${newThread.name})"
      .update.apply()
  }

}
