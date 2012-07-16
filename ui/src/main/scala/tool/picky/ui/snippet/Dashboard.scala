package tool.picky.ui.snippet

import net.liftweb.util.Helpers
import net.liftweb.util.{SecurityHelpers, Helpers}
import tool.picky.model.{ScheduledTool, PickyToolUser}
import net.liftweb.common.Full
import net.liftweb.http.{S, SHtml}
import xml.{Text, NodeSeq}
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml._
import net.liftweb.http.js.jquery.JqJsCmds.ModalDialog
import net.liftweb.http.js.JsCmds.Alert
import net.liftweb.mongodb.BsonDSL._
import net.liftweb.http.js.{JsCmd, JsCmds}

object Dashboard {

  private def deleteTool(tool: ScheduledTool): JsCmd = {
    tool.delete_!
    JsCmds.RedirectTo("/dashboard")
  }

  def list(xhtml: NodeSeq): NodeSeq = {
    PickyToolUser.findUserByEmail(LoggedInUser.get) match {
      case Full(user) => {
        ScheduledTool.findAll("userEmail" -> user.email.get) match {
          case Nil => <li><lift:loc locid="LABEL_NO_CLEANUP">No cleanup has been planned so far.</lift:loc></li>
          case items => items.flatMap(i =>
            bind("tool", xhtml,
              "name" -> {i.name},
              "edit" -> SHtml.link("/edittool", () => {}, Text(S.?("LABEL_CHANGE"))),
              "delete" -> SHtml.ajaxButton(S.?("LABEL_DELETE"), () => deleteTool(i))
            )
          )
        }
      }
      case _ => {
         S.redirectTo("/login")
      }
    }
  }

  def action(xhtml: NodeSeq): NodeSeq = {
    PickyToolUser.findUserByEmail(LoggedInUser.get) match {
      case Full(user) => {
        bind("action", xhtml,
          "tooladd" -> ajaxButton(Text(S.?("LABEL_ADD")),
            () => S.runTemplate(List("tool/add")).
              map(ns => ModalDialog(ns)) openOr
              S.redirectTo("/login"))
        )
      }
      case _ => {
        S.redirectTo("/login")
      }
    }
  }

  def account(xhtml: NodeSeq): NodeSeq = {
    PickyToolUser.findUserByEmail(LoggedInUser.get) match {
      case Full(user) => {
        bind("dashboard", xhtml,
          "logout" -> SHtml.link("/", () => {
            LoggedInUser.remove()
          }, Text(S.?("LABEL_LOGOUT"))),
          "settings" -> SHtml.link("/settings", () => {
            //TODO finish this
          }, Text(S.?("LABEL_SETTINGS")))
        )
      }
      case _ => {
        S.redirectTo("/login")
      }
    }

  }
}
