package models

import org.specs2.mutable._
import play.api.test.WithApplication

class ThreadsSpec extends Specification {

  sequential

  "create" should {
    "1件挿入できる" in new WithApplication{
      val newBoard = NewBoard("hoge","huga")
      val board = Boards.create(newBoard)
      val newThread = NewThread("name","message")
      val actual = Threads.create(Board(1,"hoge","huga"),newThread)

      actual must equalTo(1)
    }
  }

  "findById" should {
    "挿入したデータを検索できる" in new WithApplication {
      val newBoard = NewBoard("hoge","huga")
      val board = Boards.create(newBoard)
      val newThread = NewThread("name","message")
      Threads.create(Board(1,"hoge","huga"),newThread)

      val actual = Threads.findById(1)

      actual must equalTo(Some(Thread(1,1,"name","message")))

    }
  }

  "findAll" should {

    "データが空の場合空のリストが返る" in new WithApplication {
      val actual = Threads.findAll()

      actual must equalTo(List())
    }

    "挿入したデータを全件取得できる" in new WithApplication {
      Boards.create(NewBoard("hoge","fuga"))
      Threads.create(Board(1,"hoge","huga"),NewThread("foo","bar"))
      Threads.create(Board(1,"hoge","huga"),NewThread("foo","bar"))

      val actual = Threads.findAll()

      actual must equalTo(List(Thread(1,1,"foo","bar"),Thread(2,1,"foo","bar")))
    }
  }

  "findByBoard" should {
    "データが空の場合空のリストが返る" in new WithApplication {
      val actual = Threads.findBy(Board(1,"hoge","fuga"))

      actual must equalTo(List())
    }

    "同じボードのスレッドのリストが返る" in new WithApplication {
      Boards.create(NewBoard("hoge","fuga"))
      Threads.create(Board(1,"hoge","huga"),NewThread("foo","bar"))
      Threads.create(Board(1,"hoge","huga"),NewThread("foo","bar"))
      Boards.create(NewBoard("hoge","fuga"))
      Threads.create(Board(2,"hoge","huga"),NewThread("foo","bar"))
      val actual = Threads.findBy(Board(1,"hoge","fuga"))

      actual must equalTo(List(Thread(1,1,"foo","bar"),Thread(2,1,"foo","bar")))
    }
  }

}
