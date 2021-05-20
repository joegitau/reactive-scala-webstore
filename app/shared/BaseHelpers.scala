package shared

import views.html
import views.html.helper.FieldConstructor

object BaseHelpers {
  implicit val customInputField: FieldConstructor = FieldConstructor(html.helpers.customInput.f)
}
