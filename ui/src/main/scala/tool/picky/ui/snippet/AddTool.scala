package tool.picky.ui.snippet

import xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.common.{Box, Full, Empty}
import net.liftweb.http.js.jquery.JqJsCmds.Unblock
import net.liftweb.http.js.JsCmds.{SetValById, ReplaceOptions}
import net.liftweb.http.{RequestVar, S, SessionVar, SHtml}
import tool.picky.model._
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.common.Full
import net.liftweb.common.Full
import net.liftweb.common.Full
import net.liftweb.http.js.JsCmds.SetValById
import net.liftweb.http.js.JsCmds.ReplaceOptions
import collection.mutable.ListBuffer

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

  def makeExpressionPair(v: MetaTagExpression.MetaTagExpression): (String, String) = {
    v match {
      case MetaTagExpression.Contains => (v.toString(), "contains")
      case MetaTagExpression.Equals => (v.toString(), "equals to")
      case MetaTagExpression.GreaterThan => (v.toString(), "is greater than")
      case MetaTagExpression.GreaterThanEquals => (v.toString(), "is greater or equals than")
      case MetaTagExpression.LessThan => (v.toString(), "is less than")
      case MetaTagExpression.LessThanEquals => (v.toString(), "is less or equals than")
      case MetaTagExpression.NotEquals => (v.toString(), "is not equal to")
    }

  }

  def makeDateExpressionPair(v: MetaTagExpression.MetaTagExpression): (String, String) = {
    v match {
      case MetaTagExpression.Contains => (v.toString(), "contains")
      case MetaTagExpression.Equals => (v.toString(), "is")
      case MetaTagExpression.GreaterThan => (v.toString(), "after")
      case MetaTagExpression.LessThan => (v.toString(), "before")
      case MetaTagExpression.NotEquals => (v.toString(), "is not")
    }

  }

  def filterDateExpressions(exp: MetaTagExpression.MetaTagExpression) = {
      exp != MetaTagExpression.Contains && exp != MetaTagExpression.GreaterThanEquals &&
        exp != MetaTagExpression.LessThanEquals
  }

  def getAllowableExpression(v: MetaTag.MetaTag): Set[(String, String)] = {
    v match {
      case MetaTag.MimeType => { MetaTagExpression.values.filter(v => v.==(MetaTagExpression.Contains)).map(makeExpressionPair _) }
      case MetaTag.Path => { MetaTagExpression.values.filter(v => v.==(MetaTagExpression.Contains)).map(makeExpressionPair _) }
      case MetaTag.Modified => { MetaTagExpression.values.filter(filterDateExpressions _).map(makeDateExpressionPair _) }
      case default => MetaTagExpression.values.filter(v => v.!=(MetaTagExpression.Contains)).map(makeExpressionPair _)
    }
  }

  def expressionForm(xhtml: NodeSeq): NodeSeq = {
    val metatags = MetaTag.values.map(v =>
      v match {
        case MetaTag.Size => { (v, "Size") }
        case MetaTag.MimeType => { (v, "Mime Type") }
        case MetaTag.Modified => { (v, "Modified Date") }
        case MetaTag.Path => { (v,"Path") }
      }
    ).toSeq

    val conditions = new ListBuffer[MetaTagCondition]
    val initial = metatags.head._1
    val expressionsInitial = getAllowableExpression(initial)

    var currentExpressionValue = ""
    var currentTag = initial
    var currentExpression = MetaTagExpression.withName(expressionsInitial.head._1)
    var currentExpressionString = currentExpression.toString()

    def bindList( xhtml: NodeSeq): NodeSeq = {
      conditions.flatMap{case (c) => bind("taglistname", xhtml, "name" -> c.tag.value.toString)}
    }

    def setTag(value: MetaTag.MetaTag): JsCmd = {
      currentTag = value
      currentExpressionString = getAllowableExpression(value).head._1
      currentExpressionValue = ""
      SetValById("expressionValue", currentExpressionValue) &
      ReplaceOptions("expression", getAllowableExpression(value).toList, Empty)
    }

    def setExpression(value: String): JsCmd = {
      currentExpressionString = value
      currentExpression = MetaTagExpression.withName(value)
      JsCmds.Noop
    }

    def addExpression(): JsCmd = {
      val cnd:MetaTagCondition = MetaTagCondition.createRecord.tag(currentTag).expression(currentExpression).value(currentExpressionValue)
      conditions += cnd
      bindList _
      JsCmds.Noop
    }

    SHtml.ajaxForm(
      bind("form", xhtml,
        "metatag" -> SHtml.ajaxSelectObj(metatags, Full(initial), setTag _),
        "expression" -> SHtml.ajaxSelect(expressionsInitial.toSeq, Full(currentExpressionString), setExpression _, "id" -> "expression"),
        "tagvalue" -> SHtml.text(currentExpressionValue, currentExpressionValue = _, "id" -> "expressionValue"),
        "addbutton" -> (SHtml.hidden(addExpression) ++ <input type="submit" value="Add"/>) ,
        "taglist" -> bindList _
      )
    )
  }

  def form(xhtml: NodeSeq): NodeSeq = {
    SHtml.ajaxForm(
      bind("form", xhtml,
        "period" -> SHtml.ajaxSelectObj[String](unitMap, Full(selectedPeriod.get), adjustTimeUnit _),
        "unit" -> SHtml.select(unitsToPeriodMap.get(selectedPeriod.get).get.map(v => (v.toString(), v.toString())), Full(selectedValue.get.toString()), updateAmount _, "id" -> "unit"),
        "target" -> SHtml.select(("file","File") :: ("folder","Folder") :: Nil, Empty, (String) => {}),
        "submit" -> (SHtml.hidden(addTool) ++ <input type="submit" value="Finish"/>)
      )
    )
  }

  def button(xhtml: NodeSeq): NodeSeq = {
    bind("form", xhtml,
      "submit" -> (SHtml.hidden(addTool) ++ <input type="submit" value="Finish"/>)
    )

  }
}
