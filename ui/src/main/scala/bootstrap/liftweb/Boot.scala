package bootstrap.liftweb

import net.liftweb._
import common.Full
import http._
import sitemap.{SiteMap, Menu, Loc}
import util.{ NamedPF }
import tool.picky.dbabstract.MongoConfig
import tool.picky.model.{UserEmail, DropboxIdVendor, PickyToolUser}


class Boot {
  def boot {

    MongoConfig.init
  
    // where to search snippet
    LiftRules.addToPackages("tool.picky.ui")

    LiftRules.loggedInTest = Full(
      () => {
        PickyToolUser.loggedIn_?
      }
    )

    LiftRules.statelessRewrite.prepend(NamedPF("RegisterRewrite") {
      case RewriteRequest(
      ParsePath("register" :: encodedEmail :: Nil, _, _,_), _, _) =>
        RewriteResponse(
          "register/register" :: Nil, Map("encodedEmail" -> encodedEmail)
        )
    })

    // build sitemap
    val entries = (List(Menu("Home") / "index") :::
                   List(Menu("Register") / "register/register") :::
                  Nil)
    LiftRules.setSiteMap(SiteMap(entries:_*))
    
    LiftRules.uriNotFound.prepend(NamedPF("404handler"){
      case (req,failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions","404"),"html",false,false))
    })
    

    
    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.htmlProperties.default.set((r: Req) =>new XHtmlInHtml5OutProperties(r.userAgent))

    LiftRules.dispatch.append(DropboxIdVendor.dispatchPF)
    LiftRules.snippets.append(DropboxIdVendor.snippetPF)
  }
}