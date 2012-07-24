package tool.picky.ui.snippet

import net.liftweb.util._
import Helpers._
import net.liftweb.http.{S, SHtml}
import xml.{Text, NodeSeq}
import tool.picky.ui.snippet.Register.encodedEmail


object LogInForm {

  def dashboard(xhtml: NodeSeq): NodeSeq = {
    S.redirectTo(S.encodeURL(S.hostAndPath + "/dashboard"))
  }

  def login(xhtml: NodeSeq): NodeSeq = {
    SHtml.ajaxForm(
      bind("login", xhtml,
        "submit" -> SHtml.link("/login", () => {}, Text("Login"))))
  }

  def register(xhtml: NodeSeq): NodeSeq = {
    SHtml.ajaxForm(
      bind("register", xhtml,
        "submit" -> SHtml.link("/register", () => {}, Text("Register"))))
  }


}
