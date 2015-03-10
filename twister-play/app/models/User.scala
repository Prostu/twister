package models

import java.time.Instant

case class UserId(value: Long)

case class User(
  id: UserId,
  handle: String,
  name: String,
  joined: Instant)
