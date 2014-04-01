package com.mishlabs.xmpp
package protocol
package message

import scala.collection._
import scala.xml._

import Protocol._

object Headline
{
    val messageType = MessageTypeEnumeration.Headline

    def apply(to:Option[JID], from:Option[JID], body:Option[String]):Headline = apply(None, to, from, None, body, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], body:Option[String]):Headline = apply(id, to, from, None, body, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):Headline = apply(id, to, from, subject, body, None, None)

    def apply(to:Option[JID], from:Option[JID], body:String):Headline = apply(to, from, None, Some(body))

    def apply(to:Option[JID], from:Option[JID], subject:String, body:String):Headline = apply(None, to, from, Some(subject), Some(body))

    //def apply(to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):Headline = apply(None, to, from, subject, body, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extensions:Option[Seq[Extension]]):Headline =
    {
        val xml = Message.build(messageType, id, to, from, subject, body, thread, extensions)
        return apply(xml)
    }

    def apply(xml:Node):Headline = new Headline(xml)

    def unapply(headline:Headline):Option[(Option[String], Option[JID], Option[JID], Option[String], Option[String], Option[String], Option[Seq[Extension]])] = Some(headline.id, headline.to, headline.from, headline.subject, headline.body, headline.thread, headline.extensions)
}

class Headline(xml:Node) extends Message(xml, Headline.messageType)
{
}
