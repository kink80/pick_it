package tool.picky

import model.{PickyToolUser, PickyTool}
import org.specs.Specification

/**
 * Created with IntelliJ IDEA.
 * User: slavek
 * Date: 4/16/12
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */

class PickyToolRegistryTest extends Specification {

    "PickyToolRegistry" should {
      "register a tool" in {
        val tool: PickyTool = PickyTool.createRecord.name("test")
        val usr: PickyToolUser = PickyToolUser.createRecord.email("k@c.com")
        PickyToolRegistry.schedule(tool, usr, 500000L)
      }
    }
}
