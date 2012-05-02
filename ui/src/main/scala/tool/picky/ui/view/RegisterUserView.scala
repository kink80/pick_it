package tool.picky.ui.view

import net.liftweb.http.LiftView
import xml.NodeSeq
import net.liftweb.common.{Full}

object RegisterUserView extends LiftView {

  override def dispatch = {
    case "hi" => doRender _
  }

  def doRender () : NodeSeq = {
    <lift:surround with="default" at="content">
      <b>hai!</b>
    </lift:surround>

  }
}
