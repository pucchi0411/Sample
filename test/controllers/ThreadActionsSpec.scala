package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock.Mockito

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ThreadActionsSpec extends Specification with Mockito {

  "list" should {

    "存在しない板を指定した場合,NotFound" in new WithApplication {
      val board = mock[models.Boards]
      board.findAll() returns List()

      val response = route(FakeRequest(GET, "/1/thread")).get
      status(response) must equalTo(NOT_FOUND)
    }

  }

}
