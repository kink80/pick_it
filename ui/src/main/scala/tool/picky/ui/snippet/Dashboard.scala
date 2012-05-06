package tool.picky.ui.snippet

import net.liftweb.util.Helpers
import net.liftweb.util.{SecurityHelpers, Helpers}
import tool.picky.model.PickyToolUser
import net.liftweb.common.Full
import net.liftweb.http.{S, SHtml}
import xml.{Text, NodeSeq}
import net.liftweb.util.Helpers._

object Dashboard {

  def list(xhtml: NodeSeq): NodeSeq = {
    PickyToolUser.findUserByEmail(LoggedInUser.get) match {
      case Full(user) => {
        user.ToolsSettings.get match {
          case Nil => Text("No tools configured")
          case items => items.flatMap(i =>
            bind("tool", xhtml,
              "name" -> {i.name},
              "edit" -> SHtml.link("/edittool", () => {}, Text("Edit"))
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
          "tooladd" -> SHtml.link("/addtool", () => {}, Text("Add"))
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
          }, Text("Logout")),
          "settings" -> SHtml.link("/settings", () => {
            //TODO finish this
          }, Text("Settings"))
        )
      }
      case _ => {
        S.redirectTo("/login")
      }
    }

  }
}
