package com.mishlabs.xmpp
package protocol
package extensions
package forms
package fields

import scala.collection._
import scala.xml._

import Protocol._

object SingleText
{
    val fieldType = FieldTypeEnumeration.SingleText
    val fieldTypeName = fieldType.toString

    def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, value:Option[String]=None):SingleText =
    {
        val xml = SimpleField.build(SingleText.fieldType, identifier, label, description, required, value)
        return apply(xml)
    }

    def apply(xml:Node):SingleText = new SingleText(xml)
}

class SingleText(xml:Node) extends SimpleField(xml, SingleText.fieldType)
{
}