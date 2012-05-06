package tool.picky.ui.snippet

import net.liftweb.util._
import Helpers._
import net.liftweb.http.{SHtml}
import xml.{Text, NodeSeq}


object LogInForm {

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
