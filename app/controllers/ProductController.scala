package controllers

import models.Product
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import services.ProductService

import javax.inject.{Inject, Singleton}

@Singleton
class ProductController @Inject() (val controllerComponents: ControllerComponents,
                                   messagesApi: MessagesApi,
                                   productService: ProductService,
                                  ) extends BaseController with I18nSupport {
  // val logger: Logger = Logger(getClass)

  val productForm: Form[Product] = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText,
      "details" -> text,
      "price" -> bigDecimal
    )(Product.apply)(Product.unapply)
  )

  def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val products = productService
      .findAll()
      .getOrElse(Seq())

    // Logger.debug(s"Products: $products")
    Ok(views.html.product_index())
  }

  def blank: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.product_details(None, productForm))
  }

  def details(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    // Logger.debug(s"Product Id: $id")

    val product = productService.findById(id).get

    // redirect to view + bind data with the form so that UI loads with all HTML inputs pre-filled with data
    Ok(
      views.html.product_details(Some(id)),
      productForm.fill(product)
    )
  }

  def create: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    // Logger.debug("create method initialised...")

    // fold takes two arguments (functions), Error & Ok
    productForm
      .bindFromRequest
      .fold(
        // binding failure, you retrieve the form containing errors:
        form => BadRequest(views.html.product_details(None, form)),
        // binding success, you pass along actual value.
        product => {
          val id = productService.create(product)

          Redirect(routes.ProductController.index())
            .flashing("success" -> Messages("success.create", id))
        }
      )
  }


  def update(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    // Logger.info(s"Updating product with id: $id")

    productForm
      .bindFromRequest
      .fold(
        form => {
          Ok(views.html.product_details(Some(id), form))
            .flashing("error" -> "Fix the errors!")
        },
        product => {
          productService.update(id,product)
          Redirect(routes.ProductController.index())
            .flashing("success" -> Messages("success.update", product.name))
        }
      )
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent]  =>
    productService
      .findById(id)
      .map { product =>
        productService.delete(id)
        Redirect(routes.ProductController.index())
          .flashing("success" -> Messages("success.delete", product.name))
      }
      .getOrElse(NotFound)
  }

}
