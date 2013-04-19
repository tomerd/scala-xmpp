package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

object Unavailable
{
    val presenceType = PresenceTypeEnumeration.Unavailable

    def apply(id:Option[String], to:Option[JID], from:Option[JID]):Unavailable = apply(id, to, from, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):Unavailable =
    {
        val xml = Presence.build(presenceType, id, to, from, None, extensions)
        return apply(xml)
    }

    def apply(xml:Node):Unavailable = new Unavailable(xml)

    def unapply(unavailable:Unavailable):Option[(Option[String], Option[JID], Option[JID], Option[Seq[Extension]])] = Some(unavailable.id, unavailable.to, unavailable.from, unavailable.extensions)
}

class Unavailable(xml:Node) extends Presence(xml, Unavailable.presenceType)
{
}
