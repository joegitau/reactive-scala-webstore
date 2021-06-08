package dao

import slick.lifted.TableQuery

import scala.concurrent.Future

trait BaseDao[T] {
  def toTable: TableQuery[_] // Return the table FRM mapping for that dao (Functional Relational Mapper)
  def findAll: Future[Seq[T]] // Find all data for a specific table
  def findById(id: Long): Future[Option[T]] // Get a specific record in a table, filtered by ID
  def create(o: T): Future[Unit] // Add new record to a table
  def update(o: T): Future[Int] // Update data in a table
  def delete(id: Long): Future[Int] // Delete an item in a table by ID
}
