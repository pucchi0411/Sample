import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "存在しないページへのアクセスでは404NotFound" in new WithApplication{
      route(FakeRequest(GET, "/hoge/huga/foo/bar")) must beNone
    }

    "「/」にアクセスするとリダイレクトされる" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(SEE_OTHER)
    }
  }
}
