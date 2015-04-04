package repository

import java.sql.{ResultSet, PreparedStatement, SQLException}

import models.{Iterator, User, UserId}
import play.api.db.DB
import play.api.Play.current

import scala.collection.mutable.ListBuffer

object UserRepo {
  object Statement {
    val insert = """INSERT INTO "user" (handle, name) VALUES (?, ?)"""
    val query = """SELECT id, handle, name, joined FROM "user""""

    val get = s"""$query WHERE id = ?"""
    val iterateWithContinuation = s"""$query WHERE id > ? LIMIT ?"""
    val IterateWithoutContinuation = s"""$query LIMIT ?"""
  }
  val DefaultBatchSize = 50

  def resultSetToUser(rs: ResultSet): User = User(
    id = UserId(rs.getLong(1)),
    handle = rs.getString(2),
    name = rs.getString(3),
    joined = rs.getTimestamp(4).toInstant)
}

class UserRepo {
  import UserRepo._

  def insert(handle: String, name: String): Unit = DB.withConnection { conn =>
    try {
      val statement: PreparedStatement = conn.prepareStatement(Statement.insert)
      statement.setString(1, handle)
      statement.setString(2, name)
      if (statement.executeUpdate() == 0) {
        throw new RepoException("Could not insert user in database")
      }
    } catch { case e: SQLException =>
      throw new RepoException("Failed talking to database when inserting", e)
    }
  }

  def get(userId: UserId): User = DB.withConnection { conn =>
    try {
      val statement: PreparedStatement = conn.prepareStatement(Statement.get)
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

  def iterate(continuation: Option[Long], batchSize: Option[Long] = None): Iterator[User] = DB.withConnection { conn =>
    // We don't implement a real iterator so this entire signature is probably misleading
    try {
      val batchSizeValue: Long = batchSize.getOrElse(DefaultBatchSize)
      val statement: PreparedStatement = continuation match {
        case Some(cValue) =>
          val statement = conn.prepareStatement(Statement.iterateWithContinuation)
          statement.setLong(1, continuation.getOrElse (0L) )
          statement.setLong(2, batchSizeValue)
          statement
        case None =>
          val statement = conn.prepareStatement(Statement.IterateWithoutContinuation)
          statement.setLong(1, batchSizeValue)
          statement
      }

      val resultSet = statement.executeQuery()
      val users = ListBuffer[User]()
      while (resultSet.next()) {
        users.append(resultSetToUser(resultSet))
      }

      val nextContinuation = if (users.size < batchSizeValue) None else Some(users.last.id.value)
      Iterator(users.toList, nextContinuation)
    } catch { case e: SQLException =>
      throw new RepoException("Failed talking to database when iterating", e)
    }
  }
}
