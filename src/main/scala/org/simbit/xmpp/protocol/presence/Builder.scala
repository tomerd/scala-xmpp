package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Builder
		{
			def build(xml:Node):Presence = 
			{
				require("presence" == xml.label)
				
				(xml \ "@type").text match
				{
					// FIXME, use the enum values (attribute stanzaType) instead of presenceTypeName, getting compilation error even with implicit cast
					case Available.presenceTypeName => AvailableBuilder.build(xml) // PresenceTypeEnumeration.Available
					case Unavailable.presenceTypeName => Unavailable(xml) // PresenceTypeEnumeration.Unavailable					
					case Subscribe.presenceTypeName => Subscribe(xml) // PresenceTypeEnumeration.Subscribe
					case Subscribed.presenceTypeName => Subscribed(xml) // PresenceTypeEnumeration.Subscribed					
					case Unsubscribe.presenceTypeName => Unsubscribe(xml) // PresenceTypeEnumeration.Unsubscribe
					case Unsubscribed.presenceTypeName => Unsubscribed(xml) // PresenceTypeEnumeration.Unsubscribed
					case Probe.presenceTypeName => Probe(xml) // PresenceTypeEnumeration.Probe
					case Error.presenceTypeName => Error(xml) // PresenceTypeEnumeration.Error
					case _ => throw new Exception("unknown presence stanza") // TODO, give a more detailed error message here
				}
			}
		}
		
	}
}