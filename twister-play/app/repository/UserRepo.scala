package repository

import java.sql.{PreparedStatement, SQLException}

import models.{User, UserId}
import play.api.db.DB
import play.api.Play.current

object UserRepo {
  val insertStatement = """INSERT INTO "user" (handle, name) VALUES (?, ?)"""
  val getStatement = """SELECT id, handle, name, joined FROM "user" WHERE id = ?"""
}

class UserRepo {
  import UserRepo._

  def insert(handle: String, name: String): Unit = DB.withConnection { conn =>
    try {
      val stmt: PreparedStatement = conn.prepareStatement(insertStatement)
      stmt.setString(1, handle)
      stmt.setString(2, name)
      if (stmt.executeUpdate() == 0) {
        throw new RepoException("Could not insert user in database")
      }
    } catch { case e: SQLException =>
      throw new RepoException("Failed talking to database when inserting", e)
    }
  }

  def get(userId: UserId): User = DB.withConnection { conn =>
    try {
      val statement: PreparedStatement = conn.prepareStatement(getStatement)
      statement.setLong(1, userId.value)
      val resultSet = statement.executeQuery()
      resultSet.next()
      User(
        id = UserId(resultSet.getLong(1)),
        handle = resultSet.getString(2),
        name = resultSet.getString(3),
        joined = resultSet.getTimestamp(4).toInstant)
    } catch { case e: SQLException =>
      throw new RepoException("Failed talking to database when fetching", e)
    }
  }
}
