package services

import scala.collection.mutable
import java.util.concurrent.atomic.AtomicLong

trait CrudService[A] {
  var inMemoryDB = new mutable.HashMap[Long, A]
  var idCounter = new AtomicLong(0L)

  def create(a: A): Long // return Id of created item
  def findById(id: Long): Option[A] // return Optional item
  def findAll(): Option[List[A]] // return list of Optional items
  def update(id: Long, a: A): Boolean // return true / false if item updated/ not updated
  def delete(id: Long): Boolean // return true / false if item is deleted/ not deleted
}
