package modules

import com.google.inject.AbstractModule
import services.{ImageService, ImageServiceImpl, ProductService, ProductServiceImpl, ReviewService, ReviewServiceImpl}


class CoreModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ProductService])
      .to(classOf[ProductServiceImpl]).asEagerSingleton()

    bind(classOf[ImageService])
      .to(classOf[ImageServiceImpl]).asEagerSingleton()

    bind(classOf[ReviewService])
      .to(classOf[ReviewServiceImpl]).asEagerSingleton()
  }
}
