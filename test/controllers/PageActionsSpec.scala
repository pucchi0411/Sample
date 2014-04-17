package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.{JsString, Json}
import models._

class PageActionsSpec extends Specification with Mockito {

  sequential

  "deleteForm" should {
    "入力に問題がなければエラー無し" in {
      val form = PageActions.deleteForm
      val actual = form.bind(Map("id" -> "1"))

      actual.get must equalTo(1)
      actual.hasErrors must beFalse
    }

    "未入力はエラー" in {
      val form = PageActions.deleteForm
      val actual = form.bind(Map("id" -> ""))

      actual.hasErrors must beTrue
    }

  }

  "commentForm" should {
    "入力に問題がなければエラー無し" in {
      val form = PageActions.commentForm
      val actual = form.bind(Map("name" -> "hoge", "comment" -> "fuga"))

      actual.get must equalTo(models.NewComment("hoge", "fuga"))
      actual.hasErrors must beFalse
    }

    "comment未入力はエラー" in {
      val form = PageActions.commentForm
      val actual = form.bind(Map("name" -> "hoge", "comment" -> ""))

      actual.hasErrors must beTrue
    }

    "name未入力は問題なし" in {
      val form = PageActions.commentForm
      val actual = form.bind(Map("name" -> "", "comment" -> "fuga"))

      actual.errors must equalTo(List())
      actual.get must equalTo(models.NewComment("", "fuga"))
      actual.hasErrors must beFalse
    }
  }

  "read" should {

    "存在しないスレッドを指定すると,NotFound" in new WithApplication {
      val response = route(FakeRequest(GET, "/1")).get
      status(response) must equalTo(NOT_FOUND)
    }

    "スレッドが見つかればOK" in new WithApplication {
      Boards.create(NewBoard("foo","bar"))
      Threads.create(Board(1,"foo","bar"),NewThread("hoge","huga"))

      val response = route(FakeRequest(GET, "/1")).get

      status(response) must equalTo(OK)
    }


  }

  "create" should {
    "formエラーでトップ[/]にリダイレクトされる" in new WithApplication {
      val json = Json.obj(
        "name" -> JsString("hoge"),
        "comment" -> JsString("")
      )
      val response = route(FakeRequest("POST", "/1/create").withJsonBody(json)).get

      redirectLocation(response) must equalTo(Some("/"))
    }

    "formから値が取得できたが,スレッドが存在しない場合NotFound" in new WithApplication {
      val json = Json.obj(
        "name" -> JsString(""),
        "comment" -> JsString("fuga")
      )
      val response = route(FakeRequest("POST", "/1/create").withJsonBody(json)).get

      status(response) must equalTo(NOT_FOUND)
    }


    "formから値が取得でき,書き込めれば[/1]へリダイレクト" in new WithApplication {
      Boards.create(NewBoard("foo","bar"))
      Threads.create(Board(1,"foo","bar"),NewThread("hoge","huga"))

      val json = Json.obj(
        "name" -> JsString(""),
        "comment" -> JsString("fuga")
      )
      val response = route(FakeRequest("POST", "/1/create").withJsonBody(json)).get

      redirectLocation(response) must equalTo(Some("/1"))
    }
  }

}
