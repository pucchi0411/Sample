package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock.Mockito

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.{JsString, Json}

@RunWith(classOf[JUnitRunner])
class ThreadActionsSpec extends Specification with Mockito {

  "newThreadForm" should {
    "入力に問題がない場合" in {
      val form = ThreadActions.newThreadForm
      val actual = form.bind(Map("name" -> "hoge","message" -> "fuga"))

      actual.get must equalTo(models.NewThread("hoge","fuga"))
      actual.hasErrors must beFalse
    }

    "nameが未入力でエラー" in {
      val form = ThreadActions.newThreadForm
      val actual = form.bind(Map("name" -> "","message" -> "fuga"))

      actual.hasErrors must beTrue
    }

    "messageが未入力でも問題なし" in {
      val form = ThreadActions.newThreadForm
      val actual = form.bind(Map("name" -> "hoge","message" -> ""))

      actual.get must equalTo(models.NewThread("hoge",""))
      actual.hasErrors must beFalse
    }
  }

  "list" should {

    "存在しない板を指定した場合,NotFound" in new WithApplication {
      val board = mock[models.Boards]
      board.findAll() returns List()

      val response = route(FakeRequest(GET, "/1/thread")).get
      status(response) must equalTo(NOT_FOUND)
    }

  }

  "create" should {

    "formエラーでトップ[/]にリダイレクトされる" in new WithApplication {
      val json = Json.obj(
        "name" -> JsString(""),
        "message" -> JsString("fuga")
      )
      val response = route(FakeRequest("POST","/1/thread/create").withJsonBody(json)).get

      redirectLocation(response) must equalTo(Some("/"))
    }

    "formから値が取得できればスレッドトップにリダイレクト[/1/thread]" in new WithApplication {
      val json = Json.obj(
        "name" -> JsString("hoge"),
        "message" -> JsString("fuga")
      )
      val response = route(FakeRequest("POST","/1/thread/create").withJsonBody(json)).get

      redirectLocation(response) must equalTo(Some("/1/thread"))
    }

  }

}
