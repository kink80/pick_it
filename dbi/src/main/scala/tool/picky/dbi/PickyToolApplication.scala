package tool.picky.dbi

import com.dropbox.client2.DropboxAPI
import com.dropbox.client2.DropboxAPI._
import scala.collection.JavaConversions._
import com.dropbox.client2.session.{WebAuthSession, AppKeyPair, Session, AccessTokenPair}
import tool.picky.model.{MetaTagCondition, PickyToolUser}

object PickyToolApplication extends MetaTagEvaluator {

  private val applicationKey = "pm2mbyomzx2rdpu"
  private val applicationSecret = "0k9lwmnp0xjweg8"
  private val appAccessType = Session.AccessType.DROPBOX

  def getWebAuthenticationSession(): WebAuthSession = {
    val appKeys: AppKeyPair = new AppKeyPair(applicationKey, applicationSecret)
    new WebAuthSession(appKeys, appAccessType)
  }

  def list(root: String, user: PickyToolUser): Set[Entry] = {
    val appKeys: AppKeyPair = new AppKeyPair(applicationKey, applicationSecret)
    val session: WebAuthSession = new WebAuthSession(appKeys, appAccessType)
    val mDBApi: DropboxAPI[WebAuthSession] = new DropboxAPI[WebAuthSession](session)
    val reAuthTokens: AccessTokenPair = new AccessTokenPair(user.dropboxTokenKey.get, user.dropboxTokenSecret.get)
    mDBApi.getSession().setAccessTokenPair(reAuthTokens)

    // Now list the root
    // Heuristics? Maximum is 1000 files per search
    val entry: Entry = mDBApi.metadata(root, -1, null, true, null)
    entry.contents.toSet filter (e => !e.isDeleted)
  }

  def delete(entry: Entry, user: PickyToolUser) = {
    val appKeys: AppKeyPair = new AppKeyPair(applicationKey, applicationSecret)
    val session: WebAuthSession = new WebAuthSession(appKeys, appAccessType)
    val mDBApi: DropboxAPI[WebAuthSession] = new DropboxAPI[WebAuthSession](session)
    val reAuthTokens: AccessTokenPair = new AccessTokenPair(user.dropboxTokenKey.get, user.dropboxTokenSecret.get)
    mDBApi.getSession().setAccessTokenPair(reAuthTokens)

    mDBApi.delete(entry.path)
  }

  def evaluate(entry: Entry)(conditions: List[MetaTagCondition]): Boolean = {
    conditions.map(_ =>
      if (evaluateExpression(entry) _ == false) {
        false
      }
    )

    true
  }

}

