package com.mishlabs.xmpp
package protocol
package message

import scala.collection._
import scala.xml._

import Protocol._

object Extended
{
    val messageType = MessageTypeEnumeration.Extended

    def apply(to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):Extended = apply(None, to, from, extensions)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):Extended =
    {
        val xml = Message.build(messageType, id, to, from, None, None, None, extensions)
        return apply(xml)
    }

    def apply(xml:Node):Extended = new Extended(xml)

    def unapply(extended:Extended):Option[(Option[String], Option[JID], Option[JID], Option[Seq[Extension]])] = Some(extended.id, extended.to, extended.from, extended.extensions)
}

class Extended(xml:Node) extends Message(xml, Extended.messageType)
{
}
