package com.mishlabs.xmpp
package protocol
package extensions
package forms
package fields

import scala.collection._
import scala.xml._

import Protocol._

object Fixed
{
    val fieldType = FieldTypeEnumeration.Fixed
    val fieldTypeName = fieldType.toString

    def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, value:Option[String]=None):Fixed =
    {
        val xml = SimpleField.build(Fixed.fieldType, identifier, label, description, required, value)
        return apply(xml)
    }

    def apply(xml:Node):Fixed = new Fixed(xml)
}

class Fixed(xml:Node) extends SimpleField(xml, Fixed.fieldType)
{
}