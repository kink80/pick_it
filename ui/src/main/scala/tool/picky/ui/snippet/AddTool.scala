package tool.picky.ui.snippet

import xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.common.{Box, Full, Empty}
import net.liftweb.http.js.jquery.JqJsCmds.Unblock
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.ReplaceOptions
import net.liftweb.http.{RequestVar, S, SessionVar, SHtml}
import tool.picky.model.{PickyToolUser, ScheduledTool, PickyToolRecipient}

object AddTool {
  val unitMap = List(
    ("minute", "Minute(s)"),
    ("hour", "Hour(s)"),
    ("day", "Day(s)")
  )

  val unitsToPeriodMap = Map(
    "minute" -> List[Int](5,10,15,30,60),
    "hour" -> List[Int](1,2,3,4,5,6,12,15,18,21,24),
    "day" -> List[Int](1,2,5,10)
  )

  private object selectedPeriod extends RequestVar[String](unitMap.head._1)
  private object selectedValue extends RequestVar[Int](unitsToPeriodMap.get(selectedPeriod.get).get.head)

  def addTool() = {
    val period = selectedPeriod.get match {
      case "minute" => {
        selectedValue.get * 60 * 1000
      }
      case "hour" => {
        selectedValue.get * 60 * 60 * 1000
      }
      case "day" => {
        selectedValue.get * 24 * 60 * 60 * 1000
      }
    }
    PickyToolUser.findUserByEmail(LoggedInUser.get) match {
      case Full(user) => {
        ScheduledTool.createRecord.runEvery(period).userEmail(user.email.get).save
        Unblock
      }
      case _ => {
        S.redirectTo("/login")
      }
    }
  }

  def updateAmount(value: String) = {
       println(value);
  }

  def adjustTimeUnit(value: String): JsCmd = {
    selectedPeriod.set(value)
    selectedValue.set(unitsToPeriodMap.get(selectedPeriod.get).get.head)
    val periods = unitsToPeriodMap.get(value).get
    ReplaceOptions("unit", periods.map(v => (v.toString(), v.toString())), Empty)
  }

  def form(xhtml: NodeSeq): NodeSeq = {
    SHtml.ajaxForm(
      bind("form", xhtml,
        "period" -> SHtml.ajaxSelectObj[String](unitMap, Full(selectedPeriod.get), adjustTimeUnit _),
        "unit" -> SHtml.select(unitsToPeriodMap.get(selectedPeriod.get).get.map(v => (v.toString(), v.toString())), Full(selectedValue.get.toString()), updateAmount _, "id" -> "unit"),
        "target" -> SHtml.select(("file","File") :: ("folder","Folder") :: Nil, Empty, (String) => {}),
        "submit" -> (SHtml.hidden(addTool) ++ <input type="submit" value="Add"/>))
    )
  }
}
