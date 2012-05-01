package tool.picky.ui.snippet

import xml.NodeSeq
import net.liftweb.http.{SHtml, S, RequestVar, SessionVar}
import net.liftweb.util._
import Helpers._
import tool.picky.model.PickyToolUser
import net.liftweb.common.Box
import com.dropbox.client2.session.{WebAuthSession, AppKeyPair}
import com.dropbox.client2.DropboxAPI
import tool.picky.dbi.PickyToolApplication

object LogInForm {

  private object email extends RequestVar("")

  def auth() = {
    val boxedUsr: Box[PickyToolUser] = PickyToolUser.findUserByEmail(email.is)
    if(boxedUsr.isDefined) {
     val usr: PickyToolUser = boxedUsr.openTheBox
     if(usr.dropboxTokenKey.is.eq("") || usr.dropboxTokenSecret.is.eq("")) {
       val session: WebAuthSession  = PickyToolApplication.getWebAuthenticationSession()
       val mDBApi: DropboxAPI[WebAuthSession] = new DropboxAPI[WebAuthSession](session);
       val waInfo: WebAuthSession.WebAuthInfo = mDBApi.getSession().getAuthInfo("/")
       waInfo.url
     } else {
       true
     }

    }
    // validate the user credentials and do a bunch of other stuff


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
        "email" -> SHtml.text(email.is, email(_), "maxlength" -> "40"),
        "submit" -> (SHtml.hidden(auth) ++ <input type="submit" value="Login"/>)))
  }
}
