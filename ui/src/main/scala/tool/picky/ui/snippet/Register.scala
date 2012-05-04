package tool.picky.ui.snippet

import net.liftweb.util.Helpers._
import xml.{Text, NodeSeq}
import net.liftweb.http.{RequestVar, S, SHtml}
import com.dropbox.client2.DropboxAPI
import com.dropbox.client2.session.{AccessTokenPair, RequestTokenPair, WebAuthSession}
import tool.picky.model.PickyToolUser

object Register {
  object encodedEmail extends RequestVar[String](S.param("encodedEmail") openOr "")

  def content(xhtml: NodeSeq): NodeSeq = {
    val session: WebAuthSession = DropboxSession.get
    if(session == null) {
      S.redirectTo("/error")
    }
    val reqPair: RequestTokenPair = new RequestTokenPair(session.getAppKeyPair.key, session.getAppKeyPair.secret)
    try {
      session.retrieveWebAccessToken(reqPair)
    } catch {
      case c => S.redirectTo("/error")
    }

    val tokens: AccessTokenPair = session.getAccessTokenPair()

    val mDBApi: DropboxAPI[WebAuthSession] = new DropboxAPI[WebAuthSession](session);
    val account: DropboxAPI.Account = mDBApi.accountInfo()

    PickyToolUser.createRecord.email(encodedEmail.is).dropboxTokenKey(tokens.key).dropboxTokenSecret(tokens.secret).
      firstName(account.displayName).save

    bind("content", xhtml,"dashboard" -> Text(account.displayName))
  }
}
