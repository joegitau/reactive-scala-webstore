package controllers

import models.Review
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import services.{ProductService, ReviewService}

import javax.inject.{Inject, Singleton}

@Singleton
class ReviewController @Inject()(reviewService: ReviewService,
                                 productService: ProductService,
                                 val controllerComponents: ControllerComponents
                                ) extends BaseController with I18nSupport {
  val reviewForm: Form[Review] = Form(
    mapping(
      "id" -> optional(longNumber),
      "productId" -> optional(longNumber),
      "author" -> nonEmptyText,
      "comment" -> nonEmptyText
    )(Review.apply)(Review.unapply)
  )

  def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val reviews = reviewService
      .findAll()
      .getOrElse(Seq())

    Ok(views.html.reviews.review_index(reviews))
  }

  def blank: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.reviews.review_details(None, reviewForm, productService.findAllProducts()))
  }

  def details(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val review = reviewService.findById(id).get

    Ok(views.html.reviews.review_details(Some(id), reviewForm.fill(review), productService.findAllProducts()))
  }

  def create: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    reviewForm
      .bindFromRequest()
      .fold(
        errorForm => {
          BadRequest(views.html.reviews.review_details(None, errorForm, productService.findAllProducts()))
            .flashing("error" -> "Fix the errors within form!")
        },
        review => {
          if (review.productId == null || review.productId.getOrElse(0) == 0) {
            Redirect(routes.ReviewController.blank())
              .flashing("error" -> "Product Id cannot be null!")
          } else {
            val id = reviewService.create(review)

            Redirect(routes.ReviewController.index())
              .flashing("success" -> Messages("success.create", id))
          }
        }
      )
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    reviewForm
      .bindFromRequest()
      .fold(
        errorForm => {
          BadRequest(views.html.reviews.review_details(Some(id), errorForm, productService.findAllProducts()))
            .flashing("error" -> "Fix the errors within form!")
        },
        review => {
          reviewService.update(id, review)

          Redirect(routes.ReviewController.index())
            .flashing("success" -> Messages("success.update", review.productId))
        }
      )
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    reviewService
      .findById(id)
      .map { review =>
        Redirect(routes.ReviewController.index())
          .flashing("success" -> Messages("success.delete", review.productId))
      }
      .getOrElse(NotFound)
  }

}
