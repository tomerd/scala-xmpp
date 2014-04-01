package com.mishlabs.xmpp
package protocol

import scala.collection._
import scala.xml._

import iq._
import presence._
import message._

import Protocol._

object Stanza
{
    def apply(xml:String):Stanza = apply(XML.loadString(xml))

    def apply(xml:Node):Stanza =
    {
        xml.label match
        {
            case IQ.tag => iq.Builder(xml)
            case Presence.tag => presence.Builder(xml)
            case Message.tag => message.Builder(xml)
            case _ => throw new Exception("unknown stanza type, expected iq, presence or message")
        }
    }

    def build(name:String, stanzaType:String, id:Option[String], to:Option[JID], from:Option[JID], children:Option[Seq[Node]]=None):Node =
    {
        val kids:Seq[Node] = if (!children.isEmpty) children.get else Nil
        //if (!extension.isEmpty) children += extension.get
        var metadata:MetaData = Null
        if (null != stanzaType && !stanzaType.isEmpty) metadata = metadata.append(new UnprefixedAttribute("type", Text(stanzaType), Null))
        if (id.isDefined && !id.get.isEmpty) metadata = metadata.append(new UnprefixedAttribute("id", Text(id.get), Null))
        if (to.isDefined && !to.get.isEmpty) metadata = metadata.append(new UnprefixedAttribute("to", Text(to.get), Null))
        if (from.isDefined && !from.get.isEmpty) metadata = metadata.append(new UnprefixedAttribute("from", Text(from.get), Null))
        Elem(null, name, metadata, TopScope, kids:_*)
    }

    def withFrom(stanza:Stanza, from:JID) =
    {
        // TODO: find a more efficient way to do this
        val stanzaType = (stanza.xml \ "@type").text
        apply(build(stanza.xml.label, stanzaType, stanza.id, stanza.to, from, stanza.xml.child))
    }
}

abstract class Stanza(xml:Node) extends XmlWrapper(xml) with Packet
{
    val id:Option[String] = (this.xml \ "@id").text

    val to:Option[JID] = (this.xml \ "@to").text

    val from:Option[JID] = (this.xml \ "@from").text

    val language:Option[String] =
    {
        // TODO: find a better way to query this prefixed attribute with no namespace, this does not work: (this.xml \ "@lang").text
        this.xml.attributes.find("lang" == _.key) match
        {
            case Some(attribute) if (!attribute.value.text.isEmpty) => Some(attribute.value.text)
            case None => None
        }
    }

    private implicit def string2optjid(string:String):Option[JID] = if (null != string && !string.isEmpty) Some(JID(string)) else None
}
