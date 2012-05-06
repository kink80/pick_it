package tool.picky.ui.snippet

import xml.NodeSeq
import net.liftweb.http.SHtml
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.util.{SecurityHelpers, Helpers}

object Dashboard {

  def list(xhtml: NodeSeq): NodeSeq = {
    bind("greet", xhtml,
      "user" -> SHtml.text(LoggedInUser.get, (_) => {})
    )
  }
}
