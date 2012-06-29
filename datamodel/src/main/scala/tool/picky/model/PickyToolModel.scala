package tool.picky.model

import net.liftweb.common.{Empty, Full, Box}
import net.liftweb.mongodb.record.field.{BsonRecordField, BsonRecordListField, ObjectIdPk}
import net.liftweb.record.field._
import net.liftweb.mongodb.record._
import net.liftweb.record.{MegaProtoUser, MetaMegaProtoUser}
import net.liftweb.util.{FatLazy, FieldError}
import net.liftweb.util.Helpers._

case class UserEmail(base64Email: String)

object PickyToolRecipientsEnumeration extends Enumeration {
  type PickyToolRecipientsEnumeration = Value
  val Pruner, Reporter, Logger = Value
}

class PickyToolRecipient private() extends BsonRecord[PickyToolRecipient] {
  def meta = PickyToolRecipient

  object recipientType extends EnumField(this, PickyToolRecipientsEnumeration)
}

object PickyToolRecipient extends PickyToolRecipient with BsonMetaRecord[PickyToolRecipient]

object MetaTag extends Enumeration {
  type MetaTag = Value
  val Size, Modified, Path, MimeType = Value
}

object MetaTagExpression extends Enumeration {
  type MetaTagExpression = Value
  val LessThan, LessThanEquals, GreaterThan, GreaterThanEquals, Equals, NotEquals, Contains = Value
}

class MetaTagCondition private() extends BsonRecord[MetaTagCondition] {
  def meta = MetaTagCondition

  object tag extends EnumField(this, MetaTag)
  object expression extends EnumField(this, MetaTagExpression)
  object value extends StringField(this, 200)
}
object MetaTagCondition extends MetaTagCondition with BsonMetaRecord[MetaTagCondition]

class ToolOption private() extends BsonRecord[ToolOption] {
  def meta = ToolOption

  object key extends StringField(this, 100)

  object value extends StringField(this, 100)

}

object ToolOption extends ToolOption with BsonMetaRecord[ToolOption]

class ToolSettings private() extends MongoRecord[ToolSettings] with ObjectIdPk[ToolSettings] {
  def meta = ToolSettings

  object name extends StringField(this, 50)

  object settings extends BsonRecordListField(this, ToolOption)

}

object ToolSettings extends ToolSettings with MongoMetaRecord[ToolSettings] {
  override def collectionName = "toolsettings"
}

class PickyToolUser private() extends MongoRecord[PickyToolUser] with MegaProtoUser[PickyToolUser] {
  def meta = PickyToolUser

  object dropboxTokenKey extends StringField(this, 100)
  object dropboxTokenSecret extends StringField(this, 100)
  object hashedPassword extends StringField(this, 50)

  object ToolsSettings extends BsonRecordListField(this, ToolSettings)

  override def valUnique(errorMsg: => String)(emailValue: String) = {
    meta.findAll("email", emailValue) match {
      case Nil => Nil
      case usr :: Nil if (usr.id == id) => Nil
      case _ => List(FieldError(email, "The email should be unique"))
    }
  }

}

object PickyToolUser extends PickyToolUser with MongoMetaRecord[PickyToolUser] with MetaMegaProtoUser[PickyToolUser] {

  protected def userFromStringId(id: String): Box[PickyToolUser] = meta.find(id)

  protected def findUserByUniqueId(id: String): Box[PickyToolUser] = {
    var searchListHeadOption = meta.findAll("_id", id).headOption
    searchListHeadOption match {
      case Some(x) => Full(x)
      case None => return Empty
    }
  }

  /**
   * Given an username (probably email address), find the user
   */
  def findUserByEmail(email: String): Box[PickyToolUser] = {
    var searchListHeadOption = meta.findAll("email", email).headOption
    searchListHeadOption match {
      case Some(x) => Full(x)
      case None => return Empty
    }
  }

  protected def findUserByUserName(email: String) = findUserByEmail(email)

  override def homePage = if (loggedIn_?) "/dashboard" else "/"
}

class PickyTool private() extends MongoRecord[PickyTool] with ObjectIdPk[PickyTool] {
  def meta = PickyTool

  object name extends StringField(this, 100)

  object settings extends BsonRecordField(this, ToolSettings)
}

object PickyTool extends PickyTool with MongoMetaRecord[PickyTool] {
  override def collectionName = "pickyTooltools"
}

class ScheduledTool private() extends MongoRecord[ScheduledTool] with ObjectIdPk[ScheduledTool] {
  def meta = ScheduledTool

  object name extends StringField(this, 100)
  object runEvery extends LongField(this, 0)
  object running extends BooleanField(this, false)
  object lastRun extends LongField(this, 0)
  object userEmail extends EmailField(this, 50)

  object conditions extends BsonRecordListField(this, MetaTagCondition)
  object recipients extends BsonRecordListField(this, PickyToolRecipient)

  object settings extends BsonRecordField(this, ToolSettings)
}

object ScheduledTool extends ScheduledTool with MongoMetaRecord[ScheduledTool] {
  override def collectionName = "scheduledTools"
}