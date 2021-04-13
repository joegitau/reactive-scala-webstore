package services

import models.Product

trait ProductService extends CrudService[Product] {
  def findAllProducts(): Seq[(String,String)]
}

@Singleton
class ProductServiceImpl extends ProductService {
  def create(product: Product): Long = {
    // increment id
    val id: Long = idCounter.incrementAndGet()

    product.id = Some(id)
    // associate id with the specified key within hashMap.
    inMemoryDB.put(id, product)

    id
  }

  def findById(id: Long): Option[Product] = {
    validateId(id)
    inMemoryDB.get(id)
  }

  def findAll(): Option[List[Product]] = {
    if (inMemoryDB.values == Nil || inMemoryDB.values.toList.isEmpty) return None
    else Some(inMemoryDB.values.toList)
  }

  def update(id: Long, product: Product): Boolean = {
    // assert that product exists
    validateId(id)

    product.id = Some(id)
    inMemoryDB.put(id, product)

    true
  }

  def delete(id: Long): Boolean = {
    // assert that product exists
    validateId(id)
    inMemoryDB.remove(id)

    true
  }

  // will be required by review & image services
  def findAllProducts(): Seq[(String, String)] = {
    val products = this
      .findAll()
      .getOrElse(List(Product(id = Some(0), name = "", details = "", price = 0)))
      .map { product =>
        (product.id.get.toString, product.name) // map list to seq of tuples required by selectBox checkbox in view (UI)
      }

    products
  }

  private def validateId(id: Long): Unit = {
    val entry = inMemoryDB.get(id)

    if (entry == null) throw new RuntimeException(s"Could not find product with given $id!")
  }
}
