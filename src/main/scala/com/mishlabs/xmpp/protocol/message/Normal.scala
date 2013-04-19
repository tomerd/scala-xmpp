package com.mishlabs.xmpp
package protocol
package message

import scala.collection._
import scala.xml._

import Protocol._

object Normal
{
    val messageType = MessageTypeEnumeration.Normal

    def apply(to:Option[JID], from:Option[JID], body:Option[String]):Normal = apply(None, to, from, None, body, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], body:Option[String]):Normal = apply(id, to, from, None, body, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):Normal = apply(id, to, from, subject, body, None, None)

    def apply(to:Option[JID], from:Option[JID], body:String):Normal = apply(to, from, None, Some(body))

    def apply(to:Option[JID], from:Option[JID], subject:String, body:String):Normal = apply(None, to, from, Some(subject), Some(body))

    //def apply(to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):Normal = apply(None, to, from, subject, body, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extensions:Option[Seq[Extension]]):Normal =
    {
        val xml = Message.build(messageType, id, to, from, subject, body, thread, extensions)
        return apply(xml)
    }

    def apply(xml:Node):Normal = new Normal(xml)

    def unapply(normal:Normal):Option[(Option[String], Option[JID], Option[JID], Option[String], Option[String], Option[String], Option[Seq[Extension]])] = Some(normal.id, normal.to, normal.from, normal.subject, normal.body, normal.thread, normal.extensions)
}

class Normal(xml:Node) extends Message(xml, Normal.messageType)
{
}
