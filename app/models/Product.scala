package models

case class Product(
                    var id: Option[Long],
                    name: String,
                    details: String,
                    price: BigDecimal
                   ) {
   override def toString: String =
    s"Product ID: ${id.getOrElse(0)}, name: $name, details: $details and price: $price"
}
