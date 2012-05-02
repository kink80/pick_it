package tool.picky.ui.snippet

import xml.NodeSeq
import net.liftweb.util._
import Helpers._
import tool.picky.model.PickyToolUser
import com.dropbox.client2.DropboxAPI
import tool.picky.dbi.PickyToolApplication
import net.liftweb.common.{Logger, Box}
import net.liftweb.http.js.JsCmds.RedirectTo
import scala.Predef._
import net.liftweb.http.{SessionVar, SHtml, S, RequestVar}
import com.dropbox.client2.session.{Session, WebAuthSession, AppKeyPair}
import net.liftweb.http.js.JsCmds

object dropboxSession extends SessionVar[WebAuthSession](null)
object currentUserEmail extends SessionVar("")

object LogInForm {

  def auth() = {
    true
  }

  /**
   * This is the part of the snippet that creates the form elements and connects the client side components to
   * server side handlers.
   *
   * @param xhtml - the raw HTML that we are going to be manipulating.
   * @return NodeSeq - the fully rendered HTML
   */
  def login(xhtml: NodeSeq): NodeSeq = {
    SHtml.ajaxForm(
      bind("login", xhtml,
        "email" -> SHtml.text(currentUserEmail.is, currentUserEmail(_), "maxlength" -> "40"),
        "submit" -> (SHtml.hidden(auth) ++ <input type="submit" value="Login"/>)), JsCmds.Noop, RedirectTo(getRedirectTarget))
  }

  private def getRedirectTarget():String = {
    val boxedUsr: Box[PickyToolUser] = PickyToolUser.findUserByEmail(currentUserEmail.is)
    val session: WebAuthSession  = PickyToolApplication.getWebAuthenticationSession()
    val mDBApi: DropboxAPI[WebAuthSession] = new DropboxAPI[WebAuthSession](session);
    dropboxSession.set(mDBApi.getSession())

    if(boxedUsr.isEmpty) {

      val waInfo: WebAuthSession.WebAuthInfo = mDBApi.getSession().getAuthInfo(
        S.encodeURL(S.contextPath + "/register/" + SecurityHelpers.base64EncodeURLSafe(currentUserEmail.is.getBytes)))
      waInfo.url
    } else {
      S.encodeURL(S.contextPath + "/dashboard/" + SecurityHelpers.base64EncodeURLSafe(currentUserEmail.is.getBytes))
    }
  }
}
