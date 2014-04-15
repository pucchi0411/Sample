package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock.Mockito

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class BoardActionsSpec extends Specification with Mockito {

  "list" should {

    "板が一枚もなくとも表示できる" in new WithApplication {
      val board = mock[models.Boards]
      board.findAll() returns List()

      val response = route(FakeRequest(GET, "/board")).get
      status(response) must equalTo(OK)
    }

  }

}
