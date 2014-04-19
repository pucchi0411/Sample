package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock.Mockito

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.{JsString, Json}
import models.{NewThread, Board, Boards, Threads}

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
      val boards = mock[Boards]
      val threads = mock[Threads]

      boards.findById(1) returns None
      val action = new ThreadActions(threads,boards)

      val response = action.list(1)(FakeRequest(GET, "/1/thread"))
      status(response) must equalTo(NOT_FOUND)
    }

    "板が存在すればOK" in new WithApplication {
      val b = mock[Board]
      val boards = mock[Boards]
      val threads = mock[Threads]

      b.name returns "hoge"
      boards.findById(1) returns Some(b)
      threads.findBy(b) returns List()
      val action = new ThreadActions(threads,boards)

      val response = action.list(1)(FakeRequest(GET, "/1/thread"))
      contentAsString(response) must contain("hoge")
      status(response) must equalTo(OK)
    }

  }

  "create" should {

    "formエラーでトップ[/]にリダイレクトされる" in new WithApplication {
      val boards = mock[Boards]
      val threads = mock[Threads]

      val action = new ThreadActions(threads,boards)

      val json = Json.obj(
        "name" -> JsString(""),
        "message" -> JsString("fuga")
      )
      val response = action.create(1)(FakeRequest("POST","/1/thread/create").withJsonBody(json))

      redirectLocation(response) must equalTo(Some("/"))
    }

    "formから値が取得できればスレッドトップにリダイレクト[/1/thread]" in new WithApplication {
      val b = mock[Board]
      val t = mock[Thread]
      val boards = mock[Boards]
      val threads = mock[Threads]

      boards.findById(1) returns Some(b)
      b.create(NewThread("hoge","fuga")) returns 1
      val action = new ThreadActions(threads,boards)
      val json = Json.obj(
        "name" -> JsString("hoge"),
        "message" -> JsString("fuga")
      )
      val response = action.create(1)(FakeRequest("POST","/1/thread/create").withJsonBody(json))

      contentAsString(response) must contain("hoge")
      redirectLocation(response) must equalTo(Some("/1/thread"))
    }

  }

}
