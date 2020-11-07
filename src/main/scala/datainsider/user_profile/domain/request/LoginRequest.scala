package datainsider.user_profile.domain.request

import datainsider.user_profile.controller.http.filter.email.ConfirmEmailFilterRequest
import datainsider.user_profile.controller.http.filter.user.UsernameFilterRequest
import datainsider.user_profile.util.Utils

/**
 * @author anhlt
 */
case class LoginBodyRequest(username: String, password: String, remember: Boolean)

case class LoginByUserPassRequest(username: String, password: String, remember: Boolean) extends UsernameFilterRequest with ConfirmEmailFilterRequest {
  override def getUsername(): String = username
}

case class LoginByEmailRequest(email: String, password: String, remember: Boolean)

case class LoginByPhoneRequest(var phoneNumber: String, password: String, remember: Boolean) {
  phoneNumber = Utils.normalizePhoneNumber(phoneNumber)
}