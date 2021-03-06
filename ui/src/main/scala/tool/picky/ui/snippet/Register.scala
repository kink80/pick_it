package tool.picky.ui.snippet

import net.liftweb.util.Helpers._
import xml.{Text, NodeSeq}
import com.dropbox.client2.DropboxAPI
import com.dropbox.client2.session.{AccessTokenPair, RequestTokenPair, WebAuthSession}
import tool.picky.model.PickyToolUser
import tool.picky.dbi.PickyToolApplication
import net.liftweb.util.SecurityHelpers
import net.liftweb.http.{SessionVar, RequestVar, S, SHtml}
import net.liftweb.common.{Empty, Full, Box}

object DropboxSession extends SessionVar[WebAuthSession](null)


object Register {

  var email = ""; var pass = ""; var confirmPass = ""

  def verify() = {
    pass == confirmPass match {
      case true => {
        PickyToolUser.findUserByEmail(email) match {
          case Full(user) => {
            S.error(S.?("ERROR_USER_EXISTS"))
          }
          case _ => {
            /*
            PickyToolUser.createRecord.email(email).hashedPassword(SecurityHelpers.md5(pass)).save
            LoggedInUser.set(email)
            */
            S.redirectTo(getRedirectTarget)
          }
        }
      }
      case false => {
        S.error(S.?("ERROR_PASSWORD_MISMATCH"))
      }
    }
  }

  def form(xhtml: NodeSeq): NodeSeq = {
    SHtml.ajaxForm(
      bind("form", xhtml,
        "email" -> SHtml.text(email, email = _),
        "password" -> SHtml.password(pass, pass = _),
        "confirm" -> SHtml.password(confirmPass, confirmPass = _),
        "submit" -> SHtml.ajaxSubmit(S.?("LABEL_REGISTER"), verify))
    )
  }


  object encodedEmail extends RequestVar[String](S.param("encodedEmail") openOr "")

  private def getRedirectTarget():String = {
    val boxedUsr: Box[PickyToolUser] = PickyToolUser.findUserByEmail(encodedEmail.is)
    val session: WebAuthSession  = PickyToolApplication.getWebAuthenticationSession()
    val mDBApi: DropboxAPI[WebAuthSession] = new DropboxAPI[WebAuthSession](session);
    DropboxSession.set(mDBApi.getSession())


    if(boxedUsr.isEmpty) {
      val target = S.encodeURL(S.hostAndPath + "/register/finish" + SecurityHelpers.base64EncodeURLSafe(encodedEmail.is.getBytes))
      val email: String =  encodedEmail.is
      val encoded: String = SecurityHelpers.base64EncodeURLSafe(email.getBytes);
      val waInfo: WebAuthSession.WebAuthInfo = mDBApi.getSession().getAuthInfo(target + encoded)
      waInfo.url
    } else {
      S.encodeURL(S.hostAndPath + "/dashboard" + SecurityHelpers.base64EncodeURLSafe(encodedEmail.is.getBytes))
    }
  }

  def finish(xhtml: NodeSeq): NodeSeq = {
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

    S.redirectTo(S.encodeURL(S.hostAndPath + "/dashboard" + SecurityHelpers.base64EncodeURLSafe(encodedEmail.is.getBytes)))
  }
}
