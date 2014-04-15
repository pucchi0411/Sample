package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock.Mockito

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class PageActionsSpec extends Specification with Mockito {

  "read" should {

    "存在しないスレッドを指定すると,NotFound" in new WithApplication {
      val threads = mock[models.Threads]
      threads.findAll() returns List()

      val response = route(FakeRequest(GET, "/1")).get
      status(response) must equalTo(NOT_FOUND)
    }

  }

}
