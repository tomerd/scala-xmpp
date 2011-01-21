package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object PresenceFactory
		{
			def create(xml:Node):Presence = 
			{
				require(Presence.tag == xml.label)
				
				(xml \ "@type").text match
				{
					// FIXME, use the enum values (attribute stanzaType) instead of presenceTypeName, getting compilation error even with implicict cast
					case Available.presenceTypeName => Available(xml) // PresenceTypeEnumeration.Available
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