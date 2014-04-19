package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock.Mockito

import play.api.test._
import play.api.test.Helpers._
import play.api.mvc.AnyContentAsFormUrlEncoded
import play.api.libs.json._

@RunWith(classOf[JUnitRunner])
class BoardActionsSpec extends Specification with Mockito {

  "form" should {

    "入力が問題ない場合" in {
      val form = BoardActions.newBoardForm
      val actual = form.bind(Map("name" -> "hoge","message" -> "fuga"))

      actual.hasErrors must beFalse
      actual.get must equalTo(models.NewBoard("hoge","fuga"))
    }

    "nameが未入力でエラー" in {
      val form = BoardActions.newBoardForm
      val actual = form.bind(Map("name" -> "","message" -> "fuga"))

      actual.hasErrors must beTrue
    }

    "messageが未入力でも問題なし" in {
      val form = BoardActions.newBoardForm
      val actual = form.bind(Map("name" -> "hoge","message" -> ""))


      actual.hasErrors must beFalse
      actual.get must equalTo(models.NewBoard("hoge",""))
    }

  }

  "list" should {

    "板が一枚もなくとも表示できる" in new WithApplication {
      val board = mock[models.Boards]
      board.findAll() returns List()

      val action = new BoardActions(board)

      val response = route(FakeRequest(GET, "/board")).get
      status(response) must equalTo(OK)
    }

    "板が一枚でもあればその情報を表示できる" in new WithApplication {
      val b = mock[models.Board]
      val boards = mock[models.Boards]
      b.id returns 1
      b.name returns "hoge"
      boards.findAll() returns List(b)

      val action = new BoardActions(boards)

      val response = action.list(FakeRequest(GET, "/board"))

      contentAsString(response) must contain("hoge")
      status(response) must equalTo(OK)
    }

  }

  "create" should {
    "formエラーでトップ[/]にリダイレクトされる" in new WithApplication {
      val boards = mock[models.Boards]
      val action = new BoardActions(boards)

      val json = Json.obj(
        "name" -> JsString(""),
        "message" -> JsString("fuga")
      )
      val response = action.create()(FakeRequest("POST","/board/create").withJsonBody(json))

      redirectLocation(response) must equalTo(Some("/"))
    }

    "formから値が取得できれば[/board]にリダイレクト" in new WithApplication {
      val b = mock[models.Board]
      val boards = mock[models.Boards]
      b.id returns 1
      b.name returns "hoge"
      boards.findAll() returns List(b)

      val action = new BoardActions(boards)
      val json = Json.obj(
        "name" -> JsString("hoge"),
        "message" -> JsString("fuga")
      )
      val response = action.create()(FakeRequest("POST","/board/create").withJsonBody(json))

      there was one(boards).create(models.NewBoard("hoge","fuga"))
      redirectLocation(response) must equalTo(Some("/board"))
    }
  }

}
