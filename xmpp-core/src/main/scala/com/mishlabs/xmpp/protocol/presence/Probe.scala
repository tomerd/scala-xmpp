package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

object Probe
{
    val presenceType = PresenceTypeEnumeration.Probe

    def apply(id:Option[String], to:Option[JID], from:Option[JID]):Probe =
    {
        val xml = Presence.build(presenceType, id, to, from)
        return apply(xml)
    }

    def apply(xml:Node):Probe = new Probe(xml)

    def unapply(probe:Probe):Option[(Option[String], Option[JID], Option[JID], Option[Seq[Extension]])] = Some(probe.id, probe.to, probe.from, probe.extensions)
}

class Probe(xml:Node) extends Presence(xml, Probe.presenceType)
{
}