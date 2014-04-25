package models

import scalikejdbc._
import org.mindrot.jbcrypt.BCrypt

case class Account(id:Long,email:String,password:String,permission:Permission)

object Accounts extends Accounts
class Accounts {

  val * = (rs:WrappedResultSet) => Account(
    id         = rs.long("id"),
    email      = rs.string("email"),
    password   = rs.string("password"),
    permission = Permission.valueOf(rs.string("permission"))
  )

  def authenticate(email:String,password:String)(implicit session:DBSession = AutoSession) = {
    findByEmail(email).filter( a => BCrypt.checkpw(password,a.password))
  }

  def findByEmail(email:String)(implicit session:DBSession = AutoSession):Option[Account] = {
    sql"select * from accounts where email = ${email}"
      .map(*).single.apply()
  }

}
