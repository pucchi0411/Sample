package models

import org.specs2.mutable._
import play.api.test.WithApplication

class CommentsSpec  extends Specification {

  sequential

  "create" should {
    "1件挿入できる" in new WithApplication{
      Boards.create(NewBoard("hoge","huga"))
      Threads.create(Board(1,"hoge","huga"),NewThread("name","message"))
      val actual = Comments.create(Thread(1,1,"hoge","huga"),NewComment("foo","bar"))

      actual must equalTo(1)
    }
  }

  "findById" should {
    "存在しないデータではNoneが返る" in new WithApplication {
      val actual = Comments.findById(1)

      actual must equalTo(None)
    }

    "挿入したデータを検索できる" in new WithApplication {
      Boards.create(NewBoard("hoge","huga"))
      Threads.create(Board(1,"hoge","huga"),NewThread("name","message"))
      Comments.create(Thread(1,1,"hoge","huga"),NewComment("foo","bar"))

      val actual = Comments.findById(1)

      actual must equalTo(Some(Comment(1,"foo","bar")))
    }
  }

  "findAll" should {

    "データが空の場合空のリストが返る" in new WithApplication {
      val actual = Comments.findAll()

      actual must equalTo(List())
    }

    "挿入したデータを全件取得できる" in new WithApplication {
      Boards.create(NewBoard("hoge","huga"))
      Threads.create(Board(1,"hoge","huga"),NewThread("name","message"))
      Comments.create(Thread(1,1,"hoge","huga"),NewComment("foo","bar"))
      Comments.create(Thread(1,1,"hoge","huga"),NewComment("foo","bar"))

      val actual = Comments.findAll()

      actual must equalTo(List(Comment(1,"foo","bar"),Comment(2,"foo","bar")))
    }
  }

  "findByThread" should {
    "データが空の場合空のリストが返る" in new WithApplication {
      val actual = Comments.findBy(Thread(1,1,"hoge","fuga"))

      actual must equalTo(List())
    }

    "同じボードのスレッドのリストが返る" in new WithApplication {
      Boards.create(NewBoard("hoge","huga"))
      Threads.create(Board(1,"hoge","huga"),NewThread("name","message"))
      Comments.create(Thread(1,1,"hoge","huga"),NewComment("foo","bar"))
      Comments.create(Thread(1,1,"hoge","huga"),NewComment("foo","bar"))
      Boards.create(NewBoard("hoge","huga"))
      Threads.create(Board(2,"hoge","huga"),NewThread("name","message"))
      Comments.create(Thread(2,2,"hoge","huga"),NewComment("foo","bar"))

      val actual = Comments.findBy(Thread(1,1,"hoge","fuga"))

      actual must equalTo(List(Comment(1,"foo","bar"),Comment(2,"foo","bar")))
    }
  }
}
