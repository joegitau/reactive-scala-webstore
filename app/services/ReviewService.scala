package services

import models.Review

import javax.inject.Singleton

trait ReviewService extends CrudService[Review] {}

@Singleton
class ReviewServiceImpl extends ReviewService {
  def create(review: Review): Long = {
    val id = idCounter.incrementAndGet()
    review.id = Some(id)

    inMemoryDB.put(id, review)
    id
  }

  def findById(id: Long): Option[Review] = {
    inMemoryDB.get(id)
  }

  def findAll(): Option[List[Review]] = {
    if (inMemoryDB.values.toList == null || inMemoryDB.values.toList.isEmpty) None
    else Some(inMemoryDB.values.toList)
  }

  def update(id: Long, review: Review): Boolean = {
    validateId(id)
    review.id = Some(id)

    inMemoryDB.put(id, review)
    true
  }

  def delete(id: Long): Boolean = {
    validateId(id)

    inMemoryDB.remove(id)
    true
  }

  private def validateId(id: Long): Unit = {
    val entry = inMemoryDB.get(id)
    if (entry == null) throw new RuntimeException(s"Could not find product with given $id!")
  }

}
