package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "HomeController GET" should {
    "get all employees in TGT" in {
      val controller = app.injector.instanceOf(classOf[HomeController])
      val req = controller.getEmployee("TGT").apply(FakeRequest(GET, "/Employee/:company"))
      status(req) mustBe OK
      contentType(req) mustBe Some("application/json")
    }
    "did not find Exception for get all Employees for Samsung" in {
      val controller = app.injector.instanceOf(classOf[HomeController])
      val home = controller.getEmployee("Samsung").apply(FakeRequest(GET, "/Employee/:company"))

      status(home) mustBe NOT_FOUND
      contentType(home) mustBe Some("text/plain")
    }
  }
}
