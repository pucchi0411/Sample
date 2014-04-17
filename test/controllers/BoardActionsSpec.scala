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

      val response = route(FakeRequest(GET, "/board")).get
      status(response) must equalTo(OK)
    }

  }

  "create" should {
    "formエラーでトップ[/]にリダイレクトされる" in new WithApplication {
      val json = Json.obj(
        "name" -> JsString(""),
        "message" -> JsString("fuga")
      )
      val response = route(FakeRequest("POST","/board/create").withJsonBody(json)).get

      status(response) must equalTo(SEE_OTHER)
      redirectLocation(response) must equalTo(Some("/"))
    }

    "formから値が取得できれば[/board]にリダイレクト" in new WithApplication {
      val json = Json.obj(
        "name" -> JsString("hoge"),
        "message" -> JsString("fuga")
      )
      val response = route(FakeRequest("POST","/board/create").withJsonBody(json)).get

      status(response) must equalTo(SEE_OTHER)
      redirectLocation(response) must equalTo(Some("/board"))
    }
  }

}
