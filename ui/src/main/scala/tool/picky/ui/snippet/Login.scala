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
        user.hashedPassword.is == SecurityHelpers.md5(pass) match {
          case true => {
            LoggedInUser.set(email)
            S.redirectTo("/dashboard")
          }
          case _ => {
            LoggedInUser.remove()
            S.error(S.?("ERROR_LOGIN_FAILED"))
          }
        }
      }
      case _ => {
        S.error(S.?("ERROR_LOGIN_FAILEDh"))
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
        "submit" -> SHtml.ajaxSubmit(S.?("LABEL_LOGIN"), auth))
    )
  }

}
