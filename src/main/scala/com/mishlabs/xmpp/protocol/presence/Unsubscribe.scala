package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

object Unsubscribe
{
    val presenceType = PresenceTypeEnumeration.Unsubscribe

    def apply(id:Option[String], to:Option[JID], from:Option[JID]):Unsubscribe =
    {
        val xml = Presence.build(presenceType, id, to, from)
        return apply(xml)
    }

    def apply(xml:Node):Unsubscribe = new Unsubscribe(xml)

    def unapply(unsubscribe:Unsubscribe):Option[(Option[String], Option[JID], Option[JID], Option[Seq[Extension]])] = Some(unsubscribe.id, unsubscribe.to, unsubscribe.from, unsubscribe.extensions)
}

class Unsubscribe(xml:Node) extends Presence(xml, Unsubscribe.presenceType)
{
}