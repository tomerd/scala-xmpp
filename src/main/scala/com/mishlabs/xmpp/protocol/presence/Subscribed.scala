package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

object Subscribed
{
    val presenceType = PresenceTypeEnumeration.Subscribed

    def apply(id:Option[String], to:Option[JID], from:Option[JID]):Subscribed =
    {
        val xml = Presence.build(presenceType, id, to, from)
        return apply(xml)
    }

    def apply(xml:Node):Subscribed = new Subscribed(xml)

    def unapply(subscribed:Subscribed):Option[(Option[String], Option[JID], Option[JID], Option[Seq[Extension]])] = Some(subscribed.id, subscribed.to, subscribed.from, subscribed.extensions)
}

class Subscribed(xml:Node) extends Presence(xml, Subscribed.presenceType)
{
}