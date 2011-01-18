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
			val presenceType = PresenceTypeEnumeration.Probe
			val presenceTypeName = presenceType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Probe =
			{					
				val xml = Presence.build(presenceType, id, to, from)
				return apply(xml)
			}
			
			def apply(xml:Node):Probe = new Probe(xml)
		}
		
		class Probe(xml:Node) extends Presence(xml, Probe.presenceType)
		{
		}
	}
}