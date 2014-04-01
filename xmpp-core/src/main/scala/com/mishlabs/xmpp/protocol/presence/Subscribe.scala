package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

object Subscribe
{
    val presenceType = PresenceTypeEnumeration.Subscribe

    def apply(id:Option[String], to:Option[JID], from:Option[JID]):Subscribe =
    {
        val xml = Presence.build(presenceType, id, to, from)
        return apply(xml)
    }

    def apply(xml:Node):Subscribe = new Subscribe(xml)

    def unapply(subscribe:Subscribe):Option[(Option[String], Option[JID], Option[JID], Option[Seq[Extension]])] = Some(subscribe.id, subscribe.to, subscribe.from, subscribe.extensions)
}

class Subscribe(xml:Node) extends Presence(xml, Subscribe.presenceType)
{
}