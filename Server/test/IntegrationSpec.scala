import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {

  "Application" should {

    "work from within a browser" in /*new WithBrowser(classOf[ChromeDriver])*/ {
      success
//      browser.goTo("http://localhost:" + port + "/login")
//      browser.fill("#username").`with`("test@test.com")
//      browser.fill("#password").`with`("123456")
//      browser.submit( "#id")
//      browser.pageSource must contain("index")
    }
  }
}
