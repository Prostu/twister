package repository

import models.{TweetId, Tweet}

abstract class TweetRepo {
  def insert(tweet: Tweet): Unit
  def get(tweetId: TweetId): Tweet
}
