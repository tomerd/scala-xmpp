package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Probe
		{
			val presenceType = PresenceTypeEnumeration.Probe
			val presenceTypeName = presenceType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(id:Option[String], to:JID, from:JID):Probe =
			{					
				val xml = Presence.build(presenceType, id, to, from)
				return apply(xml)
			}
			
			def apply(xml:Node):Probe = new Probe(xml)
			
			def unapply(probe:Probe):Option[(Option[String], JID, JID, Option[Seq[Extension]])] = Some(probe.id, probe.to, probe.from, probe.extensions)
		}
		
		class Probe(xml:Node) extends Presence(xml, Probe.presenceType)
		{
		}
	}
}