package controllers

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{BaseController, ControllerComponents}
import services.ReviewService

import javax.inject.Inject

class ReviewController @Inject()(messagesApi: MessagesApi,
                                 reviewService: ReviewService,
                                 val controllerComponents: ControllerComponents
                                ) extends BaseController with I18nSupport{

}
