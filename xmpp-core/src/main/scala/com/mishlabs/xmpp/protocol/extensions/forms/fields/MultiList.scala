package com.mishlabs.xmpp
package protocol
package extensions
package forms
package fields

import scala.collection._
import scala.xml._

import Protocol._

object MultiList
{
    val fieldType = FieldTypeEnumeration.MultiList
    val fieldTypeName = fieldType.toString

    def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, options:Option[Seq[FieldOption]]=None, values:Option[Seq[String]]=None):MultiList =
    {
        val xml = OptionsField.build(MultiList.fieldType, identifier, label, description, required, options, values)
        return apply(xml)
    }

    def apply(xml:Node):MultiList = new MultiList(xml)
}

class MultiList(xml:Node) extends OptionsField(xml, MultiList.fieldType)
{
}