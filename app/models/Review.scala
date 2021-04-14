package models

case class Review(
                   var id: Option[Long],
                   productId: Option[Long],
                   author: String,
                   comment: String
                  ) {
  override def toString: String =
    s"Review ID: ${id.getOrElse(0)}, productId: $productId, author: $author and comment: $comment"
}
