package tool.picky.dbi

import net.liftweb.util.ClassHelpers
import tool.picky.model.{MetaTagExpression, MetaTagCondition}

trait MetaTagEvaluator {

  def evaluateExpression(ref: AnyRef)(condition: MetaTagCondition): Boolean = {
    evaluateExpression(condition.expression.get,
      getValue(ref, condition), condition.value.get)
  }

  private def evaluateExpression(expression: MetaTagExpression.MetaTagExpression, metatagValue: String, comparedValue: String): Boolean = {
    expression match {
      case MetaTagExpression.LessThan => metatagValue < comparedValue
      case MetaTagExpression.LessThanEquals => metatagValue <= comparedValue
      case MetaTagExpression.GreaterThan => metatagValue > comparedValue
      case MetaTagExpression.GreaterThanEquals => metatagValue >= comparedValue
      case MetaTagExpression.Equals => metatagValue.compareTo(comparedValue) == 0
      case MetaTagExpression.NotEquals => metatagValue.compareTo(comparedValue) != 0
      case MetaTagExpression.Contains => metatagValue.contains(comparedValue)
    }
  }

  private def getValue(ref: AnyRef, condition: MetaTagCondition): String = {
    getValue(ref, condition.tag.toString())
  }

  private def getValue(ref: AnyRef, fieldName: String): String = {
    String.valueOf(ClassHelpers.invokeMethod(ref.getClass, ref, fieldName, Array()) openOr "")
  }

}
