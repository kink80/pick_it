package bootstrap.liftweb

import net.liftweb._
import common.{Box, Full}
import http._
import sitemap.{SiteMap, Menu, Loc}
import util.{ NamedPF }
import tool.picky.dbabstract.MongoConfig
import tool.picky.model.{UserEmail, DropboxIdVendor, PickyToolUser}
import tool.picky.ui.snippet.LoggedInUser


class Boot {
  def boot {

    MongoConfig.init

    LiftRules.resourceNames = "i18n/messages" :: LiftRules.resourceNames
    // where to search snippet
    LiftRules.addToPackages("tool.picky.ui")

    LiftRules.loggedInTest = Full(
      () => {
        if(LoggedInUser.get.isEmpty) false
        PickyToolUser.findUserByEmail(LoggedInUser.get) match {
          case Full(user) => {
            true
          }
          case _ => false
        }
      }
    )

    LiftRules.statelessRewrite.prepend(NamedPF("RegisterRewrite") {
      case RewriteRequest(
        ParsePath("register" :: "finish" :: Nil, _, _,_), _, _) =>
        RewriteResponse("register/finish" :: Nil)
      case RewriteRequest(
        ParsePath("register" :: Nil, _, _,_), _, _) =>
        RewriteResponse("register/register" :: Nil)
      case RewriteRequest(
        ParsePath("login" :: Nil, _, _,_), _, _) =>
        RewriteResponse("login/login" :: Nil)
      case RewriteRequest(
        ParsePath("dashboard" :: Nil, _, _,_), _, _) =>
        RewriteResponse("dashboard/dashboard" :: Nil)
    })

    // build sitemap
    val entries = (List(Menu("Home") / "index") :::
                   List(Menu("Finish") / "register/finish") :::
                   List(Menu("Register") / "register/register") :::
                   List(Menu("Login") / "login/login") :::
                   List(Menu("Dashboard") / "dashboard/dashboard") :::
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