package services

import models.Image

import javax.inject.Singleton

trait ImageService extends CrudService[Image] {}

@Singleton
class ImageServiceImpl extends ImageService {
  def create(image: Image): Long = {
    val id = idCounter.incrementAndGet()
    image.id = Some(id)

    inMemoryDB.put(id, image)
    id
  }

  def findById(id: Long): Option[Image] = {
    inMemoryDB.get(id)
  }

  def findAll(): Option[List[Image]] = {
    if (inMemoryDB.values.toList == null || inMemoryDB.values.toList.isEmpty) None
    else Some(inMemoryDB.values.toList)
  }

  def update(id: Long, image: Image): Boolean = {
    validateId(id)
    image.id = Some(id)

    inMemoryDB.put(id, image)
    true
  }

  def delete(id: Long): Boolean = {
    validateId(id)

    inMemoryDB.remove(id)
    true
  }

  private def validateId(id: Long): Unit = {
    val entry = inMemoryDB.get(id)
    if (entry == null) throw new RuntimeException(s"Could not find image with given $id!")
  }
}
