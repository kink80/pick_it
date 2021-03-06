package tool.picky.ui.snippet

import xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.common.{Box, Full, Empty}
import net.liftweb.http.js.jquery.JqJsCmds.Unblock
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{RequestVar, S, SessionVar, SHtml}
import tool.picky.model._
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.http.js.JsCmds.ReplaceOptions
import net.liftweb.common.Full
import net.liftweb.http.js.JE.{JsRaw, JsFunc}
import tool.picky.PickyToolRegistry

object AddTool {

  val unitMap = List(
    ("minute", S.?("LABEL_MINUTES")),
    ("hour", S.?("LABEL_HOURS")),
    ("day", S.?("LABEL_DAYS"))
  )

  val unitsToPeriodMap = Map(
    "minute" -> List[Int](5,10,15,30,60),
    "hour" -> List[Int](1,2,3,4,5,6,12,15,18,21,24),
    "day" -> List[Int](1,2,5,10)
  )

  val numberUnits = List(
    ("second", S.?("LABEL_SECONDS")),
    ("minute", S.?("LABEL_MINUTES")),
    ("hour", S.?("LABEL_HOURS")),
    ("day", S.?("LABEL_DAYS")),
    ("week", S.?("LABEL_WEEKS")),
    ("year", S.?("LABEL_YEARS"))
  )

  private object selectedPeriod extends RequestVar[String](unitMap.head._1)
  private object selectedValue extends RequestVar[Int](unitsToPeriodMap.get(selectedPeriod.get).get.head)
  private object selectedAge extends RequestVar[String]("")
  private object selectedNumberUnit extends RequestVar[String](numberUnits.head._1)

  private def toMillis(unit: String, value: Int): Long = {
    unit match {
     case "second" => {
       value * 1000;
     }
     case "minute" => {
       value * 60 * 1000
     }
     case "hour" => {
       value * 60 * 60 * 1000
     }
     case "day" => {
       value * 24 * 60 * 60 * 1000
     }
     case "week" => {
       value * 7 * 24 * 60 * 60 * 1000
     }
     case "month" => {
       value * 30 * 60 * 60 * 1000
     }
     case "year" => {
       value * 365 * 24 * 60 * 60 * 1000
     }
   }
  }

  def addTool() = {
    val period = toMillis(selectedPeriod.get, selectedValue.get)
    val age = selectedAge.get match {
      case "" => {
        0
      }
      case default => {
        selectedAge.get.toInt
      }
    }
    PickyToolUser.findUserByEmail(LoggedInUser.get) match {
      case Full(user) => {
        val runsEveryLabel = numberUnits.filter(item => {
          val(name, _) = item
          name == selectedPeriod.get
        }).head._2
        val toolLabel = S.?("LABEL_TOOL_NAME", selectedValue.get, runsEveryLabel)
        val tool = ScheduledTool.createRecord.name(toolLabel).runEvery(period).userEmail(user.email.get)
        if(age > 0) {
          val cond = MetaTagCondition.createRecord.tag(MetaTag.Modified).expression(MetaTagExpression.LessThanEquals).value(age.toString)
          PickyToolRegistry.schedule(toolLabel, List(cond), user.email.toString(), period)
        } else {
          PickyToolRegistry.schedule(toolLabel, user.email.toString(), period)
        }
        Unblock & RedirectTo("/dashboard")
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

  def toggleShowAdvanced(option: Boolean): JsCmd = {
    if(option) JsShowId("advanced") else {
      selectedAge.set("")
      JsRaw("$('#age').val('')") & JsHideId("advanced")
    }
  }

  def form(xhtml: NodeSeq): NodeSeq = {
    SHtml.ajaxForm(
      bind("form", xhtml,
        "period" -> SHtml.ajaxSelectObj[String](unitMap, Full(selectedPeriod.get), adjustTimeUnit _),
        "unit" -> SHtml.select(unitsToPeriodMap.get(selectedPeriod.get).get.map(v => (v.toString(), v.toString())), Full(selectedValue.get.toString()), updateAmount _, "id" -> "unit"),
        "target" -> SHtml.select(("file", S.?("LABEL_FILE")) :: ("folder", S.?("LABEL_FOLDER")) :: Nil, Empty, (String) => {}),
        "showAdvanced" -> SHtml.ajaxCheckbox(false, toggleShowAdvanced _),
        "age" -> SHtml.text("", selectedAge.set(_), "id" -> "age"),
        "timeunit" -> SHtml.select(numberUnits, Full(selectedNumberUnit.get), selectedNumberUnit.set(_)),
        "submit" -> SHtml.ajaxSubmit(S.?("LABEL_OK"), addTool)
      )
    )
  }
}
