package com.mishlabs.xmpp
package protocol
package extensions
package forms
package fields

import scala.collection._
import scala.xml._

import Protocol._

object SingleJid
{
    val fieldType = FieldTypeEnumeration.SingleJid
    val fieldTypeName = fieldType.toString

    def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, jid:Option[JID]=None):SingleJid =
    {
        val xml = SimpleField.build(SingleJid.fieldType, identifier, label, description, required, jid)
        return apply(xml)
    }

    def apply(xml:Node):SingleJid = new SingleJid(xml)
}

class SingleJid(xml:Node) extends SimpleField(xml, SingleJid.fieldType)
{
}