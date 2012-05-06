package tool.picky.ui.snippet

import xml.NodeSeq
import tool.picky.model.PickyToolUser
import net.liftweb.util.{SecurityHelpers, Helpers}
import Helpers._
import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.JsCmds.RedirectTo
import net.liftweb.http.{SessionVar, SHtml, S, RequestVar}
import net.liftweb.common.Full

object LoggedInUser extends SessionVar[String]("")

object Login {

  var email = ""; var pass = "";

  def auth() = {
    PickyToolUser.findUserByEmail(email) match {
      case Full(user) => {
        println(pass)
        println(user.password.value)
        user.password.match_?(pass) match {
          case true => {
            LoggedInUser.set(email)
            S.redirectTo("/dashboard")
          }
          case _ => {
            LoggedInUser.remove()
            S.error("User or password does not match")
          }
        }
      }
      case _ => {
        S.error("User or password does not match")
      }
    }
  }

  private def hash(in: String): Any = {
    pass = SecurityHelpers.md5(in.toString())
  }

  def form(xhtml: NodeSeq): NodeSeq = {
    SHtml.ajaxForm(
      bind("form", xhtml,
        "email" -> SHtml.text(email, email = _),
        "password" -> SHtml.password(pass, pass = _),
        "submit" -> (SHtml.hidden(auth) ++ <input type="submit" value="Login"/>))
    )
  }

}
