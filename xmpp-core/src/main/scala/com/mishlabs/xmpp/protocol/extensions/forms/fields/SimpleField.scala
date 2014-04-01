package com.mishlabs.xmpp
package protocol
package extensions
package forms
package fields

import scala.collection._
import scala.xml._

import Protocol._

protected object SimpleField
{
    def build(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, value:Option[String]=None):Node =
    {
        val children = mutable.ListBuffer[Node]()
        if (!value.isEmpty) children += <value>{ value }</value>
        Field.build(fieldType, identifier, label, description, required, children)
    }
}

abstract class SimpleField(xml:Node, fieldType:FieldTypeEnumeration.Value) extends Field(xml, fieldType)
{
    val value:Option[String] = (this.xml \ "value").text
}