package com.mishlabs.xmpp
package protocol
package message

import scala.collection._
import scala.xml._

import Protocol._

object GroupChat
{
    val messageType = MessageTypeEnumeration.GroupChat

    def apply(to:Option[JID], from:Option[JID], body:Option[String]):GroupChat = apply(None, to, from, None, body, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], body:Option[String]):GroupChat = apply(id, to, from, None, body, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):GroupChat = apply(id, to, from, subject, body, None, None)

    def apply(to:Option[JID], from:Option[JID], body:String):GroupChat = apply(to, from, None, Some(body))

    def apply(to:Option[JID], from:Option[JID], subject:String, body:String):GroupChat = apply(None, to, from, Some(subject), Some(body))

    //def apply(to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):GroupChat = apply(None, to, from, subject, body, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extensions:Option[Seq[Extension]]):GroupChat =
    {
        val xml = Message.build(messageType, id, to, from, subject, body, thread, extensions)
        return apply(xml)
    }

    def apply(xml:Node):GroupChat = new GroupChat(xml)

    def unapply(groupchat:GroupChat):Option[(Option[String], Option[JID], Option[JID], Option[String], Option[String], Option[String], Option[Seq[Extension]])] = Some(groupchat.id, groupchat.to, groupchat.from, groupchat.subject, groupchat.body, groupchat.thread, groupchat.extensions)
}

class GroupChat(xml:Node) extends Message(xml, GroupChat.messageType)
{
}
