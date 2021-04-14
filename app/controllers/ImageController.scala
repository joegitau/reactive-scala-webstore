package controllers

import models.Image
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import services.{ImageService, ProductService}

import javax.inject.{Inject, Singleton}

@Singleton
class ImageController @Inject()(messagesApi: MessagesApi,
                                imageService: ImageService,
                                productService: ProductService,
                                val controllerComponents: ControllerComponents
                               ) extends BaseController with I18nSupport {

  val imageForm: Form[Image] = Form(
    mapping(
      "id" -> optional(longNumber),
      "productId" -> optional(longNumber),
      "url" -> text
    )(Image.apply)(Image.unapply)
  )

  def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val images = imageService
      .findAll()
      .getOrElse(Seq())

    // Logger.info(s"Images: $images ")
    Ok(views.html.images.image_index(images))
  }

  def blank: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    // Logger.info("blank called. ")
    Ok(views.html.images.image_details(None, imageForm, productService.findAllProducts()))
  }

  def details(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val image = imageService
      .findById(id).get

    Ok(
      views.html.images.image_details(Some(id), imageForm.fill(image), productService.findAllProducts())
    )
  }

  def create: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    // Logger.info("insert called.")

    imageForm
      .bindFromRequest()
      .fold(
        errorForm => {
          BadRequest(views.html.images.image_details(None, errorForm, productService.findAllProducts()))
        },
        image => {
          if (image.productId == null || image.productId.getOrElse(0) == 0) {
            Redirect(routes.ImageController.blank()).
              flashing("error" -> "Product ID Cannot be Null!")
          } else {
            if (image.url == null || "".equals(image.url))
              image.url = "/assets/images/default_product.png"

            val id = imageService.create(image)

            Redirect(routes.ImageController.index())
              .flashing("success" -> Messages("success.create", id))
          }
        }
      )
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    // Logger.info("updated called. id: " + id)

    imageForm
      .bindFromRequest()
      .fold(
        errorForm => {
          Ok(views.html.images.image_details(Some(id), errorForm, null))
            .flashing("error" -> "Fix the errors!")
        },
        image => {
          imageService.update(id, image)

          Redirect(routes.ImageController.index())
            .flashing("success" -> Messages("success.update", image.id))
        }
      )
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    imageService
      .findById(id)
      .map { image =>
        imageService.delete(id)
        Redirect(routes.ImageController.index())
          .flashing("success" -> Messages("success.delete", image.id))
      }
      .getOrElse(NotFound)
  }

}
