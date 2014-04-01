package com.mishlabs.xmpp
package protocol
package iq

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object IQ
{
    val tag = "iq"

    def build(stanzaType:IQTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]):Node = Stanza.build(tag, stanzaType.toString, id, to, from, extension)

    def error(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension], condition:StanzaErrorCondition.Value, description:Option[String]):Node =
    {
        val children = mutable.ListBuffer[Node]()
        if (!extension.isEmpty) children += extension.get
        children += StanzaError(condition, description)
        Stanza.build(tag, IQTypeEnumeration.Error.toString, id, to, from, children)
    }
}

abstract class IQ(xml:Node, iqType:IQTypeEnumeration.Value) extends Stanza(xml)
{
    val extension:Option[Extension] = ExtensionsManager.getExtensions(this.xml) match
    {
        case Some(extensions) if (1 == extensions.length) => Some(extensions(0))
        case _ => None
    }
}

object IQTypeEnumeration extends Enumeration
{
    type value = Value

    val Unknown = Value("unknown") // internal use
    val Get = Value("get")
    val Set = Value("set")
    val Result = Value("result")
    val Error = Value("error")

    def fromString(string:String):Value = values.find( _.toString == string ).getOrElse(Unknown)
}