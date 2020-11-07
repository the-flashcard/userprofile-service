package datainsider.user_profile.exception

import com.twitter.finagle.http.Status

/**
 * @author anhlt
 */
case class PhoneInvalidError(message: Option[String] = None, cause: Throwable = null)
  extends XedException(XedException.PhoneInvalid,message, cause) {
  override def getStatus: Status = Status.BadRequest
}

