package models

case class TweetId(value: Long)

case class Tweet(
  id: TweetId,
  userId: UserId,
  message: String)
