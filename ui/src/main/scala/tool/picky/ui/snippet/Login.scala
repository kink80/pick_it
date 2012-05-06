package tool.picky.ui.snippet

import xml.NodeSeq
import tool.picky.model.PickyToolUser
import net.liftweb.util.{SecurityHelpers, Helpers}
import Helpers._
import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.JsCmds.RedirectTo
import net.liftweb.http.{SessionVar, SHtml, S, RequestVar}

object LoggedInUser extends SessionVar[String]("")

object Login {

  var email = ""; var pass = "";

  def auth() = {
    val usr: PickyToolUser = PickyToolUser.findUserByEmail(email) openOr null
    if(usr != null && SecurityHelpers.md5(usr.password.is).equals(pass)) {
      LoggedInUser.set(email)
      RedirectTo("/dashboard")
    }
  }

  private def hash(in: String): Any = {
    pass = SecurityHelpers.md5(in.toString())
  }

  def form(xhtml: NodeSeq): NodeSeq = {
    SHtml.ajaxForm(
      bind("form", xhtml,
        "email" -> SHtml.text(email, email = _),
        "password" -> SHtml.password(pass, hash(_)),
        "submit" -> (SHtml.hidden(auth) ++ <input type="submit" value="Login"/>))
    )
  }

}
