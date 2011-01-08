package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Probe
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Probe =
			{					
				val xml = Presence.build(PresenceTypeEnumeration.Probe, id, to, from, None, None, None, None)
				return apply(xml)
			}
			
			def apply(xml:Node):Probe = new Probe(xml)
		}
		
		class Probe(xml:Node) extends Presence(xml, PresenceTypeEnumeration.Probe)
		{
		}
	}
}