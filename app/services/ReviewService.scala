package services

import models.Review

trait ReviewService extends CrudService[Review] {

}

class ReviewServiceImpl extends ReviewService {
  override def create(a: Review): Long = ???

  override def findById(id: Long): Option[Review] = ???

  override def findAll(): Option[List[Review]] = ???

  override def update(id: Long, a: Review): Boolean = ???

  override def delete(id: Long): Boolean = ???
}
