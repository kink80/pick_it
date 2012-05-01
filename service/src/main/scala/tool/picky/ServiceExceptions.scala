package tool.picky

case class ToolAlreadyExists(errorMsg:String)  extends Exception(errorMsg:String)
