package tool.picky

import dbabstract.MongoConfig
import model.{ScheduledTool, PickyToolUser, PickyTool}
import org.bson.types.ObjectId


object Tst extends App {

  override def main(args: Array[String]) = {

    MongoConfig.init

    val tool: PickyTool = PickyTool.createRecord.name("test")
    val usr: PickyToolUser = PickyToolUser.createRecord.email("k@c.com")
    //PickyToolRegistry.schedule(tool, usr, 500000L)

  }

}
