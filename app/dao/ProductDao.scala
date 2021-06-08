package dao

import models.Product
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ProductDaoTrait extends BaseDao[Product] {
  def findAll: Future[Seq[Product]]
  def findById(id: Long): Future[Option[Product]]
  def create(p: Product): Future[Unit]
  def update(newProd: Product): Future[Int]
  def delete(id: Long): Future[Int]
}

class ProductDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] with ProductDaoTrait {
  import profile.api._

  class ProductTable(tag: Tag) extends Table[Product](tag, "Products") {
    def id: Rep[Option[Long]] = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("NAME")
    def details: Rep[String] = column[String]("DETAILS")
    def price: Rep[BigDecimal] = column[BigDecimal]("PRICE")

    // add bi-directional mapping
    def * : ProvenShape[Product] = (id, name, details, price) <> (Product.tupled, Product.unapply)
  }

  // TableQuery value represents the actual database table
  override def toTable: TableQuery[ProductTable] = TableQuery[ProductTable]

  private val Products: TableQuery[ProductTable] = toTable

  override def findAll: Future[Seq[Product]] =
    db.run(Products.result)

  override def findById(id: Long): Future[Option[Product]] =
    db.run(Products.filter( _.id === id).result.headOption)

  override def create(p: Product): Future[Unit] =
    db.run(Products += p).map { _ => () }

  override def update(newProd: Product): Future[Int] = {
    db.run(
      Products.filter(_.id === newProd.id)
        .map(p => (p.name, p.details, p.price)) .update((newProd.name, newProd.details, newProd.price))
    )
  }

  override def delete(id: Long): Future[Int] =
    db.run(Products.filter( _.id === id).delete)
}
