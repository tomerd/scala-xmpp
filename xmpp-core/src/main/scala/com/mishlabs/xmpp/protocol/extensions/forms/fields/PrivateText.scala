package com.mishlabs.xmpp
package protocol
package extensions
package forms
package fields

import scala.collection._
import scala.xml._

import Protocol._

object PrivateText
{
    val fieldType = FieldTypeEnumeration.PrivateText
    val fieldTypeName = fieldType.toString

    def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, value:Option[String]=None):PrivateText =
    {
        val xml = SimpleField.build(PrivateText.fieldType, identifier, label, description, required, value)
        return apply(xml)
    }

    def apply(xml:Node):PrivateText = new PrivateText(xml)
}

class PrivateText(xml:Node) extends SimpleField(xml, PrivateText.fieldType)
{
}