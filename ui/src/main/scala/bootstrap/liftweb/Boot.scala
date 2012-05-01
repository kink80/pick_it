package bootstrap.liftweb

import net.liftweb._
import common.Full
import http._
import sitemap.{SiteMap, Menu, Loc}
import util.{ NamedPF }
import tool.picky.dbabstract.MongoConfig
import tool.picky.model.{DropboxIdVendor, PickyToolUser}


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


    // build sitemap
    val entries = (List(Menu("Home") / "index") :::
                  
                  Nil)
    
    LiftRules.uriNotFound.prepend(NamedPF("404handler"){
      case (req,failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions","404"),"html",false,false))
    })
    
    LiftRules.setSiteMap(SiteMap(entries:_*))
    
    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.htmlProperties.default.set((r: Req) =>new XHtmlInHtml5OutProperties(r.userAgent))

    LiftRules.dispatch.append(DropboxIdVendor.dispatchPF)
    LiftRules.snippets.append(DropboxIdVendor.snippetPF)
  }
}