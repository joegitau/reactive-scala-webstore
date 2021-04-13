package models

case class Image(
                var id: Option[Long],
                var productId: Option[Long],
                var url: String
                )
