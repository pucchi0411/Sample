package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock.Mockito

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class PageActionsSpec extends Specification with Mockito {

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
      val actual = form.bind(Map("name" -> "hoge","comment" -> "fuga"))

      actual.get must equalTo(models.NewComment("hoge","fuga"))
      actual.hasErrors must beFalse
    }

    "comment未入力はエラー" in {
      val form = PageActions.commentForm
      val actual = form.bind(Map("name" -> "hoge","comment" -> ""))

      actual.hasErrors must beTrue
    }

    "name未入力は問題なし" in {
      val form = PageActions.commentForm
      val actual = form.bind(Map("name" -> "","comment" -> "fuga"))

      actual.errors must equalTo(List())
      actual.get must equalTo(models.NewComment("","fuga"))
      actual.hasErrors must beFalse
    }
  }

  "read" should {

    "存在しないスレッドを指定すると,NotFound" in new WithApplication {
      val threads = mock[models.Threads]
      threads.findAll() returns List()

      val response = route(FakeRequest(GET, "/1")).get
      status(response) must equalTo(NOT_FOUND)
    }

  }

}
