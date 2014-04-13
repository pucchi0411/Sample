package models

import scalikejdbc._
import scalikejdbc.SQLInterpolation._

case class NewComment(name:String,comment:String)
case class Comment(id:Long,name:String,comment:String)

object Comments {

  def * = (rs:WrappedResultSet) => Comment(
    id = rs.long("id"),
    name = rs.string("name"),
    comment = rs.string("comment")
  )

  def findBy(thread:Thread)(implicit session:DBSession = AutoSession) = {
    sql"select * from comments where thread_id = ${thread.id}"
      .map(*).list.apply()
  }

  def findById(id:Long)(implicit session:DBSession = AutoSession):Option[Comment] = {
    sql"select * from comments where id = ${id}"
      .map(*).single.apply()
  }

  def create(thread:Thread,comment:NewComment)(implicit session:DBSession = AutoSession):Int = {
    val name = if(comment.name.isEmpty) "nanashi" else comment.name
    val now = new java.util.Date()
    sql"""insert into comments(thread_id,name,comment,created_at)
          values(${thread.id},${name},${comment.comment},${now})"""
      .update.apply()
  }

  def findAll()(implicit session:DBSession = AutoSession):List[Comment] = {
    sql"select * from comments"
      .map(*).list.apply()
  }

  def delete(id:Long)(implicit session:DBSession = AutoSession) = {
    sql"delete from comments where id = ${id}"
      .update.apply()
  }

}
