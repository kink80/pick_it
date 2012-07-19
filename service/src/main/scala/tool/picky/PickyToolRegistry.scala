package tool.picky

import  net.liftweb.json.JsonDSL._
import model._
import net.liftweb.mongodb.BsonDSL._
import net.liftweb.common.{Full, Box}
import collection.mutable.HashSet
import akka.actor.ActorSystem._
import akka.actor.Props._
import akka.util.Duration
import org.bson.types.ObjectId
import akka.actor.{Cancellable, Props, ActorSystem}
import java.util.concurrent.TimeUnit

private class PickyToolCancellable(val id: ObjectId, val cancellable: Cancellable) extends Cancellable {

  def cancel() {
    cancellable.cancel()
  }

  def isCancelled: Boolean = {
    cancellable.isCancelled
  }
}

object PickyToolRegistry {

  private val tasks = new HashSet[PickyToolCancellable]
  private val system = ActorSystem("PickyToolSystem")

  def schedule(toolName: String, userEmail: String, runsEvery: Long):Unit = {
    schedule(toolName, List(), userEmail, runsEvery)
  }

  def schedule(toolName: String,
               conditions: List[MetaTagCondition], userEmail: String, runsEvery: Long):Unit = {
    val scheduled = ScheduledTool.createRecord.name(toolName).userEmail(userEmail).runEvery(runsEvery).
      conditions(conditions).save

    PickyToolRegistry.register(scheduled)
  }

  def scheduleTraversing(targetUserEmail: String, targetTool: String, directory: String, conditions: List[MetaTagCondition], recipients: List[PickyToolRecipient]) = {
    val traverser = system.actorOf(Props[PickyToolTraverser], name = "traverser")
    traverser ! TraverseMessage(targetUserEmail, targetTool, "/", conditions, recipients)
  }

  def schedulePruning(targetUserEmail: String, targetEntry: String) = {

  }

  private def register(tool: ScheduledTool) = {
    tasks.filter(cnc => cnc.id.equals(tool.id.is)).foreach(cnc => {
      cnc.cancel()
      tasks.remove(cnc)
    })

    val listener = system.actorOf(Props[PickyToolSearcher], name = "searcher")

    val cancellable: PickyToolCancellable = new PickyToolCancellable(tool.id.is,
      system.scheduler.schedule(Duration.create(0, TimeUnit.MILLISECONDS), Duration.create(tool.runEvery.get.asInstanceOf[Long], TimeUnit.MILLISECONDS), listener,
      SearchMessage(tool.userEmail.get.toString, tool.id.get.toString)))
    tasks.add(cancellable)
  }
}
