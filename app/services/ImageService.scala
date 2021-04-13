package services

import models.Image

trait ImageService extends CrudService[Image] {

}

class ImageServiceImp extends ImageService {
  override def create(a: Image): Long = ???

  override def findById(id: Long): Option[Image] = ???

  override def findAll(): Option[List[Image]] = ???

  override def update(id: Long, a: Image): Boolean = ???

  override def delete(id: Long): Boolean = ???
}
