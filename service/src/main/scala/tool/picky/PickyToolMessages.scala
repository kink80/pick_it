package tool.picky

import akka.actor.Actor
import dbi.PickyToolApplication
import model._
import net.liftweb.common.Box
import net.liftweb.json.JsonDSL._
import net.liftweb.mongodb.BsonDSL._
import com.dropbox.client2.DropboxAPI.Entry



sealed trait PickyToolMessage
case class FinishSearching(targetTool: String) extends PickyToolMessage
case class SearchMessage(targetUserEmail: String, targetTool: String) extends PickyToolMessage
case class EntryMessage(targetUserEmail: String, targetTool: String, entry: Entry) extends PickyToolMessage
case class TraverseMessage(targetUserEmail: String, targetTool: String, directory: String, conditions: List[MetaTagCondition] , recipients: List[PickyToolRecipient]) extends PickyToolMessage
case class PruningMessage(targetUserEmail: String, targetEntryPath: String) extends PickyToolMessage
case class ReportMessage(targetUserEmail: String, targetEntryPath: String) extends PickyToolMessage
case class LogMessage(targetUserEmail: String, targetEntryPath: String) extends PickyToolMessage

class PickyToolSearcher extends Actor {

  def receive = {
    case SearchMessage(targetUserEmail, targetTool) => {
      val scheduledToolBox: Box[ScheduledTool]  = ScheduledTool.find(targetTool)
      if(scheduledToolBox.isDefined) {
        val scheduledTool = scheduledToolBox.get
        if (!scheduledTool.running.value) {
          scheduledTool.running(true).save
        }

        PickyToolRegistry.scheduleTraversing(targetUserEmail, targetTool, "/",
          scheduledTool.conditions.get, scheduledTool.recipients.get)
      }
    }
    case EntryMessage(targetUserEmail, targetTool, entry) => {
      val user:PickyToolUser = PickyToolUser.find("email" -> targetUserEmail).openTheBox
      PickyToolApplication.delete(entry, user)
    }
    case FinishSearching(targetTool) => {
      val scheduledToolBox: Box[ScheduledTool] = ScheduledTool.find(targetTool)
      if(scheduledToolBox.isDefined) {
        val scheduledTool = scheduledToolBox.get
        if (scheduledTool.running.value) {
          scheduledTool.running(false).save
        }
      }
    }
  }
}

class PickyToolTraverser extends Actor {

  def receive = {
    case TraverseMessage(targetUserEmail, targetTool, directory, conditions, recipients) => {

      val user:PickyToolUser = PickyToolUser.find("email" -> targetUserEmail).openTheBox
      traverse(directory, user, conditions, targetTool)
      sender ! FinishSearching
    }
  }

  def traverse(directory: String, user: PickyToolUser, conditions: List[MetaTagCondition], targetTool: String):Any = {
    val entries: Set[Entry] = PickyToolApplication.list(directory, user)
    val filtered: Set[Entry] = entries filter(PickyToolApplication.evaluate(_)(conditions))
    filtered map(e => sender ! EntryMessage(user.email.get, targetTool, e) )

    val directoriesToTraverse: Set[Entry] = entries filter (e => e.isDir && !filtered.contains(e))
    directoriesToTraverse map(e => traverse(e.path, user, conditions, targetTool))
  }
}