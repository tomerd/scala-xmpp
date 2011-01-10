package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object PresenceFactory extends StanzaFactory[Presence]
		{
			def create(xml:Node):Presence = 
			{
				require("presence" == xml.label)
				
				(xml \ "@type").text match
				{
					// FIXME, use the enum values (attribute stanzaType) instead of stanzaTypeName, getting compilation error even with implicict cast
					case Available.stanzaTypeName => Available(xml) // PresenceTypeEnumeration.Available
					case Unavailable.stanzaTypeName => Unavailable(xml) // PresenceTypeEnumeration.Unavailable					
					case Subscribe.stanzaTypeName => Subscribe(xml) // PresenceTypeEnumeration.Subscribe
					case Subscribed.stanzaTypeName => Subscribed(xml) // PresenceTypeEnumeration.Subscribed					
					case Unsubscribe.stanzaTypeName => Unsubscribe(xml) // PresenceTypeEnumeration.Unsubscribe
					case Unsubscribed.stanzaTypeName => Unsubscribed(xml) // PresenceTypeEnumeration.Unsubscribed
					case Probe.stanzaTypeName => Probe(xml) // PresenceTypeEnumeration.Probe
					case Error.stanzaTypeName => Error(xml) // PresenceTypeEnumeration.Error
					case _ => throw new Exception("unknown presence stanza") // TODO, give a more detailed error message here
				}
			}
		}
		
	}
}