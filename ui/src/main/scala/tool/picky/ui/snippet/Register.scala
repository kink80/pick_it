package tool.picky.ui.snippet

import net.liftweb.util.Helpers._
import xml.{Text, NodeSeq}
import com.dropbox.client2.DropboxAPI
import com.dropbox.client2.session.{AccessTokenPair, RequestTokenPair, WebAuthSession}
import tool.picky.model.PickyToolUser
import net.liftweb.common.Box
import tool.picky.dbi.PickyToolApplication
import net.liftweb.util.SecurityHelpers
import net.liftweb.http.{SessionVar, RequestVar, S, SHtml}
import net.liftweb.http.js.JsCmds.RedirectTo

object DropboxSession extends SessionVar[WebAuthSession](null)


object Register {

  var email = ""; var pass = ""; var confirmPass = ""

  def verify() = {
    if(! pass.equals(confirmPass)) {
      false
    }
    val usr: PickyToolUser = PickyToolUser.findUserByEmail(email) openOr null
    if(usr != null) {
      false
    }

    PickyToolUser.createRecord.email(email).password(pass).save
    LoggedInUser.set(email)
    RedirectTo("/dashboard")
  }

  private def hash(in: String): Any = {
    pass = SecurityHelpers.md5(in.toString())
  }

  def form(xhtml: NodeSeq): NodeSeq = {
    SHtml.ajaxForm(
      bind("form", xhtml,
        "email" -> SHtml.text(email, email = _),
        "password" -> SHtml.password(pass, hash(_)),
        "confirm" -> SHtml.password(confirmPass, hash(_)),
        "submit" -> (SHtml.hidden(verify) ++ <input type="submit" value="Register"/>))
    )
  }


  object encodedEmail extends RequestVar[String](S.param("encodedEmail") openOr "")

  private def getRedirectTarget():String = {
    val boxedUsr: Box[PickyToolUser] = PickyToolUser.findUserByEmail(encodedEmail.is)
    val session: WebAuthSession  = PickyToolApplication.getWebAuthenticationSession()
    val mDBApi: DropboxAPI[WebAuthSession] = new DropboxAPI[WebAuthSession](session);
    DropboxSession.set(mDBApi.getSession())

    if(boxedUsr.isEmpty) {
      val url: String = S.hostAndPath + "/register/"
      val email: String =  encodedEmail.is
      val encoded: String = SecurityHelpers.base64EncodeURLSafe(email.getBytes);
      val waInfo: WebAuthSession.WebAuthInfo = mDBApi.getSession().getAuthInfo(url + encoded)
      waInfo.url
    } else {
      S.encodeURL(S.hostAndPath + "/dashboard/" + SecurityHelpers.base64EncodeURLSafe(encodedEmail.is.getBytes))
    }
  }

  def content(xhtml: NodeSeq): NodeSeq = {
    val session: WebAuthSession = DropboxSession.get
    if(session == null) {
      S.redirectTo("/error")
    }
    val tokens: AccessTokenPair = session.getAccessTokenPair()
    val reqPair: RequestTokenPair = new RequestTokenPair(tokens.key, tokens.secret)
    try {
      session.retrieveWebAccessToken(reqPair)
    } catch {
      case c => S.redirectTo("/error")
    }

    val mDBApi: DropboxAPI[WebAuthSession] = new DropboxAPI[WebAuthSession](session);
    val account: DropboxAPI.Account = mDBApi.accountInfo()

    PickyToolUser.createRecord.email(encodedEmail.is).dropboxTokenKey(reqPair.key).dropboxTokenSecret(reqPair.secret).
      firstName(account.displayName).save

    bind("content", xhtml,"dashboard" -> Text(account.displayName))
  }
}
