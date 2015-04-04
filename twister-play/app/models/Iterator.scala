package models

case class Iterator[T](values: Seq[T], continuation: Option[Long])
