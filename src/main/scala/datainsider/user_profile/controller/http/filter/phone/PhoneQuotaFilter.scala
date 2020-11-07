package datainsider.user_profile.controller.http.filter.phone

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import datainsider.user_profile.controller.http.filter.parser.DataRequestContext._
import datainsider.user_profile.exception.QuotaExceedError
import datainsider.user_profile.service.CaptchaService
import datainsider.user_profile.service.verification.VerifyService
import javax.inject.Inject

/**
 * @author anhlt
 */
trait PhoneQuotaFilterRequest {

  def getPhoneForQuota(): String

  def getTokenCaptcha(): Option[String]
}

class PhoneQuotaFilter @Inject()(verifyService : VerifyService, captchaService: CaptchaService) extends SimpleFilter[Request, Response] {

  override def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    val phoneRequest: PhoneQuotaFilterRequest = request.requestData

    for {
      isExceed <- verifyService.isExceedQuota(phoneRequest.getPhoneForQuota())
      r <- isExceed match {
        case true if phoneRequest.getTokenCaptcha().isEmpty => Future.exception(QuotaExceedError(Some("quota exceed.")))
        case true => captchaService.verifyCaptcha(phoneRequest.getTokenCaptcha().get).flatMap({
          case true => service(request)
          case _ => Future.exception(QuotaExceedError(Some("Captcha invalid")))
        })
        case _ => service(request)
      }
    } yield r
  }
}