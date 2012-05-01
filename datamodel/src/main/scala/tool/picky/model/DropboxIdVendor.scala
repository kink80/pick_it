package tool.picky.model

import net.liftweb.openid.{OpenIDConsumer, WellKnownEndpoints, WellKnownAttributes, SimpleOpenIDVendor}
import net.liftweb.common.{Logger, Full}
import org.openid4java.discovery.DiscoveryInformation
import org.openid4java.message.AuthRequest

object DropboxIdVendor extends SimpleOpenIDVendor with Logger {
  def ext(di: DiscoveryInformation, authReq: AuthRequest): Unit = {
    import WellKnownAttributes._
    WellKnownEndpoints.findEndpoint(di) map {ep =>
      ep.makeAttributeExtension(List(Email, FullName, FirstName, LastName)) foreach {ex =>
        authReq.addExtension(ex)
      }
    }
  }
  override def createAConsumer = new OpenIDConsumer[UserType] {
    info("Creating consumer")
    beforeAuth = Full(ext _)
  }
}
