package models

import org.specs2.mutable._
import play.api.test.WithApplication

class BoardsSpec extends Specification {

  "create" should {
    "1件挿入できる" in new WithApplication{
      val newBoard = NewBoard("name","message")
      val actual = Boards.create(newBoard)

      actual must equalTo(1)
    }

  }

  "findById" should {
    "挿入したデータを検索できる" in new WithApplication {
      val newBoard = NewBoard("name","message")
      Boards.create(newBoard)

      val actual = Boards.findById(1)

      actual must equalTo(Some(Board(1,"name","message")))

    }
  }

  "findAll" should {

    "データが空の場合空のリストが返る" in new WithApplication {
      val actual = Boards.findAll()

      actual must equalTo(List())
    }

    "挿入したデータを全件取得できる" in new WithApplication {
      Boards.create(NewBoard("hoge","fuga"))
      Boards.create(NewBoard("foo","bar"))

      val actual = Boards.findAll()

      actual must equalTo(List(Board(1,"hoge","fuga"),Board(2,"foo","bar")))
    }
  }

}
